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

import org.apache.log4j.Logger;

public final class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger(LoggerUtil.class);

    private LoggerUtil() {
        super();
    }

    synchronized public static void error(final Throwable t) {
        error(t.getMessage(), t);
    }

    synchronized public static void error(final Object message, final Throwable t) {
        LOGGER.error(message, t);
    }

    synchronized public static void warn(final Object message) {
        LOGGER.warn(message);
    }

    synchronized public static void warn(final Object message, final Throwable t) {
        LOGGER.warn(message, t);
    }

    synchronized public static void info(final Object message) {
        LOGGER.info(message);
    }

    synchronized public static void debug(final Object string) {
        LOGGER.debug(string);
    }
}