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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUtil {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private FileUtil() {
        super();
    }

    public static String read(final InputStream inputStream) throws IOException {

        final byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        return new String(buffer);
    }

    public static void write(final InputStream inputStream, final File file) throws IOException {

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        final byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        try (
             final OutputStream outStream = new FileOutputStream(file)) {
            outStream.write(buffer);
        }
    }
}