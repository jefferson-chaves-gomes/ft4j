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
package app.commons.monitors.impl;

import static app.commons.constants.ResourceMonitorConstants.ERROR_READ_CPU_USAGE;
import static app.commons.constants.ResourceMonitorConstants.ERROR_READ_MEM_USAGE;

import java.io.IOException;

import app.commons.monitors.ResourceMonitor;
import app.commons.utils.EnumUtil.OsType;
import app.commons.utils.HostInfoUtil;
import app.commons.utils.LoggerUtil;

public class ResourceMonitorImpl extends ResourceMonitor {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.commons.monitors.ResourceMonitor#getCpuUsage()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Float getCpuUsage() {
        try {
            Float cpuUsage = super.getOutputAsFloat(super.getCpuUsageCommand());
            if (OsType.MAC == HostInfoUtil.getOsType()) {
                cpuUsage = cpuUsage / HostInfoUtil.getCoresNumber();
            }
            return cpuUsage;
        } catch (IOException | InterruptedException e) {
            LoggerUtil.warn(ERROR_READ_CPU_USAGE, e);
        }
        return null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.commons.monitors.ResourceMonitor#getMemUsage()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Float getMemUsage() {
        try {
            return super.getOutputAsFloat(super.getMemUsageCommand());
        } catch (IOException | InterruptedException e) {
            LoggerUtil.warn(ERROR_READ_MEM_USAGE, e);
        }
        return null;
    }

}