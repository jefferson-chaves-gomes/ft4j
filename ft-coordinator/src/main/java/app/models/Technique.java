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

import static app.commons.enums.SystemEnums.FaultToletancePolicy.REACTVE;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import app.commons.enums.SystemEnums.FaultToletancePolicy;
import app.models.base.BaseModel;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = SoftwareRejuvenation.class),
        @Type(value = Retry.class),
        @Type(value = TaskResubmission.class),
        @Type(value = Replication.class),
})
public abstract class Technique extends BaseModel {

    private AttemptsNumber attemptsNumber;
    private DelayBetweenAttempts delayBetweenAttempts;
    private Timeout timeout;
    private FaultToletancePolicy ftType;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Technique() {
        this(new AttemptsNumber(), new DelayBetweenAttempts(), new Timeout(), REACTVE);
    }

    public Technique(final AttemptsNumber attemptsNumber, final DelayBetweenAttempts delayBetweenAttempts, final Timeout timeout, final FaultToletancePolicy ftType) {
        super();
        this.attemptsNumber = attemptsNumber;
        this.delayBetweenAttempts = delayBetweenAttempts;
        this.timeout = timeout;
        this.ftType = ftType;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // abstract methods.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    abstract public void execute(final String moduleId, final String taskStartupCommand);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public AttemptsNumber getAttemptsNumber() {
        return this.attemptsNumber;
    }

    public void setAttemptsNumber(final AttemptsNumber attemptsNumber) {
        this.attemptsNumber = attemptsNumber;
    }

    public DelayBetweenAttempts getDelayBetweenAttempts() {
        return this.delayBetweenAttempts;
    }

    public void setDelayBetweenAttempts(final DelayBetweenAttempts delayBetweenAttempts) {
        this.delayBetweenAttempts = delayBetweenAttempts;
    }

    public Timeout getTimeout() {
        return this.timeout;
    }

    public void setTimeout(final Timeout timeout) {
        this.timeout = timeout;
    }

    public FaultToletancePolicy getFtType() {
        return this.ftType;
    }

    public void setFtType(final FaultToletancePolicy ftType) {
        this.ftType = ftType;
    }
}
