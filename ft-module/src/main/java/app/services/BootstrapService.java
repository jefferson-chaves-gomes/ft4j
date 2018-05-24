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
package app.services;

import static app.commons.constants.MessageConstants.ATTEMPT_TO_SHUTDOWN_COORDINATOR;
import static app.commons.constants.MessageConstants.ATTEMPT_TO_SHUTDOWN_EXECUTOR;
import static app.commons.constants.MessageConstants.CANCEL_NON_FINISHED_TASKS;
import static app.commons.constants.MessageConstants.ERROR_REGISTER_COORDINATOR;
import static app.commons.constants.MessageConstants.ERROR_TASKS_INTERRUPTED;
import static app.commons.constants.MessageConstants.FT_MODULE_INITIALIZED_SUCCESSFULLY;
import static app.commons.constants.MessageConstants.SHUTDOWN_FINISHED;
import static app.commons.constants.MessageConstants.START_FT_COORDINATOR_CALLED;
import static app.commons.constants.MessageConstants.TRYING_TO_CONTACT_MODULE_AT_COORDINATOR;
import static app.commons.constants.MessageConstants.TRYING_TO_REGISTER_MODULE_AT_COORDINATOR;
import static app.commons.constants.MessageConstants.WAITING_START_FT_COORDINATOR_START;
import static app.commons.enums.SystemEnums.ExecutionStatus.STARTED;
import static app.models.AttemptsNumber.DEFAULT_VALUE;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import app.FaultToleranceModule;
import app.commons.exceptions.ArgumentsInitializeException;
import app.commons.exceptions.DuplicateInitializeException;
import app.commons.exceptions.ReplicationInitializeException;
import app.commons.exceptions.SystemException;
import app.commons.utils.LoggerUtil;
import app.commons.utils.RuntimeUtil;
import app.commons.utils.RuntimeUtil.Command;
import app.commons.utils.StreamUtil;
import app.conf.Routes;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.Technic;

public class BootstrapService implements FaultToleranceModule {

    private static BootstrapService bootstrap;
    private static ExecutorService executor;
    private static CommServiceThread commService;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private BootstrapService() {
        super();
    }

    public static BootstrapService getInstance() {
        if (bootstrap == null) {
            bootstrap = new BootstrapService();
            executor = Executors.newSingleThreadExecutor();
        }
        return bootstrap;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#start()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void start(final Level ftLevel) throws SystemException, InterruptedException {
        this.validate(ftLevel);
        this.startFtCoordinator(ftLevel);
        commService = new CommServiceThread(ftLevel);
        executor.submit(commService);
        this.waitForCommunication(commService);
        if (STARTED != commService.getStatus() || !commService.isRegistered()) {
            this.stop();
            throw new SystemException(ERROR_REGISTER_COORDINATOR);
        }
        LoggerUtil.info(FT_MODULE_INITIALIZED_SUCCESSFULLY);
    }

    private void waitForCommunication(final CommServiceThread commService) throws InterruptedException {
        int attemptsNumber = 0;
        while (STARTED != commService.getStatus() && attemptsNumber++ < DEFAULT_VALUE) {
            LoggerUtil.info(TRYING_TO_CONTACT_MODULE_AT_COORDINATOR);
            TimeUnit.SECONDS.sleep(DEFAULT_VALUE);
        }
        attemptsNumber = 0;
        while (!commService.isRegistered() && attemptsNumber++ < DEFAULT_VALUE) {
            LoggerUtil.info(TRYING_TO_REGISTER_MODULE_AT_COORDINATOR);
            TimeUnit.SECONDS.sleep(DEFAULT_VALUE);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#stop()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void stop() {
        try {
            LoggerUtil.info(ATTEMPT_TO_SHUTDOWN_COORDINATOR);
            commService.callRequest(Routes.SHUTDOWN);
            LoggerUtil.info(ATTEMPT_TO_SHUTDOWN_EXECUTOR);
            BootstrapService.executor.shutdown();
            BootstrapService.executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            LoggerUtil.error(ERROR_TASKS_INTERRUPTED, e);
        } finally {
            if (!BootstrapService.executor.isTerminated()) {
                LoggerUtil.info(CANCEL_NON_FINISHED_TASKS);
            }
            BootstrapService.executor.shutdownNow();
            LoggerUtil.info(SHUTDOWN_FINISHED);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#isTerminated()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean isTerminated() {
        return BootstrapService.executor.isShutdown() && BootstrapService.executor.isTerminated();
    }

    private void validate(final Level ftLevel) throws SystemException {
        final List<Technic> lstTechnics = ftLevel.getLstTechnics();
        this.validateDuplications(lstTechnics);
        this.validateArgsInitialization(lstTechnics);
    }

    private void validateArgsInitialization(final List<Technic> lstTechnics) throws ArgumentsInitializeException, ReplicationInitializeException {
        for (final Technic element : lstTechnics) {
            if (element instanceof TaskResubmission) {
                final TaskResubmission technic = (TaskResubmission) element;
                if (technic.getAttemptsNumber().getValue() < 0
                        || technic.getDelayBetweenAttempts().getValue() < 0
                        || technic.getTimeout().getValue() < 0) {
                    throw new ArgumentsInitializeException();
                }
                continue;
            }
            if (element instanceof Replication) {
                final Replication technic = (Replication) element;
                if (technic.getLstReplicas() == null || technic.getLstReplicas().isEmpty()) {
                    throw new ReplicationInitializeException();
                }
                continue;
            }
        }
    }

    private void validateDuplications(final List<Technic> lstTechnics) throws DuplicateInitializeException {
        if (StreamUtil.hasDuplicates(lstTechnics, SoftwareRejuvenation.class)
                || StreamUtil.hasDuplicates(lstTechnics, Retry.class)
                || StreamUtil.hasDuplicates(lstTechnics, TaskResubmission.class)
                || StreamUtil.hasDuplicates(lstTechnics, Replication.class)) {
            throw new DuplicateInitializeException();
        }
    }

    private void startFtCoordinator(final Level ftLevel) throws SystemException, InterruptedException {
        CompletableFuture.runAsync(() -> {
            try {
                LoggerUtil.info(START_FT_COORDINATOR_CALLED + ftLevel.getTaskStartupCommand());
                RuntimeUtil.execAndGetResponseString(new Command(ftLevel.getTaskStartupCommand()));
                TimeUnit.SECONDS.sleep(DEFAULT_VALUE);
            } catch (IOException | InterruptedException e) {
                LoggerUtil.error(e);
            }
        });
        LoggerUtil.info(WAITING_START_FT_COORDINATOR_START);
        TimeUnit.SECONDS.sleep(DEFAULT_VALUE * 3);
    }
}
