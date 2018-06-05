package app;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.commons.enums.SystemEnums.FaultToletancePolicy;
import app.commons.exceptions.ArgumentsInitializeException;
import app.commons.exceptions.DuplicateInitializeException;
import app.commons.exceptions.ReplicationInitializeException;
import app.commons.exceptions.SystemException;
import app.commons.utils.StreamUtil;
import app.models.AttemptsNumber;
import app.models.CloudInstance;
import app.models.Credentials;
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
    private final static String STARTUP_COMMAND = "java -jar ../ft-coordinator/target/ft-coordinator-0.0.1.jar --server.port=7777";
    private final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT, new Credentials("userZoo", "passZoo01"));
    private final Level ftLevel = new Level(STARTUP_COMMAND, this.zooInstance);
    private final ArrayList<CloudInstance> lstEmptyReplicas = new ArrayList<>();
    private final FaultToleranceModule ftModule = BootstrapService.getInstance();

    @Test(expected = ReplicationInitializeException.class)
    public void testValidateReplicationInitialize() throws InterruptedException, SystemException {
        this.ftLevel.addTechnique(new Replication(this.lstEmptyReplicas));
        Assert.assertTrue(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize01() throws InterruptedException, SystemException {
        this.ftLevel.addTechnique(new SoftwareRejuvenation());
        this.ftLevel.addTechnique(new SoftwareRejuvenation());
        Assert.assertFalse(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertTrue(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize02() throws InterruptedException, SystemException {
        this.ftLevel.addTechnique(new Retry());
        this.ftLevel.addTechnique(new Retry());
        Assert.assertTrue(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize03() throws InterruptedException, SystemException {
        this.ftLevel.addTechnique(new TaskResubmission());
        this.ftLevel.addTechnique(new TaskResubmission());
        Assert.assertTrue(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize04() throws InterruptedException, SystemException {
        this.ftLevel.addTechnique(new Replication(this.lstEmptyReplicas));
        this.ftLevel.addTechnique(new Replication(this.lstEmptyReplicas));
        Assert.assertTrue(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = ArgumentsInitializeException.class)
    public void testArgsInitializeException() throws InterruptedException, SystemException {

        final AttemptsNumber attemptsNumber = new AttemptsNumber(0, TimeUnit.SECONDS);
        final DelayBetweenAttempts delayBetweenAttempts = new DelayBetweenAttempts(0, TimeUnit.SECONDS);
        final Timeout timeout = new Timeout(-1, TimeUnit.SECONDS);

        this.ftLevel.addTechnique(new Retry(attemptsNumber, delayBetweenAttempts, timeout));
        this.ftLevel.addTechnique(new TaskResubmission(attemptsNumber, delayBetweenAttempts, timeout));
        this.ftLevel.addTechnique(new SoftwareRejuvenation(attemptsNumber, delayBetweenAttempts, timeout, 0, 0));
        Assert.assertTrue(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertTrue(StreamUtil.hasFaultToleranceType(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }
}
