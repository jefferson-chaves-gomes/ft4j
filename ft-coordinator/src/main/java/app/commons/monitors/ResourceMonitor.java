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
package app.commons.monitors;

import static app.commons.constants.ResourceMonitorConstants.CMD_CPU_USAGE_LNX;
import static app.commons.constants.ResourceMonitorConstants.CMD_CPU_USAGE_MAC;
import static app.commons.constants.ResourceMonitorConstants.CMD_CPU_USAGE_WIN;
import static app.commons.constants.ResourceMonitorConstants.CMD_MEM_USAGE_LNX;
import static app.commons.constants.ResourceMonitorConstants.CMD_MEM_USAGE_MAC;
import static app.commons.constants.ResourceMonitorConstants.CMD_MEM_USAGE_WIN;

import java.io.IOException;

import app.commons.utils.HostInfoUtil;
import app.commons.utils.RuntimeUtil;
import app.commons.utils.RuntimeUtil.Command;
import app.commons.utils.StringUtil;

public abstract class ResourceMonitor {
    
    public abstract Float getCpuUsage();

    public abstract Float getMemUsage();

    protected Float getOutputAsFloat(final Command cmd) throws IOException, InterruptedException {
        final String result = RuntimeUtil.execAndGetResponseString(cmd);
        return Float.valueOf(StringUtil.removeNonNumeric(result));
    }

    protected Command getCpuUsageCommand() {
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

    protected Command getMemUsageCommand() {
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
