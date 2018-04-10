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

import app.commons.utils.RuntimeUtil.Command;

public final class RuntimeConstants {

    private static final String WIN_CPU_USAGE = "wmic cpu get loadpercentage";
    private static final String WIN_MEM_USAGE = "shell-scripts/mem-usage-win.cmd";
    private static final String MAC_CPU_USAGE = "shell-scripts/cpu-usage-macos.sh";
    private static final String MAC_MEM_USAGE = "echo 0";
    private static final String LNX_CPU_USAGE = "shell-scripts/cpu-usage-lnx.sh";
    private static final String LNX_MEM_USAGE = "shell-scripts/mem-usage-lnx.sh";

    public static final Command CMD_MEM_USAGE_LNX = new Command(Command.class.getClassLoader().getResource(LNX_MEM_USAGE).getPath());
    public static final Command CMD_MEM_USAGE_MAC = new Command(MAC_MEM_USAGE);
    public static final Command CMD_MEM_USAGE_WIN = new Command(Command.class.getClassLoader().getResource(WIN_MEM_USAGE).getPath());
    public static final Command CMD_CPU_USAGE_LNX = new Command(Command.class.getClassLoader().getResource(LNX_CPU_USAGE).getPath());
    public static final Command CMD_CPU_USAGE_MAC = new Command(Command.class.getClassLoader().getResource(MAC_CPU_USAGE).getPath());
    public static final Command CMD_CPU_USAGE_WIN = new Command(WIN_CPU_USAGE);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private RuntimeConstants() {
        super();
    }
}
