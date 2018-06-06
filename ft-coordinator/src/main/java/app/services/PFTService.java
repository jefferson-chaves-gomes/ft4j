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

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;

import app.commons.utils.LoggerUtil;
import app.commons.utils.ResourceMonitorUtil;
import app.models.Level;
import app.models.SoftwareRejuvenation;

public class PFTService extends FaultToleranceService implements Runnable {

    private static final String SERVICE_NAME_VIOLATION_OF_RULE = SERVICE_NAME + "- rule violation: [cpuUsage, memUsage] > [maxCpuUsage, maxMemoryUsage], usage[{%f}, {%f}] > maxUsage[{%f}, {%f}]";
    private static final String SERVICE_NAME_VALIDATION_OF_RULE = SERVICE_NAME + "- rule validation: [cpuUsage, memUsage] < [maxCpuUsage, maxMemoryUsage], usage[{%f}, {%f}] < maxUsage[{%f}, {%f}]";
    private static final String SOFTWARE_REJUVENATION_WAITING_TIMEOUT = "Software Rejuvenation: waiting %s minutes for timeout";
    private static final String RUNNING_SERVICE = "Running " + PFTService.class.getName();
    private static final String SERVICE_STATUS = "FaultToleranceService is " + FaultToleranceService.status;
    private final SoftwareRejuvenation softwareRejuvenation;
    private final Level level;
    private Instant start;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public PFTService(final Level ftLevel) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super();
        this.level = ftLevel;
        this.softwareRejuvenation = this.level.getLstTechniques().stream()
                .filter(SoftwareRejuvenation.class::isInstance)
                .map(SoftwareRejuvenation.class::cast)
                .findFirst()
                .get();
    }

    public void startService() {
        FaultToleranceService.proactiveService = FaultToleranceService.scheduledExecutors.scheduleAtFixedRate(this, DEFAULT_INITIAL_DELAY, DEFAULT_EXECTUTION_TIME, DEFAULT_TIME_UNIT);
        FaultToleranceService.status = STARTED;
        this.start = Instant.now();
        LoggerUtil.info(SERVICE_NAME_STARTED + ": " + this.getClass().getName());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see java.lang.Runnable#run()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void run() {

        LoggerUtil.info(RUNNING_SERVICE);
        if (STARTED != FaultToleranceService.status) {
            LoggerUtil.info(SERVICE_STATUS);
            return;
        }
        try {
            boolean faultDetected = false;
            final Float cpuUsage = ResourceMonitorUtil.getCpuUsage();
            final Float memUsage = ResourceMonitorUtil.getMemUsage();

            final float maxCpuUsage = this.softwareRejuvenation.getMaxCpuUsage();
            final float maxMemoryUsage = this.softwareRejuvenation.getMaxMemoryUsage();

            if (maxCpuUsage < cpuUsage || maxMemoryUsage < memUsage) {
                faultDetected = true;
                LoggerUtil.warn(String.format(SERVICE_NAME_VIOLATION_OF_RULE, cpuUsage, memUsage, maxCpuUsage, maxMemoryUsage));
            } else {
                faultDetected = false;
                LoggerUtil.info(String.format(SERVICE_NAME_VALIDATION_OF_RULE, cpuUsage, memUsage, maxCpuUsage, maxMemoryUsage));
            }

            final int timeout = this.softwareRejuvenation.getTimeout().getValue();
            final long lifetime = Duration.between(this.start, Instant.now()).getSeconds();
            if (faultDetected || lifetime >= timeout) {
                FaultToleranceService.startRecoveryServices(this.level.getModuleId(), this.level.getTaskStartupCommand(), this.softwareRejuvenation);
            } else {
                LoggerUtil.info(String.format(SOFTWARE_REJUVENATION_WAITING_TIMEOUT, (timeout - lifetime) / 60));
            }
        } catch (final Exception e) {
            LoggerUtil.error(e);
        }
    }
}
