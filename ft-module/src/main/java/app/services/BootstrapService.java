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
import static app.commons.constants.MessageConstants.FT_COORDINATOR_START_OUTPUT;
import static app.commons.constants.MessageConstants.FT_MODULE_INITIALIZED_SUCCESSFULLY;
import static app.commons.constants.MessageConstants.SHUTDOWN_FINISHED;
import static app.commons.constants.MessageConstants.START_FT_COORDINATOR_CALLED;
import static app.commons.constants.MessageConstants.TRYING_TO_CONTACT_MODULE_AT_COORDINATOR;
import static app.commons.constants.MessageConstants.TRYING_TO_REGISTER_MODULE_AT_COORDINATOR;
import static app.commons.constants.MessageConstants.WAITING_START_FT_COORDINATOR_START;
import static app.commons.constants.TimeConstants.DEFAULT_TIME_UNIT;
import static app.commons.enums.SystemEnums.ExecutionStatus.STARTED;
import static app.conf.Routes.IMALIVE;
import static app.conf.Routes.SHUTDOWN;
import static app.models.AttemptsNumber.DEFAULT_VALUE;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.FaultToleranceModule;
import app.commons.exceptions.ArgumentsInitializeException;
import app.commons.exceptions.DuplicateInitializeException;
import app.commons.exceptions.ReplicationInitializeException;
import app.commons.exceptions.SystemException;
import app.commons.utils.LoggerUtil;
import app.commons.utils.RuntimeUtil;
import app.commons.utils.RuntimeUtil.Command;
import app.commons.utils.StreamUtil;
import app.commons.utils.StringUtil;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.Technique;

public class BootstrapService implements FaultToleranceModule {

    private static final String SERVER_PORT = "server.port";
    private static final int DEFAULT_COORDINATOR_PORT = 7777;
    private static final int MAX_ATTEMPTS_NUMBER = 5;
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
            executor = Executors.newSingleThreadScheduledExecutor();
        }
        return bootstrap;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#start()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void start(final Level ftLevel, final String startupCoordinatorCommand) throws SystemException, InterruptedException {

        validate(ftLevel);
        commService = new CommServiceThread(ftLevel, getPort(startupCoordinatorCommand));
        if (!checkFtCoordinatorAvailability(ftLevel, startupCoordinatorCommand)) {
            startFtCoordinator(startupCoordinatorCommand);
        }
        executor.submit(commService);
        waitForCommunication();
        if (STARTED != commService.getStatus() || !commService.isRegistered()) {
            this.stop();
            throw new SystemException(ERROR_REGISTER_COORDINATOR);
        }
        LoggerUtil.info(FT_MODULE_INITIALIZED_SUCCESSFULLY);
    }

    private static int getPort(final String startupCoordinatorCommand) {

        if (startupCoordinatorCommand.contains(SERVER_PORT)) {
            final String port = startupCoordinatorCommand.substring(startupCoordinatorCommand.indexOf(SERVER_PORT));
            return Integer.parseInt(port.substring(port.indexOf("=") + 1));
        }
        return DEFAULT_COORDINATOR_PORT;
    }

    private static boolean checkFtCoordinatorAvailability(final Level ftLevel, final String startupCoordinatorCommand) throws SystemException, InterruptedException {

        if (!commService.callRequest(IMALIVE, true)) {
            return false;
        }
        return true;
    }

    private static void waitForCommunication() throws InterruptedException {

        int attemptsNumber = 0;
        while (STARTED != commService.getStatus() && attemptsNumber++ < MAX_ATTEMPTS_NUMBER) {
            LoggerUtil.info(TRYING_TO_CONTACT_MODULE_AT_COORDINATOR);
            DEFAULT_TIME_UNIT.sleep(DEFAULT_VALUE);
        }
        attemptsNumber = 0;
        while (!commService.isRegistered() && attemptsNumber++ < MAX_ATTEMPTS_NUMBER) {
            LoggerUtil.info(TRYING_TO_REGISTER_MODULE_AT_COORDINATOR);
            DEFAULT_TIME_UNIT.sleep(DEFAULT_VALUE);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#stop()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void stop() {

        try {
            LoggerUtil.info(ATTEMPT_TO_SHUTDOWN_COORDINATOR);
            commService.callRequest(SHUTDOWN);
            LoggerUtil.info(ATTEMPT_TO_SHUTDOWN_EXECUTOR);
            executor.shutdown();
            executor.awaitTermination(DEFAULT_VALUE, DEFAULT_TIME_UNIT);
        } catch (final InterruptedException e) {
            LoggerUtil.error(ERROR_TASKS_INTERRUPTED, e);
        } finally {
            if (!executor.isTerminated()) {
                LoggerUtil.info(CANCEL_NON_FINISHED_TASKS);
            }
            executor.shutdownNow();
            LoggerUtil.info(SHUTDOWN_FINISHED);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#isTerminated()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean isTerminated() {
        return executor.isShutdown() && executor.isTerminated();
    }

    private static void validate(final Level ftLevel) throws SystemException {

        final List<Technique> lstTechnics = ftLevel.getLstTechniques();
        validateDuplications(lstTechnics);
        validateArgsInitialization(ftLevel);
    }

    private static void validateArgsInitialization(final Level level) throws ArgumentsInitializeException, ReplicationInitializeException {

        if (level.getLstTechniques() == null || level.getLstTechniques().isEmpty()) {
            throw new ArgumentsInitializeException();
        }
        if (StringUtil.isEmpty(level.getTaskStartupCommand())) {
            throw new ArgumentsInitializeException();
        }

        if (level.getHeartbeatTimeInSeconds() == null || level.getHeartbeatTimeInSeconds() <= 0) {
            if (StreamUtil.hasFaultToleranceTechinique(level.getLstTechniques(), Retry.class)
                    || StreamUtil.hasFaultToleranceTechinique(level.getLstTechniques(), TaskResubmission.class)) {
                throw new ArgumentsInitializeException();
            }
        }

        for (final Object element : level.getLstTechniques()) {
            if (element instanceof Replication) {
                final Replication technic = (Replication) element;
                if (technic.getLstReplicas() == null || technic.getLstReplicas().isEmpty()) {
                    throw new ReplicationInitializeException();
                }
                continue;
            }
        }
    }

    private static void validateDuplications(final List<Technique> lstTechnics) throws DuplicateInitializeException {

        if (StreamUtil.hasDuplicates(lstTechnics, SoftwareRejuvenation.class)
                || StreamUtil.hasDuplicates(lstTechnics, Retry.class)
                || StreamUtil.hasDuplicates(lstTechnics, TaskResubmission.class)
                || StreamUtil.hasDuplicates(lstTechnics, Replication.class)) {
            throw new DuplicateInitializeException();
        }
    }

    private static void startFtCoordinator(final String startuCoordinatorCommand) throws SystemException, InterruptedException {

        CompletableFuture.runAsync(() -> {
            try {
                LoggerUtil.info(START_FT_COORDINATOR_CALLED + startuCoordinatorCommand);
                final String response = RuntimeUtil.execAndGetResponseString(new Command(startuCoordinatorCommand));
                LoggerUtil.info(String.format(FT_COORDINATOR_START_OUTPUT, response));
                DEFAULT_TIME_UNIT.sleep(DEFAULT_VALUE);
            } catch (IOException | InterruptedException e) {
                LoggerUtil.error(e);
            }
        });
        LoggerUtil.info(WAITING_START_FT_COORDINATOR_START);
        DEFAULT_TIME_UNIT.sleep(MAX_ATTEMPTS_NUMBER * 3);
    }
}
