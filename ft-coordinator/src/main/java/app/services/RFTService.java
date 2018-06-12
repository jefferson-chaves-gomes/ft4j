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

import static app.commons.constants.TimeConstants.DEFAULT_INITIAL_DELAY;
import static app.commons.constants.TimeConstants.DEFAULT_TIME_UNIT;
import static app.commons.enums.SystemEnums.ExecutionStatus.STARTED;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import app.commons.utils.LoggerUtil;
import app.models.HeartbeatStrategy;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.TaskResubmission;

public class RFTService extends FaultToleranceService implements Runnable {

    private static final String SERVICE_NAME_VIOLATION_OF_RULE = SERVICE_NAME + "- heartbeat fault detected [elepsedTime > latencyMilles + timeoutMilles + heartbeatTime] > [%d > %d + %d + %d]";
    private final Retry retry;
    private final TaskResubmission taskResubmission;
    private final Replication replication;
    private final Level level;
    protected static long attemptsNumber = 0;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public RFTService(final Level ftLevel) {
        super();
        this.level = ftLevel;
        this.retry = this.level.getLstTechniques().stream()
                .filter(Retry.class::isInstance)
                .map(Retry.class::cast)
                .findFirst()
                .orElse(null);
        this.taskResubmission = this.level.getLstTechniques().stream()
                .filter(TaskResubmission.class::isInstance)
                .map(TaskResubmission.class::cast)
                .findFirst()
                .orElse(null);
        this.replication = this.level.getLstTechniques().stream()
                .filter(Replication.class::isInstance)
                .map(Replication.class::cast)
                .findFirst()
                .orElse(null);
    }

    public void startService() {

        if (this.replication != null
                && this.replication.getLstReplicas() != null
                && !this.replication.getLstReplicas().isEmpty()) {

            // TODO start the replication technique
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        }

        if (this.level.getHeartbeatTimeInSeconds() != null) {
            FaultToleranceService.reactiveService = FaultToleranceService.scheduledExecutors
                    .scheduleAtFixedRate(this, DEFAULT_INITIAL_DELAY, this.level.getHeartbeatTimeInSeconds(), DEFAULT_TIME_UNIT);
            FaultToleranceService.status = STARTED;
        }
        LoggerUtil.info(SERVICE_NAME_STARTED + ": " + this.getClass().getName());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see java.lang.Runnable#run()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void run() {

        LoggerUtil.info(RUNNING_SERVICE + this.getClass().getName());
        if (STARTED != FaultToleranceService.status) {
            LoggerUtil.info(SERVICE_STATUS);
            return;
        }
        try {
            // TODO dissertation - HEARTBEAT
            // 2018-survey_hasan.pdf
            // 2015-survey_amin.pdf && the new
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            final boolean retryFault = this.hasHeartbeatFault(this.retry);
            final boolean taskResubmissionFault = this.hasHeartbeatFault(this.taskResubmission);
            if (retryFault || taskResubmissionFault) {
                HeartbeatStrategy technique = this.retry;
                if (technique == null || taskResubmissionFault && this.taskResubmission.getPriority().ordinal() < this.retry.getPriority().ordinal()) {
                    technique = this.taskResubmission;
                }
                if (++attemptsNumber >= technique.getAttemptsNumber().getValue()) {
                    FaultToleranceService.startRecoveryServices(
                            this.level.getModuleId(),
                            this.level.getTaskStartupCommand(),
                            technique);
                }
            }
        } catch (final Exception e) {
            LoggerUtil.error(e);
        }
    }

    private boolean hasHeartbeatFault(final HeartbeatStrategy technique) {

        if (technique == null) {
            return false;
        }
        final long elapsedTime = Duration.between(lastCommunication, Instant.now()).toMillis();

        final TimeUnit timeoutUnit = technique.getTimeout().getUnit();
        final Long timeoutValue = technique.getTimeout().getValue();
        final long timeoutMilles = TimeUnit.MILLISECONDS.convert(timeoutValue, timeoutUnit);
        final long heartbeatMilles = TimeUnit.MILLISECONDS.convert(this.level.getHeartbeatTimeInSeconds(), DEFAULT_TIME_UNIT);
        final long maxWaitTime = latencyMilles + timeoutMilles + heartbeatMilles;
        if (elapsedTime > maxWaitTime) {
            LoggerUtil.warn(String.format(SERVICE_NAME_VIOLATION_OF_RULE, elapsedTime, latencyMilles, timeoutMilles, heartbeatMilles));
            return true;
        }
        return false;
    }
}
