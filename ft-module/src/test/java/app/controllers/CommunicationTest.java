package app.controllers;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;

public class CommunicationTest extends BaseIntegrationTest {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CommunicationTest() {
        super();
        super.ftLevel.addTechnique(new SoftwareRejuvenation(97, 95));
        super.ftLevel.addTechnique(new Retry());
        super.ftLevel.addTechnique(new TaskResubmission());
        super.ftLevel.addTechnique(new Replication(this.lstFakeNodes));
    }

    @Test
    public void testStartFtCoordinator() throws Exception {

        super.ftModule.start(super.ftLevel);
        TimeUnit.SECONDS.sleep(20);

        super.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);

        Assert.assertTrue(super.ftModule.isTerminated());
    }
}
