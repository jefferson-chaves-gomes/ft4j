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
package app.models;

import static app.commons.enums.SystemEnums.FaultToletancePolicy.PROACTIVE;

import app.commons.utils.LoggerUtil;

public class SoftwareRejuvenation extends Technique {

    private float maxCpuUsage = 0;
    private float maxMemoryUsage = 0;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public SoftwareRejuvenation() {
        super(new AttemptsNumber(), new DelayBetweenAttempts(), new Timeout(), PROACTIVE);
        this.maxCpuUsage = 95;
        this.maxMemoryUsage = 95;
    }

    public SoftwareRejuvenation(final float maxCpuUsage, final float maxuMemoryUsage) {
        this();
        this.maxCpuUsage = maxCpuUsage <= 0 ? 100 : maxCpuUsage;
        this.maxMemoryUsage = maxuMemoryUsage <= 0 ? 100 : maxCpuUsage;
    }

    public SoftwareRejuvenation(final AttemptsNumber attemptsNumber, final DelayBetweenAttempts delayBetweenAttempts, final Timeout timeout, final float maxCpuUsage, final float maxuMemoryUsage) {
        super(attemptsNumber, delayBetweenAttempts, timeout, PROACTIVE);
        this.maxCpuUsage = maxCpuUsage;
        this.maxMemoryUsage = maxuMemoryUsage;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.models.Technique#execute(java.lang.String, java.lang.String)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void execute(final String moduleId, final String taskStartupCommand) {

        LoggerUtil.info("Killing the PID: " + moduleId);
        // TODO kill -9 moduleId
        LoggerUtil.info("Starting the Partner with command: " + taskStartupCommand);
        // TODO java -jar
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public float getMaxCpuUsage() {
        return this.maxCpuUsage;
    }

    public void setMaxCpuUsage(final float maxCpuUsage) {
        this.maxCpuUsage = maxCpuUsage;
    }

    public float getMaxMemoryUsage() {
        return this.maxMemoryUsage;
    }

    public void setMaxMemoryUsage(final float maxMemoryUsage) {
        this.maxMemoryUsage = maxMemoryUsage;
    }

}
