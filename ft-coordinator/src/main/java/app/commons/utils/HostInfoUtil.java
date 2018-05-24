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

import java.lang.management.ManagementFactory;

import app.commons.enums.SystemEnums.OsType;

public final class HostInfoUtil {

    private static final String AIX = "aix";
    private static final String HP_UX = "hp-ux";
    private static final String SOLARIS = "solaris";
    private static final String SUNOS = "sunos";
    private static final String SUN_OS = "sun os";
    private static final String MAC_OS = "mac os";
    private static final String UNIX = "unix";
    private static final String DIGITAL_UNIX = "digital unix";
    private static final String IRIX = "irix";
    private static final String FREEBSD = "freebsd";
    private static final String MPE_IX = "mpe/ix";
    private static final String LINUX = "linux";
    private static final String WINDOWS = "windows";

    private static final int CORES_NUMBER;
    private static final String OS_ARCH;
    private static final String OS_NAME;
    private static final String OS_VERSION;
    private static final OsType OS_TYPE;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // statics blocks.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    static {
        CORES_NUMBER = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        OS_ARCH = ManagementFactory.getOperatingSystemMXBean().getArch();
        OS_NAME = ManagementFactory.getOperatingSystemMXBean().getName();
        OS_VERSION = ManagementFactory.getOperatingSystemMXBean().getVersion();
        OS_TYPE = detemineOsType();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private HostInfoUtil() {
        super();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static int getCoresNumber() {
        return CORES_NUMBER;
    }

    public static String getOsArch() {
        return OS_ARCH;
    }

    public static String getOsName() {
        return OS_NAME;
    }

    public static String getOsVersion() {
        return OS_VERSION;
    }

    public static OsType getOsType() {
        return OS_TYPE;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static OsType detemineOsType() {

        final String osName = OS_NAME.toLowerCase();
        if (osName.contains(WINDOWS)) {
            return OsType.WINDOWS;
        }
        if (osName.contains(LINUX)
                || osName.contains(MPE_IX)
                || osName.contains(FREEBSD)
                || osName.contains(IRIX)
                || osName.contains(DIGITAL_UNIX)
                || osName.contains(UNIX)) {
            return OsType.UNIX;
        }
        if (osName.contains(MAC_OS)) {
            return OsType.MAC;
        }
        if (osName.contains(SUN_OS)
                || osName.contains(SUNOS)
                || osName.contains(SOLARIS)
                || osName.contains(HP_UX)
                || osName.contains(AIX)) {
            return OsType.POSIX_UNIX;
        }
        return OsType.OTHER;
    }
}