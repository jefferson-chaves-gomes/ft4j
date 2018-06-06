/*
 * ******************************************************************************
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
 * *****************************************************************************
 */
package app.commons.constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import app.FaultToleranceCoordinator;
import app.commons.enums.SystemEnums.OsType;
import app.commons.utils.FileUtil;
import app.commons.utils.HostInfoUtil;
import app.commons.utils.LoggerUtil;
import app.commons.utils.RuntimeUtil;
import app.commons.utils.RuntimeUtil.Command;

public final class RuntimeConstants {

    private static final String WIN_CPU_USAGE = "wmic cpu get loadpercentage";
    private static final String WIN_MEM_USAGE = "shell-scripts/mem-usage-win.cmd";
    private static final String MAC_CPU_USAGE = "shell-scripts/cpu-usage-macos.sh";
    private static final String MAC_MEM_USAGE = "echo 0";
    private static final String LNX_CPU_USAGE = "shell-scripts/cpu-usage-lnx.sh";
    private static final String LNX_MEM_USAGE = "shell-scripts/mem-usage-lnx.sh";

    public static Command CMD_MEM_USAGE_LNX;
    public static Command CMD_MEM_USAGE_MAC;
    public static Command CMD_MEM_USAGE_WIN;
    public static Command CMD_CPU_USAGE_LNX;
    public static Command CMD_CPU_USAGE_MAC;
    public static Command CMD_CPU_USAGE_WIN;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // static block.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    static {
        try {
            CMD_MEM_USAGE_LNX = new Command(createAuxFileAndGetPath(LNX_MEM_USAGE));
            CMD_MEM_USAGE_MAC = new Command(MAC_MEM_USAGE);
            CMD_MEM_USAGE_WIN = new Command(createAuxFileAndGetPath(WIN_MEM_USAGE));
            CMD_CPU_USAGE_LNX = new Command(createAuxFileAndGetPath(LNX_CPU_USAGE));
            CMD_CPU_USAGE_MAC = new Command(createAuxFileAndGetPath(MAC_CPU_USAGE));
            CMD_CPU_USAGE_WIN = new Command(WIN_CPU_USAGE);

            configFilesPermisstion();

        } catch (IOException | InterruptedException e) {
            LoggerUtil.error(e);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private RuntimeConstants() throws IOException, InterruptedException {
        super();
    }

    private static String createAuxFileAndGetPath(final String zippedPath) throws IOException {

        final File file = new File(FaultToleranceCoordinator.properties.getShellScriptsPath() + zippedPath);
        try (
             InputStream inputStream = Command.class.getClassLoader().getResource(zippedPath).openStream();) {
            FileUtil.write(inputStream, file);
        }
        return file.getAbsolutePath();
    }

    private static void configFilesPermisstion() throws IOException, InterruptedException {

        final String execPermission = "chmod 755 ";
        if (OsType.WINDOWS != HostInfoUtil.getOsType()) {
            final String[] shellScriptArray = new String[] {
                    CMD_CPU_USAGE_MAC.toString(),
                    CMD_CPU_USAGE_LNX.toString(),
                    CMD_MEM_USAGE_LNX.toString()
            };
            for (final String shellScritpPath : shellScriptArray) {
                final Command command = new Command(execPermission + shellScritpPath);
                RuntimeUtil.execAndGetResponseString(command);
            }
        }
    }
}
