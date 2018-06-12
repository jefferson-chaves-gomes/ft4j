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

import static app.commons.enums.SystemEnums.Priority.NORM_PRIORITY;

import java.util.ArrayList;
import java.util.List;

import app.commons.enums.SystemEnums.Priority;

public class TaskResubmission extends HeartbeatStrategy {

    protected List<CloudInstance> lstInstances;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public TaskResubmission() {
        this(new ArrayList<>());
    }

    public TaskResubmission(final List<CloudInstance> lstInstances) {
        this(new Timeout(), new AttemptsNumber(), lstInstances);
    }

    public TaskResubmission(final Timeout timeout) {
        this(timeout, new AttemptsNumber(), new ArrayList<>());
    }

    public TaskResubmission(final Timeout timeout, final AttemptsNumber attemptsNumber) {
        this(timeout, attemptsNumber, new ArrayList<>());
    }

    public TaskResubmission(final Timeout timeout, final AttemptsNumber attemptsNumber, final List<CloudInstance> lstInstances) {
        this(timeout, attemptsNumber, lstInstances, NORM_PRIORITY);
    }

    public TaskResubmission(final Timeout timeout, final AttemptsNumber attemptsNumber, final List<CloudInstance> lstInstances, final Priority priority) {
        super(timeout, attemptsNumber, priority);
        this.lstInstances = lstInstances;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.models.Technique#execute(java.lang.String, java.lang.String)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void execute(final String moduleId, final String taskStartupCommand) {

        super.stopLocal(moduleId);
        if (this.getLstInstances() != null && !this.getLstInstances().isEmpty()) {
            // TODO connect to CLOUD INSTANCE and start the task there
        } else {
            super.startLocal(taskStartupCommand);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public List<CloudInstance> getLstInstances() {
        return this.lstInstances;
    }

    public void setLstInstances(final List<CloudInstance> lstInstances) {
        this.lstInstances = lstInstances;
    }
}
