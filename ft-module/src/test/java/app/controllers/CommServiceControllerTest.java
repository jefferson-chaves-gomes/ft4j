package app.controllers;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.FaultToleranceModule;
import app.models.CloudInstance;
import app.models.Credentials;
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
    private final static String STARTUP_COMMAND = "java -jar ../ft-module/target/ft-module-0.0.1.jar";
    private final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT, new Credentials("userZoo", "passZoo01"));
    private final Level ftLevel = new Level(STARTUP_COMMAND, this.zooInstance);
    private final ArrayList<CloudInstance> lstFakeNodes = new ArrayList<>();
    private final FaultToleranceModule ftModule = BootstrapService.getInstance();
    private final static String MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CommServiceControllerTest() {
        super();
        this.lstFakeNodes.add(new CloudInstance("10.0.0.1", 21, new Credentials("user01", "pass01")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.2", 22, new Credentials("user02", "pass02")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.3", 23, new Credentials("user03", "pass03")));
        this.ftLevel.setModuleId(MODULE_ID);
        this.ftLevel.addTechnique(new SoftwareRejuvenation(93, 93));
        this.ftLevel.addTechnique(new Retry());
        this.ftLevel.addTechnique(new TaskResubmission());
        this.ftLevel.addTechnique(new Replication(this.lstFakeNodes));
    }

    @Test
    public void testStartFtCoordinator() throws Exception {

        this.ftModule.start(this.ftLevel);
        TimeUnit.SECONDS.sleep(60);
        this.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);
        Assert.assertTrue(this.ftModule.isTerminated());
    }
}
