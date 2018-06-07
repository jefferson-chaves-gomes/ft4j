package app.controllers;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.models.AttemptsNumber;
import app.models.DelayBetweenAttempts;
import app.models.SoftwareRejuvenation;
import app.models.Timeout;

public class TechniqueSoftwareRejuvenationTest extends BaseIntegrationTest {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public TechniqueSoftwareRejuvenationTest() {
        super();
        super.ftLevel.addTechnique(new SoftwareRejuvenation(new AttemptsNumber(), new DelayBetweenAttempts(), new Timeout(30, TimeUnit.SECONDS), 97, 95));
    }

    @Test
    public void testSoftwareRejuvenation() throws Exception {

        super.ftModule.start(super.ftLevel);
        TimeUnit.SECONDS.sleep(120);

        super.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);

        Assert.assertTrue(super.ftModule.isTerminated());
    }
}
