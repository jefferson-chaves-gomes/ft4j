package app.controllers;

import static app.commons.constants.TimeConstants.DEFAULT_HEARTBEAT_TIME;
import static app.commons.constants.TimeConstants.DEFAULT_TIME_UNIT;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.models.AttemptsNumber;
import app.models.Level;
import app.models.Retry;
import app.models.Timeout;

public class TechniqueTaskResubmissionTest extends BaseIntegrationTest {

    private final static String STARTUP_COMMAND = String.format("touch zzz-createdBy-TaskResubmission_%s.txt", Instant.now());
    private final Level ftLevel;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public TechniqueTaskResubmissionTest() {
        super();
        this.ftLevel = new Level(STARTUP_COMMAND, RUNTIME_MODULE_ID, DEFAULT_HEARTBEAT_TIME);
        this.ftLevel.addTechnique(new Retry(new Timeout(1L, DEFAULT_TIME_UNIT), new AttemptsNumber()));
    }

    @Test
    public void testSoftwareRejuvenation() throws Exception {

        super.ftModule.start(this.ftLevel, STARTUP_COORDINATOR_COMMAND);
        TimeUnit.SECONDS.sleep(60);

        super.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);

        Assert.assertTrue(super.ftModule.isTerminated());
    }
}
