package app.controllers;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import app.models.Level;
import app.models.SoftwareRejuvenation;
import app.models.Timeout;

public class TechniqueSoftwareRejuvenationTest extends BaseIntegrationTest {

    private final static String STARTUP_COMMAND = String.format("touch zzz-createdBy-SoftwareRejuvenation_%s.txt", Instant.now());
    private final Level ftLevel;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public TechniqueSoftwareRejuvenationTest() {
        super();
        this.ftLevel = new Level(STARTUP_COMMAND);
        this.ftLevel.setModuleId(RUNTIME_MODULE_ID);
        this.ftLevel.addTechnique(new SoftwareRejuvenation(new Timeout(30L, TimeUnit.SECONDS), 97, 95));
    }

    @Test
    public void testSoftwareRejuvenation() throws Exception {

        super.ftModule.start(this.ftLevel);
        TimeUnit.SECONDS.sleep(60);

        super.ftModule.stop();
        TimeUnit.SECONDS.sleep(10);

        Assert.assertTrue(super.ftModule.isTerminated());
    }
}
