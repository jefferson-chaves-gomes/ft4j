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

    private static final String YOU_NEED_PASS_THE_TASK_STARTUP_COMMAND = "You need pass the taskStartupCommand";

    private final String taskStartupCommand;
    private String moduleId;
    private ZooInstance zooInstance;
    private List<Technique> lstTechniques;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Level() {
        super();
        this.taskStartupCommand = YOU_NEED_PASS_THE_TASK_STARTUP_COMMAND;
    }

    public Level(final String taskStartupCommand) {
        super();
        this.taskStartupCommand = taskStartupCommand;
    }

    public Level(final String taskStartupCommand, final ZooInstance zooInstance) {
        this(taskStartupCommand);
        this.zooInstance = zooInstance;
    }

    public Level(final String taskStartupCommand, final List<Technique> lstTechnics, final ZooInstance zooInstance) {
        this(taskStartupCommand);
        this.zooInstance = zooInstance;
        this.lstTechniques = lstTechnics;
    }

    public boolean addTechnique(final Technique technique) {
        if (this.getLstTechniques() == null) {
            this.lstTechniques = new ArrayList<>();
        }
        return this.getLstTechniques().add(technique);
    }

    public boolean removeTechnique(final Technique technique) {
        if (this.getLstTechniques() != null) {
            return this.getLstTechniques().remove(technique);
        }
        return false;
    }

    public List<Technique> getLstTechniques() {
        return this.lstTechniques;
    }

    public ZooInstance getZooInstance() {
        return this.zooInstance;
    }

    public void setZooInstance(final ZooInstance zooInstance) {
        this.zooInstance = zooInstance;
    }

    public String getTaskStartupCommand() {
        return this.taskStartupCommand;
    }

    public String getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(final String moduleId) {
        this.moduleId = moduleId;
    }

    public void setLstTechniques(final List<Technique> lstTechniques) {
        this.lstTechniques = lstTechniques;
    }
}
