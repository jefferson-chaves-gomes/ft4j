package app;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.commons.exceptions.DuplicateInitializeException;
import app.commons.exceptions.ReplicationInitializeException;
import app.commons.exceptions.SystemException;
import app.models.CloudInstance;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.ZooInstance;
import app.services.BootstrapService;

public class FaultToleranceMonitorTest {

    private final static int ZOO_PORT = 2181;
    private final static String ZOO_IP = "0.0.0.0";
    private final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT);
    private final Level ftLevel = new Level(this.zooInstance);
    private final ArrayList<CloudInstance> lstEmptyReplicas = new ArrayList<>();

    @Test(expected = ReplicationInitializeException.class)
    public void testValidateReplicationInitialize() throws InterruptedException, SystemException {

        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));
        final FaultToleranceModule ftModule = BootstrapService.getInstance();
        ftModule.init(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize01() throws InterruptedException, SystemException {
        final FaultToleranceModule ftModule = BootstrapService.getInstance();
        this.ftLevel.addTechnic(new SoftwareRejuvenation());
        this.ftLevel.addTechnic(new SoftwareRejuvenation());
        ftModule.init(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize02() throws InterruptedException, SystemException {
        final FaultToleranceModule ftModule = BootstrapService.getInstance();
        this.ftLevel.addTechnic(new Retry());
        this.ftLevel.addTechnic(new Retry());
        ftModule.init(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize03() throws InterruptedException, SystemException {
        final FaultToleranceModule ftModule = BootstrapService.getInstance();
        this.ftLevel.addTechnic(new TaskResubmission());
        this.ftLevel.addTechnic(new TaskResubmission());
        ftModule.init(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize04() throws InterruptedException, SystemException {
        final FaultToleranceModule ftModule = BootstrapService.getInstance();
        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));
        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));
        ftModule.init(this.ftLevel);
    }

    @Test
    public void testModuleShutdown() throws InterruptedException, SystemException {

        this.lstEmptyReplicas.add(new CloudInstance(ZOO_IP, null));
        this.ftLevel.addTechnic(new SoftwareRejuvenation());
        this.ftLevel.addTechnic(new Retry());
        this.ftLevel.addTechnic(new TaskResubmission());
        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));

        final FaultToleranceModule ftModule = BootstrapService.getInstance();
        ftModule.init(this.ftLevel);
        ftModule.start();
        ftModule.stop();

        TimeUnit.SECONDS.sleep(1);
        Assert.assertTrue(ftModule.isTerminated());
    }
}
