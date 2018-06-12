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
import static app.commons.enums.SystemEnums.Priority.NORM_PRIORITY;

import app.commons.enums.SystemEnums.Priority;

public class SoftwareRejuvenation extends Technique {

    private static final int DEFAULT_MAX_USAGE = 95;
    private Timeout timeout;
    private float maxCpuUsage;
    private float maxMemoryUsage;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public SoftwareRejuvenation() {
        this(new Timeout());
    }

    public SoftwareRejuvenation(final Timeout timeout) {
        this(timeout, DEFAULT_MAX_USAGE, DEFAULT_MAX_USAGE);
    }

    public SoftwareRejuvenation(final float maxCpuUsage, final float maxMemoryUsage) {
        this(new Timeout(), maxCpuUsage, maxMemoryUsage);
    }

    public SoftwareRejuvenation(final Timeout timeout, final float maxCpuUsage, final float maxuMemoryUsage) {
        this(timeout, maxCpuUsage, maxuMemoryUsage, NORM_PRIORITY);
    }

    public SoftwareRejuvenation(final Timeout timeout, final float maxCpuUsage, final float maxuMemoryUsage, final Priority priority) {
        super(PROACTIVE, priority);
        this.timeout = timeout;
        this.maxCpuUsage = maxCpuUsage <= 0 ? 100 : maxCpuUsage;
        this.maxMemoryUsage = maxuMemoryUsage <= 0 ? 100 : maxCpuUsage;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.models.Technique#execute(java.lang.String, java.lang.String)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void execute(final String moduleId, final String taskStartupCommand) {
        super.stopStartLocal(moduleId, taskStartupCommand);
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

    public Timeout getTimeout() {
        return this.timeout;
    }

    public void setTimeout(final Timeout timeout) {
        this.timeout = timeout;
    }

}
