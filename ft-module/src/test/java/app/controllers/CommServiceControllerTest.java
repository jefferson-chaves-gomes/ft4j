package app.controllers;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.FaultToleranceModule;
import app.models.CloudInstance;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.ZooInstance;
import app.services.BootstrapService;

public class CommServiceControllerTest {

    private final static String ZOO_IP = "0.0.0.0";
    private final static int ZOO_PORT = 2181;
    private final static String STARTUP_COMMAND = "java -jar ../ft-coordinator/target/ft-coordinator-0.0.1.jar";
    private final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT);
    private final Level ftLevel = new Level(STARTUP_COMMAND, this.zooInstance);
    private final ArrayList<CloudInstance> lstEmptyReplicas = new ArrayList<>();
    private final FaultToleranceModule ftModule = BootstrapService.getInstance();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CommServiceControllerTest() {
        super();
        this.lstEmptyReplicas.add(new CloudInstance(ZOO_IP, null));
        this.ftLevel.addTechnic(new SoftwareRejuvenation());
        this.ftLevel.addTechnic(new Retry());
        this.ftLevel.addTechnic(new TaskResubmission());
        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));
    }

    @Test
    public void testStartFtCoordinator() throws Exception {

        this.ftModule.start(this.ftLevel);
        TimeUnit.SECONDS.sleep(20);
        this.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);
        Assert.assertTrue(this.ftModule.isTerminated());
    }
}
