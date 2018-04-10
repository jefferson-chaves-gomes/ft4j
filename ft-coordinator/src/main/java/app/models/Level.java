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

public class Level {

    private final String taskStartupCommand;
    private ZooInstance zooInstance;
    private List<Technic> lstTechnics;
    private List<CloudInstance> lstInstances;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Level(final String taskStartupCommand) {
        super();
        this.taskStartupCommand = taskStartupCommand;
    }

    public Level(final String taskStartupCommand, final ZooInstance zooInstance) {
        this(taskStartupCommand);
        this.zooInstance = zooInstance;
    }

    public Level(final String taskStartupCommand, final List<Technic> lstTechnics, final List<CloudInstance> lstInstantes, final ZooInstance zooInstance) {
        this(taskStartupCommand);
        this.zooInstance = zooInstance;
        this.lstTechnics = lstTechnics;
        this.lstInstances = lstInstantes;
    }

    public boolean addTechnic(final Technic technic) {
        if (this.getLstTechnics() == null) {
            this.lstTechnics = new ArrayList<>();
        }
        return this.getLstTechnics().add(technic);
    }

    public boolean removeTechnic(final Technic technic) {
        if (this.getLstTechnics() != null) {
            return this.getLstTechnics().remove(technic);
        }
        return false;
    }

    public boolean addInstance(final CloudInstance instance) {
        if (this.getLstInstances() == null) {
            this.lstInstances = new ArrayList<>();
        }
        return this.getLstInstances().add(instance);
    }

    public boolean removeInstance(final CloudInstance instance) {
        if (this.getLstInstances() != null) {
            return this.getLstInstances().remove(instance);
        }
        return false;
    }

    public List<Technic> getLstTechnics() {
        return this.lstTechnics;
    }

    public List<CloudInstance> getLstInstances() {
        return this.lstInstances;
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

}
