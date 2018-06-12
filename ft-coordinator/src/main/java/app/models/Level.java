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

import java.util.ArrayList;
import java.util.List;

import app.models.base.BaseModel;

public class Level extends BaseModel {

    private String moduleId;
    private String taskStartupCommand;
    private Long heartbeatTimeInSeconds;
    private ZooInstance zooInstance;
    private List<Technique> lstTechniques;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Level() {
        this(null);
    }

    public Level(final String taskStartupCommand) {
        this(taskStartupCommand, null);
    }

    public Level(final String taskStartupCommand, final Long heartbeatTimeInSeconds) {
        this(taskStartupCommand, null, heartbeatTimeInSeconds, null);
    }

    public Level(final String taskStartupCommand, final String moduleId, final Long heartbeatTimeInSeconds) {
        this(taskStartupCommand, moduleId, heartbeatTimeInSeconds, null);
    }

    public Level(final String taskStartupCommand, final String moduleId, final Long heartbeatTimeInSeconds, final ZooInstance zooInstance) {
        super();
        this.taskStartupCommand = taskStartupCommand;
        this.heartbeatTimeInSeconds = heartbeatTimeInSeconds;
        this.moduleId = moduleId;
        this.zooInstance = zooInstance;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public boolean addTechnique(final Technique technique) {
        if (this.lstTechniques == null) {
            this.lstTechniques = new ArrayList<>();
        }
        return this.lstTechniques.add(technique);
    }

    public boolean removeTechnique(final Technique technique) {
        if (this.lstTechniques != null) {
            return this.lstTechniques.remove(technique);
        }
        return false;
    }

    public String getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(final String moduleId) {
        this.moduleId = moduleId;
    }

    public String getTaskStartupCommand() {
        return this.taskStartupCommand;
    }

    public void setTaskStartupCommand(final String taskStartupCommand) {
        this.taskStartupCommand = taskStartupCommand;
    }

    public Long getHeartbeatTimeInSeconds() {
        return this.heartbeatTimeInSeconds;
    }

    public void setHeartbeatTimeInSeconds(final Long heartbeatTimeInSeconds) {
        this.heartbeatTimeInSeconds = heartbeatTimeInSeconds;
    }

    public ZooInstance getZooInstance() {
        return this.zooInstance;
    }

    public void setZooInstance(final ZooInstance zooInstance) {
        this.zooInstance = zooInstance;
    }

    public List<Technique> getLstTechniques() {
        return this.lstTechniques;
    }

    public void setLstTechniques(final List<Technique> lstTechniques) {
        this.lstTechniques = lstTechniques;
    }
}
