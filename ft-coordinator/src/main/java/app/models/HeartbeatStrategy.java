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

import static app.commons.constants.TimeConstants.DEFAULT_HEARTBEAT_TIME;
import static app.commons.constants.TimeConstants.DEFAULT_TIME_UNIT;
import static app.commons.enums.SystemEnums.FaultToletancePolicy.REACTVE;
import static app.commons.enums.SystemEnums.Priority.NORM_PRIORITY;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import app.commons.enums.SystemEnums.Priority;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = Retry.class),
        @Type(value = TaskResubmission.class),
})
public abstract class HeartbeatStrategy extends Technique {

    private Timeout timeout;
    private AttemptsNumber attemptsNumber;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public HeartbeatStrategy() {
        this(new Timeout(DEFAULT_HEARTBEAT_TIME, DEFAULT_TIME_UNIT), new AttemptsNumber());
    }

    public HeartbeatStrategy(final Timeout timeout, final AttemptsNumber attemptsNumber) {
        this(timeout, attemptsNumber, NORM_PRIORITY);
    }

    public HeartbeatStrategy(final Timeout timeout, final AttemptsNumber attemptsNumber, final Priority priority) {
        super(REACTVE, priority);
        this.timeout = timeout;
        this.attemptsNumber = attemptsNumber;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public AttemptsNumber getAttemptsNumber() {
        return this.attemptsNumber;
    }

    public void setAttemptsNumber(final AttemptsNumber attemptsNumber) {
        this.attemptsNumber = attemptsNumber;
    }

    public Timeout getTimeout() {
        return this.timeout;
    }

    public void setTimeout(final Timeout timeout) {
        this.timeout = timeout;
    }
}