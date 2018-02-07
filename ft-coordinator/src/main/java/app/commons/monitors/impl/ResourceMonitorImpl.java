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
package app.commons.monitors.impl;

import static app.commons.constants.StringConstants.EMPTY_STRING;
import static app.commons.constants.StringConstants.PERCENT_SIMBOL;

import java.io.IOException;

import app.commons.monitors.ResourceMonitor;
import app.commons.utils.EnumUtil.OsType;
import app.commons.utils.HostInfoUtil;
import app.commons.utils.LoggerUtil;
import app.commons.utils.RuntimeUtil;
import app.commons.utils.RuntimeUtil.Command;

public class ResourceMonitorImpl implements ResourceMonitor {

    private static final String ERROR_ATTEMPTING_TO_READ_THE_CPU_USAGE = "Error attempting to read the CPU usage";
    private static final String MAC_CPU_USAGE = "shell-scripts/cpu-usage-macos.sh";
    private static final String WIN_CPU_USAGE = "wmic cpu get loadpercentage";
    private static final String LNX_CPU_USAGE = "wmic cpu get loadpercentage";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.commons.monitors.ResourceMonitor#getCpuUsage()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Float getCpuUsage() {
        try {
            final Command cmd = this.getCpuUsageCommand();
            final String result = RuntimeUtil.execAndGetResponseString(cmd);
            Float cpuUsage = Float.valueOf(result.replaceAll(PERCENT_SIMBOL, EMPTY_STRING));
            if (OsType.MAC == HostInfoUtil.getOsType()) {
                cpuUsage = cpuUsage / HostInfoUtil.getCoresNumber();
            }
            return cpuUsage;
        } catch (IOException | InterruptedException e) {
            LoggerUtil.warn(ERROR_ATTEMPTING_TO_READ_THE_CPU_USAGE, e);
        }
        return null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.commons.monitors.ResourceMonitor#getMemUsage()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Float getMemUsage() {
        return null;
    }

    private Command getCpuUsageCommand() {

        Command cmd = null;
        switch (HostInfoUtil.getOsType()) {
            case WINDOWS:
                cmd = new Command(WIN_CPU_USAGE);
                break;
            case MAC:
                final String shellScriptFilePath = this.getClass().getClassLoader().getResource(MAC_CPU_USAGE).getPath();
                cmd = new Command(shellScriptFilePath);
                break;
            default:
                cmd = new Command(LNX_CPU_USAGE);
                break;
        }
        return cmd;
    }
}