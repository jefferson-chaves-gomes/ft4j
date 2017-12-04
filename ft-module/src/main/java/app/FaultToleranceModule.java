package app;

import app.models.FaultToleranceLevel;

public interface FaultToleranceModule {

	public void init(FaultToleranceLevel ftLevel);

	public void start();

	public void stop();
	
	public boolean isTerminated();
}
