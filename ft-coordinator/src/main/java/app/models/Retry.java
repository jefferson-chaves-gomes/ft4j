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
package app.models;

import static app.commons.enums.SystemEnums.Priority.NORM_PRIORITY;

import app.commons.enums.SystemEnums.Priority;

public class Retry extends HeartbeatStrategy {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Retry() {
        super();
    }

    public Retry(final Timeout timeout) {
        this(timeout, new AttemptsNumber());
    }

    public Retry(final Timeout timeout, final AttemptsNumber attemptsNumber) {
        this(timeout, attemptsNumber, NORM_PRIORITY);
    }

    public Retry(final Timeout timeout, final AttemptsNumber attemptsNumber, final Priority priority) {
        super(timeout, attemptsNumber, priority);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.models.Technique#execute(java.lang.String, java.lang.String)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void execute(final String moduleId, final String taskStartupCommand) {
        super.stopStartLocal(moduleId, taskStartupCommand);
    }
}