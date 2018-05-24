package app.services;

import static app.commons.enums.SystemEnums.ExecutionStatus.STOPPED;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.commons.enums.SystemEnums.ExecutionStatus;
import app.models.Level;

public abstract class FaultToleranceService {

    protected Level level;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected static ExecutionStatus status = STOPPED;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public FaultToleranceService(final Level ftLevel) {
        super();
        this.level = ftLevel;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // abstracts.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public abstract void startServices();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static ExecutionStatus getStatus() {
        return status;
    }
}
