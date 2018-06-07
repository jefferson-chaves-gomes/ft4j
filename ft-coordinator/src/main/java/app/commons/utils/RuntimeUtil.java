/*
 *******************************************************************************
 * Copyright 2017 Contributors to Exact Sciences Institute, Department Computer Science, University of Brasília - UnB
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************
 */
package app.commons.utils;

import static app.commons.constants.MessageConstants.ERROR_RUNTIME_EXECUTION;
import static app.commons.constants.StringConstants.LINE_BREAK;
import static app.commons.constants.StringConstants.SPACE_STRING;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RuntimeUtil {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private RuntimeUtil() {
        super();
    }

    public static String execAndGetResponseString(final Command command) throws IOException, InterruptedException {
        return execAndGetResponseString(command, null);
    }

    public static Process exec(final Command command) throws IOException, InterruptedException {
        return exec(command, null);
    }

    synchronized public static String execAndGetResponseString(final Command command, final Map<String, String> env) throws IOException, InterruptedException {

        final ProcessBuilder builder = new ProcessBuilder(command.getLstCommands());
        final Map<String, String> currentEnv = builder.environment();
        if (env != null) {
            for (final Map.Entry<String, String> entry : env.entrySet()) {
                currentEnv.put(entry.getKey(), entry.getValue());
            }
        }
        final Process process = builder.start();
        return getProcessReturn(process);
    }

    synchronized public static Process exec(final Command command, final Map<String, String> env) throws IOException {

        final ProcessBuilder builder = new ProcessBuilder(command.getLstCommands());
        final Map<String, String> currentEnv = builder.environment();
        if (env != null) {
            for (final Map.Entry<String, String> entry : env.entrySet()) {
                currentEnv.put(entry.getKey(), entry.getValue());
            }
        }
        return builder.start();
    }

    synchronized private static String getProcessReturn(final Process process) throws InterruptedException {
        final StreamGobblerThread errorGobbler = new RuntimeUtil().new StreamGobblerThread(process.getErrorStream());
        final StreamGobblerThread outputGobbler = new RuntimeUtil().new StreamGobblerThread(process.getInputStream());
        process.waitFor();
        errorGobbler.join();
        outputGobbler.join();
        final StringBuilder builder = new StringBuilder();
        builder.append(outputGobbler.getOutputstream());
        builder.append(errorGobbler.getOutputstream());
        final int length = builder.length();
        final int lastLineBreak = builder.lastIndexOf(LINE_BREAK);
        if (length > 0 && lastLineBreak == length - 1) {
            builder.deleteCharAt(lastLineBreak);
        }
        builder.trimToSize();
        return builder.toString();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Class.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static class Command {

        private final List<String> lstCommands = new ArrayList<>();

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Constructors.
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        public Command(final String... command) {
            for (final String arg : command) {
                final String[] split = arg.split(SPACE_STRING);
                for (final String args : split) {
                    this.lstCommands.add(args);
                }
            }
        }

        public List<String> getLstCommands() {
            return this.lstCommands;
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // * @see java.lang.Object#toString()
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        @Override
        public String toString() {
            return StreamUtil.toString(this.lstCommands);
        }
    }

    private class StreamGobblerThread extends Thread {

        private final InputStream stream;
        private String outputstream;

        // --------------------------------------------------------------
        // Constructors.
        // --------------------------------------------------------------
        public StreamGobblerThread(final InputStream stream) {
            super();
            this.stream = stream;
            super.start();
        }

        // --------------------------------------------------------------
        // * @see java.lang.Thread#run()
        // --------------------------------------------------------------
        @Override
        public void run() {
            try (
                 final InputStreamReader reader = new InputStreamReader(this.stream);
                 final BufferedReader buffer = new BufferedReader(reader);) {
                final StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = buffer.readLine()) != null) {
                    builder.append(line);
                    builder.append(LINE_BREAK);
                }
                this.outputstream = builder.toString();
            } catch (final IOException e) {
                LoggerUtil.error(ERROR_RUNTIME_EXECUTION, e);
            }
        }

        // --------------------------------------------------------------
        // Get/Set.
        // --------------------------------------------------------------
        public String getOutputstream() {
            return this.outputstream;
        }
    }
}