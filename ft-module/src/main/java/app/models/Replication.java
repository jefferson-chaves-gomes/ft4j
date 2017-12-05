package app.models;

import java.util.ArrayList;
import java.util.List;

public class Replication extends Technic {

    private List<CloudInstance> lstReplicas;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Replication(final List<CloudInstance> lstReplicas) {
        super();
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

    public List<CloudInstance> getLstReplicas() {
        return this.lstReplicas;
    }

    public void setLstReplicas(final List<CloudInstance> lstReplicas) {
        this.lstReplicas = lstReplicas;
    }
}
