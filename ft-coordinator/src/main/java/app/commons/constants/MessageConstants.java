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
    public static final String ERROR_KILL_PID = "Error attempting to kill Process PID";
    public static final String ERROR_RUNTIME_EXECUTION = "Runtime execution error!";
    public static final String SHUTDOWN_FINISHED = "Shutdown finished";
    public static final String CANCEL_NON_FINISHED_TASKS = "Cancel non-finished tasks";
    public static final String ERROR_TASKS_INTERRUPTED = "Tasks interrupted";
    public static final String START_FT_COORDINATOR_CALLED = "startFtCoordinator() called: ";
    public static final String WAITING_START_FT_COORDINATOR_START = "Waiting startFtCoordinator() start...";
    public static final String FT_COORDINATOR_START_OUTPUT = "FTCoordinator start output: \n ********************************************* %s \n ********************************************* \n";
    public static final String ATTEMPT_TO_SHUTDOWN_COORDINATOR = "Attempt to shutdown the FTCoordinator";
    public static final String ATTEMPT_TO_SHUTDOWN_EXECUTOR = "Attempt to shutdown scheduledExecutors";
    public static final String ATTEMPT_TO_SHUTDOWN_FT_DETECTION_EXECUTORS = "Attempt to shutdown the Fault Tolerance Detection Executors";
    public static final String FT_MODULE_INITIALIZED_SUCCESSFULLY = "FTModule and Communication with FTCoordinator was initialized successfully";
    public static final String TRYING_TO_CONTACT_MODULE_AT_COORDINATOR = "Trying to contact the coordinator";
    public static final String TRYING_TO_REGISTER_MODULE_AT_COORDINATOR = "Trying to register module at coordinator";
    public static final String THE_PARTNER_WAS_STARTED_AGAIN = "The Partner was started again";
    public static final String STARTING_THE_PARTNER_FROM_PATH = "Starting the Partner from path: %s";
    public static final String STARTING_THE_PARTNER_WITH_COMMAND = "Starting the Partner with command: %s";
    public static final String THE_PARTNER_WAS_KILLED = "The Partner was killed";
    public static final String KILLING_THE_PARTNER_PID = "Killing the Partner - PID: %d";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public MessageConstants() {
        super();
    }

}
