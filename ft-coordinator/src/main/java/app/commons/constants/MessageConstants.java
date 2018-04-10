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

public final class MessageConstants {

    public static final String ERROR_REGISTER_COORDINATOR = "Error attempting to register at FTCoordinator";
    public static final String ERROR_READ_CPU_USAGE = "Error attempting to read the CPU usage";
    public static final String ERROR_READ_MEM_USAGE = "Error attempting to read the MEMORY usage";
    public static final String SHUTDOWN_FINISHED = "Shutdown finished";
    public static final String CANCEL_NON_FINISHED_TASKS = "Cancel non-finished tasks";
    public static final String ERROR_TASKS_INTERRUPTED = "Tasks interrupted";
    public static final String ATTEMPT_TO_SHUTDOWN_EXECUTOR = "Attempt to shutdown executor";
    public static final String FT_MODULE_INITIALIZED_SUCCESSFULLY = "FTModule initialized successfully";
    public static final String TRYING_TO_REGISTER_MODULE_AT_COORDINATOR = "Trying to register module at coordinator";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public MessageConstants() {
        super();
    }

}
