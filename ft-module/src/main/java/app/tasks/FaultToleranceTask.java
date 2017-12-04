package app.tasks;

import app.commons.LoggerUtil;

public class FaultToleranceTask implements Runnable {

	public void run() {
		
		String threadName = Thread.currentThread().getName();
		for (int i = 0; i < 10; i++) {
			LoggerUtil.debug((i + 1) + " - Hello " + threadName);
		}
	}
}
