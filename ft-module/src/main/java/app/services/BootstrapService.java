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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import app.FaultToleranceModule;
import app.commons.exceptions.ArgumentsInitializeException;
import app.commons.exceptions.DuplicateInitializeException;
import app.commons.exceptions.ReplicationInitializeException;
import app.commons.exceptions.SystemException;
import app.commons.utils.LoggerUtil;
import app.commons.utils.StreamUtil;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.Technic;
import app.tasks.CommServiceTask;

public class BootstrapService implements FaultToleranceModule {

    private static BootstrapService service;
    private static ExecutorService executor;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private BootstrapService() {
        super();
    }

    public static BootstrapService getInstance() {
        if (service == null) {
            service = new BootstrapService();
            executor = Executors.newSingleThreadExecutor();
        }
        return service;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#start()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void start(final Level ftLevel) throws SystemException {
        this.validate(ftLevel);
        final CommServiceTask task = new CommServiceTask();
        BootstrapService.executor.submit(task);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.FaultToleranceModule#stop()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void stop() {
        try {
            LoggerUtil.debug("attempt to shutdown executor");
            BootstrapService.executor.shutdown();
            BootstrapService.executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            LoggerUtil.debug("tasks interrupted");
        } finally {
            if (!BootstrapService.executor.isTerminated()) {
                LoggerUtil.debug("cancel non-finished tasks");
            }
            BootstrapService.executor.shutdownNow();
            LoggerUtil.debug("shutdown finished");
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
                if (technic.getAttemptsNumber().getValue() < 0 || technic.getDelayBetweenAttempts().getValue() < 0 || technic.getTimeout().getValue() < 0) {
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
        if (StreamUtil.hasDuplicates(lstTechnics, SoftwareRejuvenation.class) || StreamUtil.hasDuplicates(lstTechnics, Retry.class) || StreamUtil.hasDuplicates(lstTechnics, TaskResubmission.class)
                || StreamUtil.hasDuplicates(lstTechnics, Replication.class)) {
            throw new DuplicateInitializeException();
        }
    }
}
