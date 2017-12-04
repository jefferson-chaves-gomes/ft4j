package app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import app.commons.LoggerUtil;
import app.models.FaultToleranceLevel;
import app.tasks.FaultToleranceTask;

public final class FaultToleranceMonitor implements FaultToleranceModule {

	private static FaultToleranceMonitor instance;
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private FaultToleranceLevel ftLevel;

	private FaultToleranceMonitor() {
		super();
	}

	public static FaultToleranceMonitor getInstance() {
		if (instance == null) {
			return new FaultToleranceMonitor();
		}
		return instance;
	}

	public void init(FaultToleranceLevel ftLevel) {
		this.ftLevel = ftLevel;
	}

	public void start() {
		FaultToleranceTask task = new FaultToleranceTask();
		this.executor.submit(task);
	}
	
	public void stop() {
		try {
			LoggerUtil.debug("attempt to shutdown executor");
			this.executor.shutdown();
			this.executor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LoggerUtil.debug("tasks interrupted");
		} finally {
			if (!this.executor.isTerminated()) {
				LoggerUtil.debug("cancel non-finished tasks");
			}
			this.executor.shutdownNow();
			LoggerUtil.debug("shutdown finished");
		}
	}

	public boolean isTerminated() {
		return this.executor.isShutdown() && this.executor.isTerminated();
	}
}
