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

import static app.commons.enums.SystemEnums.FaultToletancePolicy.REACTVE;

import java.util.ArrayList;
import java.util.List;

public class Replication extends Technique {

    protected List<CloudInstance> lstReplicas;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Replication() {
        super();
    }

    public Replication(final List<CloudInstance> lstReplicas) {
        super();
        this.lstReplicas = lstReplicas;
    }

    public Replication(final List<CloudInstance> lstReplicas, final AttemptsNumber attemptsNumber, final DelayBetweenAttempts delayBetweenAttempts, final Timeout timeout) {
        super(attemptsNumber, delayBetweenAttempts, timeout, REACTVE);
        this.lstReplicas = lstReplicas;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.models.Technique#execute(java.lang.String, java.lang.String)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void execute(final String moduleId, final String taskStartupCommand) {
        // TODO
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public List<CloudInstance> getLstReplicas() {
        return this.lstReplicas;
    }

    public void setLstReplicas(final List<CloudInstance> lstReplicas) {
        this.lstReplicas = lstReplicas;
    }

    public boolean addReplica(final CloudInstance replica) {
        if (this.getLstReplicas() == null) {
            this.setLstReplicas(new ArrayList<CloudInstance>());
        }
        return this.getLstReplicas().add(replica);
    }

    public boolean removeReplica(final CloudInstance replica) {
        if (this.getLstReplicas() != null) {
            return this.getLstReplicas().remove(replica);
        }
        return false;
    }
}
