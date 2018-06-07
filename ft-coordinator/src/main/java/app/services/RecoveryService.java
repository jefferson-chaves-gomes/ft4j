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
package app.services;

import static app.commons.enums.SystemEnums.ExecutionStatus.RECOVERY_MODE;
import static app.commons.enums.SystemEnums.ExecutionStatus.STOPPED;

import app.commons.utils.LoggerUtil;
import app.models.Technique;

public class RecoveryService extends FaultToleranceService implements Runnable {

    private static final String FAULT_DETECTED_STARTING_THE_RECOVERY_SERVICE = "Fault Detected... Starting the Recovery Service. Technique: ";
    private static final String CANCELING_THE_EXECUTION_OF_REACTIVE_SERVICE = "Canceling the execution of Reactive Service";
    private static final String CANCELING_THE_EXECUTION_OF_PROACTIVE_SERVICE = "Canceling the execution of Proactive Service";
    private final Technique technique;
    private final String moduleId;
    private final String taskStartupCommand;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public RecoveryService(final String moduleId, final String taskStartupCommand, final Technique technique) {
        super();
        this.moduleId = moduleId;
        this.technique = technique;
        this.taskStartupCommand = taskStartupCommand;
    }

    public void startService() {

        FaultToleranceService.status = RECOVERY_MODE;
        LoggerUtil.info(FAULT_DETECTED_STARTING_THE_RECOVERY_SERVICE + this.technique.getClass().getName());

        if (proactiveService != null) {
            LoggerUtil.info(CANCELING_THE_EXECUTION_OF_PROACTIVE_SERVICE);
            proactiveService.cancel(true);
        }
        if (reactiveService != null) {
            LoggerUtil.info(CANCELING_THE_EXECUTION_OF_REACTIVE_SERVICE);
            reactiveService.cancel(true);
        }

        FaultToleranceService.threadExecutors.submit(this);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see java.lang.Runnable#run()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void run() {

        LoggerUtil.info("Performing recovery... " + this.technique.getClass().getName());
        this.technique.execute(this.moduleId, this.taskStartupCommand);
        FaultToleranceService.status = STOPPED;
    }
}
