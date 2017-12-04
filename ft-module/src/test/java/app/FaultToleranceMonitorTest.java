package app;

import java.util.concurrent.TimeUnit;

import app.models.FaultToleranceLevel;
import junit.framework.TestCase;

public class FaultToleranceMonitorTest extends TestCase {

	public void testFTModule() throws InterruptedException {
		
		FaultToleranceMonitor ftMonitor = FaultToleranceMonitor.getInstance();
		FaultToleranceLevel ftLevel = new FaultToleranceLevel();
		ftMonitor.init(ftLevel);
		ftMonitor.start();
		ftMonitor.stop();
		TimeUnit.SECONDS.sleep(1);
		assertTrue(ftMonitor.isTerminated());
	}
}
