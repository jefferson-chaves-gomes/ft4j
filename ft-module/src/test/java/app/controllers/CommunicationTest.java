package app.controllers;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;

public class CommunicationTest extends BaseIntegrationTest {

    private final static String STARTUP_COMMAND = "touch zzz-createdBy-CommunicationTest.txt";
    private final Level ftLevel = new Level(STARTUP_COMMAND, super.zooInstance);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CommunicationTest() {
        super();
        this.ftLevel.addTechnique(new SoftwareRejuvenation(97, 95));
        this.ftLevel.addTechnique(new Retry());
        this.ftLevel.addTechnique(new TaskResubmission());
        this.ftLevel.addTechnique(new Replication(this.lstFakeNodes));
    }

    @Test
    public void testStartFtCoordinator() throws Exception {

        super.ftModule.start(this.ftLevel);
        TimeUnit.SECONDS.sleep(20);

        super.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);

        Assert.assertTrue(super.ftModule.isTerminated());
    }
}
