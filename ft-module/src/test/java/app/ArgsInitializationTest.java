package app;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import app.commons.enums.SystemEnums.FaultToletancePolicy;
import app.commons.exceptions.ArgumentsInitializeException;
import app.commons.exceptions.DuplicateInitializeException;
import app.commons.exceptions.ReplicationInitializeException;
import app.commons.exceptions.SystemException;
import app.commons.utils.StreamUtil;
import app.controllers.BaseIntegrationTest;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.services.BootstrapService;

public class ArgsInitializationTest extends BaseIntegrationTest {

    private final FaultToleranceModule ftModule = BootstrapService.getInstance();
    private Level ftLevel;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public ArgsInitializationTest() {
        super();
        this.ftLevel = new Level("only test");
    }

    @Test(expected = ReplicationInitializeException.class)
    public void testValidateReplicationInitialize() throws InterruptedException, SystemException {
        this.ftLevel.addTechnique(new Replication(new ArrayList<>()));
        Assert.assertTrue(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize01() throws InterruptedException, SystemException {

        this.ftLevel.addTechnique(new SoftwareRejuvenation());
        this.ftLevel.addTechnique(new SoftwareRejuvenation());
        Assert.assertFalse(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertTrue(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize02() throws InterruptedException, SystemException {

        this.ftLevel.addTechnique(new Retry());
        this.ftLevel.addTechnique(new Retry());
        Assert.assertTrue(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize03() throws InterruptedException, SystemException {

        this.ftLevel.addTechnique(new TaskResubmission());
        this.ftLevel.addTechnique(new TaskResubmission());
        Assert.assertTrue(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = DuplicateInitializeException.class)
    public void testValidateDuplicateInitialize04() throws InterruptedException, SystemException {

        this.ftLevel.addTechnique(new Replication(this.lstFakeNodes));
        this.ftLevel.addTechnique(new Replication(this.lstFakeNodes));
        Assert.assertTrue(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = ArgumentsInitializeException.class)
    public void testArgsInitializeException01() throws InterruptedException, SystemException {
        this.ftLevel = new Level("only test");
        Assert.assertFalse(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.REACTVE));
        Assert.assertFalse(StreamUtil.hasFaultTolerancePolicy(this.ftLevel.getLstTechniques(), FaultToletancePolicy.PROACTIVE));
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = ArgumentsInitializeException.class)
    public void testArgsInitializeException02() throws InterruptedException, SystemException {
        this.ftLevel = new Level();
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = ArgumentsInitializeException.class)
    public void testArgsInitializeException03() throws InterruptedException, SystemException {
        this.ftLevel = new Level("only test");
        this.ftLevel.addTechnique(new TaskResubmission());
        this.ftModule.start(this.ftLevel);
    }

    @Test(expected = ArgumentsInitializeException.class)
    public void testArgsInitializeException04() throws InterruptedException, SystemException {
        this.ftLevel = new Level("only test");
        this.ftLevel.addTechnique(new Retry());
        this.ftModule.start(this.ftLevel);
    }
}
