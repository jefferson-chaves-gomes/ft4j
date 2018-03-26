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
package app.commons.monitors;

public interface ResourceMonitor {

    public static final String WIN_CPU_USAGE = "wmic cpu get loadpercentage";
    public static final String WIN_MEM_USAGE = "shell-scripts/mem-usage-win.cmd";
    public static final String MAC_CPU_USAGE = "shell-scripts/cpu-usage-macos.sh";
    public static final String MAC_MEM_USAGE = "";
    public static final String LNX_CPU_USAGE = "";
    public static final String LNX_MEM_USAGE = "";
    public static final String ERROR_READ_CPU_USAGE = "Error attempting to read the CPU usage";
    public static final String ERROR_READ_MEM_USAGE = "Error attempting to read the MEMORY usage";

    public Float getCpuUsage();

    public Float getMemUsage();
}
