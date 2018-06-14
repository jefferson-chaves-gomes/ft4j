package app.controllers;

import static app.commons.constants.TimeConstants.DEFAULT_HEARTBEAT_TIME;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;

public class CommunicationTest extends BaseIntegrationTest {

    private static final String STARTUP_COMMAND = String.format("touch zzz-createdBy-CommunicationTest_%s.txt", Instant.now());
    private final Level ftLevel;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CommunicationTest() {
        super();
        this.ftLevel = new Level(STARTUP_COMMAND, RUNTIME_MODULE_ID, DEFAULT_HEARTBEAT_TIME);
        this.ftLevel.addTechnique(new SoftwareRejuvenation(97, 95));
        this.ftLevel.addTechnique(new Retry());
        this.ftLevel.addTechnique(new TaskResubmission());
        this.ftLevel.addTechnique(new Replication(this.lstFakeNodes));
    }

    @Test
    public void testStartFtCoordinator() throws Exception {

        super.ftModule.start(this.ftLevel, STARTUP_COORDINATOR_COMMAND);
        TimeUnit.SECONDS.sleep(20);

        super.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);

        Assert.assertTrue(super.ftModule.isTerminated());
    }
}
