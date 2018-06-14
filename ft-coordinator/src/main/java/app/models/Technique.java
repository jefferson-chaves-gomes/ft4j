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

import static app.commons.constants.MessageConstants.KILLING_THE_PARTNER_PID;
import static app.commons.constants.MessageConstants.STARTING_THE_PARTNER_FROM_PATH;
import static app.commons.constants.MessageConstants.STARTING_THE_PARTNER_WITH_COMMAND;
import static app.commons.constants.MessageConstants.THE_PARTNER_WAS_KILLED;
import static app.commons.constants.MessageConstants.THE_PARTNER_WAS_STARTED_AGAIN;
import static app.commons.enums.SystemEnums.FaultToletancePolicy.REACTVE;
import static app.commons.enums.SystemEnums.Priority.NORM_PRIORITY;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import app.commons.enums.SystemEnums.FaultToletancePolicy;
import app.commons.enums.SystemEnums.Priority;
import app.commons.utils.LoggerUtil;
import app.commons.utils.ResourceMonitorUtil;
import app.models.base.BaseModel;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = SoftwareRejuvenation.class),
        @Type(value = Retry.class),
        @Type(value = TaskResubmission.class),
        @Type(value = Replication.class),
})
public abstract class Technique extends BaseModel {

    private FaultToletancePolicy ftType;
    private Priority priority;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Technique() {
        this(REACTVE, NORM_PRIORITY);
    }

    public Technique(final FaultToletancePolicy ftType) {
        this(ftType, NORM_PRIORITY);
    }

    public Technique(final FaultToletancePolicy ftType, final Priority priority) {
        super();
        this.ftType = ftType;
        this.priority = priority;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // abstract methods.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    abstract public void execute(final String moduleId, final String taskStartupCommand);

    protected Integer getModulePID(final String moduleId) {

        final int startIndex = 0;
        final String moduleIdDelimiter = "@";
        final String substring = moduleId.substring(startIndex, moduleId.indexOf(moduleIdDelimiter));
        return Integer.parseInt(substring);
    }

    protected void stopStartLocal(final String moduleId, final String taskStartupCommand) {

        this.stopLocal(moduleId);
        this.startLocal(taskStartupCommand);
    }

    protected void stopLocal(final String moduleId) {

        try {
            final Integer modulePID = this.getModulePID(moduleId);
            LoggerUtil.info(String.format(KILLING_THE_PARTNER_PID, modulePID));
            if (ResourceMonitorUtil.kill(modulePID)) {
                LoggerUtil.info(THE_PARTNER_WAS_KILLED);
            }
        } catch (final Exception e) {
            LoggerUtil.error(e);
        }
    }

    protected void startLocal(final String taskStartupCommand) {

        try {
            final String currentPath = System.getProperty("user.dir");
            LoggerUtil.info(String.format(STARTING_THE_PARTNER_FROM_PATH, currentPath));
            LoggerUtil.info(String.format(STARTING_THE_PARTNER_WITH_COMMAND, taskStartupCommand));
            CompletableFuture.runAsync(() -> {
                try {
                    if (ResourceMonitorUtil.start(taskStartupCommand)) {
                        LoggerUtil.info(THE_PARTNER_WAS_STARTED_AGAIN);
                    }
                } catch (IOException | InterruptedException e) {
                    LoggerUtil.error(e);
                }
            });
        } catch (final Exception e) {
            LoggerUtil.error(e);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public FaultToletancePolicy getFtType() {
        return this.ftType;
    }

    public void setFtType(final FaultToletancePolicy ftType) {
        this.ftType = ftType;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }
}
