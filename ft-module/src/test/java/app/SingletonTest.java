package app;

import org.junit.Assert;
import org.junit.Test;

import app.commons.exceptions.SystemException;
import app.services.BootstrapService;

public class SingletonTest {

    @Test
    public void testSingletonBootstrapService() throws InterruptedException, SystemException {
        final FaultToleranceModule ftModule = BootstrapService.getInstance();
        final FaultToleranceModule ftModule2 = BootstrapService.getInstance();
        Assert.assertEquals(ftModule, ftModule2);
        Assert.assertSame(ftModule, ftModule2);
    }
}
