package app.controllers;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.models.AttemptsNumber;
import app.models.DelayBetweenAttempts;
import app.models.Level;
import app.models.SoftwareRejuvenation;
import app.models.Timeout;

public class TechniqueSoftwareRejuvenationTest extends BaseIntegrationTest {

    private final static String STARTUP_COMMAND = "touch zzz-createdBy-SoftwareRejuvenation.txt";
    private final Level ftLevel = new Level(STARTUP_COMMAND, super.zooInstance);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public TechniqueSoftwareRejuvenationTest() {
        super();
        this.ftLevel.setModuleId(MODULE_ID);
        this.ftLevel.addTechnique(new SoftwareRejuvenation(new AttemptsNumber(), new DelayBetweenAttempts(), new Timeout(30, TimeUnit.SECONDS), 97, 95));
    }

    @Test
    public void testSoftwareRejuvenation() throws Exception {

        super.ftModule.start(this.ftLevel);
        TimeUnit.SECONDS.sleep(120);

        super.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);

        Assert.assertTrue(super.ftModule.isTerminated());
    }
}
