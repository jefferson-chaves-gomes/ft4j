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

import static app.commons.constants.TimeConstants.DEFAULT_EXECTUTION_TIME;
import static app.commons.constants.TimeConstants.DEFAULT_INITIAL_DELAY;
import static app.commons.constants.TimeConstants.DEFAULT_TIME_UNIT;
import static app.commons.enums.SystemEnums.ExecutionStatus.STARTED;
import static app.commons.enums.SystemEnums.ExecutionStatus.STOPPED;

import app.commons.utils.LoggerUtil;
import app.models.Level;

public class RFTService extends FaultToleranceService {

    protected FDServiceThread faultDetectionService;
    protected RecoveryServiceThread recoveryService;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public RFTService(final Level ftLevel) {
        super(ftLevel);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.services.FaultToleranceService#startServices()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void startServices() {

        if (STOPPED == status) {
            this.faultDetectionService = new FDServiceThread();
            super.executor.scheduleAtFixedRate(this.faultDetectionService, DEFAULT_INITIAL_DELAY, DEFAULT_EXECTUTION_TIME, DEFAULT_TIME_UNIT);

            this.recoveryService = new RecoveryServiceThread();
            super.executor.scheduleAtFixedRate(this.recoveryService, DEFAULT_INITIAL_DELAY, DEFAULT_EXECTUTION_TIME, DEFAULT_TIME_UNIT);
            status = STARTED;
            LoggerUtil.info("Service - Reactive Fault Tolerance STARTED");
        } else {
            LoggerUtil.info("Service - Reactive Fault Tolerance ALREADY STARTED");
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // inner classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    class FDServiceThread implements Runnable {

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // * @see java.lang.Runnable#run()
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        @Override
        public void run() {
            LoggerUtil.info("Service - Running Fault Detection...");
        }

    }

    class RecoveryServiceThread implements Runnable {

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // * @see java.lang.Runnable#run()
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        @Override
        public void run() {
            LoggerUtil.info("Service - Running Recovery...");
        }
    }

}
