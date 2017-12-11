package app;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import app.commons.exceptions.ArgumentsInitializeException;
import app.commons.exceptions.DuplicateInitializeException;
import app.commons.exceptions.ReplicationInitializeException;
import app.commons.exceptions.SystemException;
import app.models.AttemptsNumber;
import app.models.CloudInstance;
import app.models.DelayBetweenAttempts;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.Timeout;
import app.models.ZooInstance;
import app.services.BootstrapService;

public class ArgsInitializationTest {

    private final static int ZOO_PORT = 2181;
    private final static String ZOO_IP = "0.0.0.0";
    private final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT);
    private final Level ftLevel = new Level(this.zooInstance);
    private final ArrayList<CloudInstance> lstEmptyReplicas = new ArrayList<>();
    private final FaultToleranceModule ftModule = BootstrapService.getInstance();

    @Test(expected = ReplicationInitializeException.class)
    public void testValidateReplicationInitialize() throws InterruptedException, SystemException {
        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize01() throws InterruptedException, SystemException {
        this.ftLevel.addTechnic(new SoftwareRejuvenation());
        this.ftLevel.addTechnic(new SoftwareRejuvenation());
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize02() throws InterruptedException, SystemException {
        this.ftLevel.addTechnic(new Retry());
        this.ftLevel.addTechnic(new Retry());
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize03() throws InterruptedException, SystemException {
        this.ftLevel.addTechnic(new TaskResubmission());
        this.ftLevel.addTechnic(new TaskResubmission());
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize04() throws InterruptedException, SystemException {
        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));
        this.ftLevel.addTechnic(new Replication(this.lstEmptyReplicas));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = ArgumentsInitializeException.class)
    public void testArgsInitializeException() throws InterruptedException, SystemException {

        final AttemptsNumber attemptsNumber = new AttemptsNumber(0, TimeUnit.SECONDS);
        final DelayBetweenAttempts delayBetweenAttempts = new DelayBetweenAttempts(0, TimeUnit.SECONDS);
        final Timeout timeout = new Timeout(-1, TimeUnit.SECONDS);

        this.ftLevel.addTechnic(new Retry(attemptsNumber, delayBetweenAttempts, timeout));
        this.ftLevel.addTechnic(new TaskResubmission(attemptsNumber, delayBetweenAttempts, timeout));
        this.ftLevel.addTechnic(new SoftwareRejuvenation(attemptsNumber, delayBetweenAttempts, timeout));

        this.ftModule.start(this.ftLevel);
    }
}
