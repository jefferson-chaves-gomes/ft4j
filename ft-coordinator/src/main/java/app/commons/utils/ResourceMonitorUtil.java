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

import static app.commons.constants.MessageConstants.ERROR_KILL_PID;
import static app.commons.constants.MessageConstants.ERROR_READ_CPU_USAGE;
import static app.commons.constants.MessageConstants.ERROR_READ_MEM_USAGE;
import static app.commons.constants.RuntimeConstants.CMD_LNX_CPU_USAGE;
import static app.commons.constants.RuntimeConstants.CMD_LNX_MEM_USAGE;
import static app.commons.constants.RuntimeConstants.CMD_MAC_CPU_USAGE;
import static app.commons.constants.RuntimeConstants.CMD_MAC_MEM_USAGE;
import static app.commons.constants.RuntimeConstants.CMD_WIN_CPU_USAGE;
import static app.commons.constants.RuntimeConstants.CMD_WIN_MEM_USAGE;
import static app.commons.constants.RuntimeConstants.LNX_KILL_PID;
import static app.commons.constants.RuntimeConstants.MAC_KILL_PID;
import static app.commons.constants.RuntimeConstants.WIN_KILL_PID;

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

    public static boolean kill(final Integer pid) {
        try {
            RuntimeUtil.exec(getKillCommand(pid));
        } catch (IOException | InterruptedException e) {
            LoggerUtil.error(ERROR_KILL_PID, e);
            return false;
        }
        return true;
    }

    public static boolean start(final String startupCommand) throws IOException, InterruptedException {
        RuntimeUtil.exec(new Command(startupCommand));
        return true;
    }

    private static Float getOutputAsFloat(final Command cmd) throws IOException, InterruptedException {
        final String result = RuntimeUtil.execAndGetResponseString(cmd);
        return Float.valueOf(StringUtil.removeNonNumeric(result));
    }

    private static Command getCpuUsageCommand() {
        Command cmd = null;
        switch (HostInfoUtil.getOsType()) {
            case WINDOWS:
                cmd = CMD_WIN_CPU_USAGE;
                break;
            case MAC:
                cmd = CMD_MAC_CPU_USAGE;
                break;
            default:
                cmd = CMD_LNX_CPU_USAGE;
                break;
        }
        return cmd;
    }

    private static Command getMemUsageCommand() {
        Command cmd = null;
        switch (HostInfoUtil.getOsType()) {
            case WINDOWS:
                cmd = CMD_WIN_MEM_USAGE;
                break;
            case MAC:
                cmd = CMD_MAC_MEM_USAGE;
                break;
            default:
                cmd = CMD_LNX_MEM_USAGE;
                break;
        }
        return cmd;
    }

    private static Command getKillCommand(final Integer pid) {
        Command cmd = null;
        switch (HostInfoUtil.getOsType()) {
            case WINDOWS:
                cmd = new Command(String.format(WIN_KILL_PID, pid));
                break;
            case MAC:
                cmd = new Command(String.format(MAC_KILL_PID, pid));
                break;
            default:
                cmd = new Command(String.format(LNX_KILL_PID, pid));
                break;
        }
        return cmd;
    }
}
