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
package app.commons.utils;

import static app.commons.constants.MessageConstants.ERROR_READ_CPU_USAGE;
import static app.commons.constants.MessageConstants.ERROR_READ_MEM_USAGE;
import static app.commons.constants.RuntimeConstants.CMD_CPU_USAGE_LNX;
import static app.commons.constants.RuntimeConstants.CMD_CPU_USAGE_MAC;
import static app.commons.constants.RuntimeConstants.CMD_CPU_USAGE_WIN;
import static app.commons.constants.RuntimeConstants.CMD_MEM_USAGE_LNX;
import static app.commons.constants.RuntimeConstants.CMD_MEM_USAGE_MAC;
import static app.commons.constants.RuntimeConstants.CMD_MEM_USAGE_WIN;

import java.io.IOException;

import app.commons.enums.SystemEnums.OsType;
import app.commons.utils.RuntimeUtil.Command;

public final class ResourceMonitorUtil {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private ResourceMonitorUtil() {
        super();
    }

    public static Float getCpuUsage() {
        try {
            Float cpuUsage = getOutputAsFloat(getCpuUsageCommand());
            if (OsType.MAC == HostInfoUtil.getOsType()) {
                cpuUsage = cpuUsage / HostInfoUtil.getCoresNumber();
            }
            return cpuUsage;
        } catch (IOException | InterruptedException e) {
            LoggerUtil.error(ERROR_READ_CPU_USAGE, e);
        }
        return null;
    }

    public static Float getMemUsage() {
        try {
            return getOutputAsFloat(getMemUsageCommand());
        } catch (IOException | InterruptedException e) {
            LoggerUtil.error(ERROR_READ_MEM_USAGE, e);
        }
        return null;
    }

    private static Float getOutputAsFloat(final Command cmd) throws IOException, InterruptedException {
        final String result = RuntimeUtil.execAndGetResponseString(cmd);
        return Float.valueOf(StringUtil.removeNonNumeric(result));
    }

    private static Command getCpuUsageCommand() {
        Command cmd = null;
        switch (HostInfoUtil.getOsType()) {
            case WINDOWS:
                cmd = CMD_CPU_USAGE_WIN;
                break;
            case MAC:
                cmd = CMD_CPU_USAGE_MAC;
                break;
            default:
                cmd = CMD_CPU_USAGE_LNX;
                break;
        }
        return cmd;
    }

    private static Command getMemUsageCommand() {
        Command cmd = null;
        switch (HostInfoUtil.getOsType()) {
            case WINDOWS:
                cmd = CMD_MEM_USAGE_WIN;
                break;
            case MAC:
                cmd = CMD_MEM_USAGE_MAC;
                break;
            default:
                cmd = CMD_MEM_USAGE_LNX;
                break;
        }
        return cmd;
    }
}
