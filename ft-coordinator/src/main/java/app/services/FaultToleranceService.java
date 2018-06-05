package app.services;

import static app.commons.enums.SystemEnums.ExecutionStatus.ERROR;
import static app.commons.enums.SystemEnums.ExecutionStatus.RECOVERY_MODE;
import static app.commons.enums.SystemEnums.ExecutionStatus.STARTED;
import static app.commons.enums.SystemEnums.ExecutionStatus.STOPPED;
import static app.commons.enums.SystemEnums.FaultToletancePolicy.PROACTIVE;
import static app.commons.enums.SystemEnums.FaultToletancePolicy.REACTVE;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import app.commons.enums.SystemEnums.ExecutionStatus;
import app.commons.utils.LoggerUtil;
import app.commons.utils.StreamUtil;
import app.models.Level;
import app.models.Technique;

public class FaultToleranceService {

    protected static final String SERVICE_NAME = "Service - Fault Tolerance ";
    protected static final String SERVICE_NAME_STARTED = SERVICE_NAME + "STARTED";
    protected static final String SERVICE_NAME_ALREADY_STARTED = SERVICE_NAME + "ALREADY STARTED";
    protected static ExecutionStatus status = STOPPED;
    protected static ScheduledExecutorService scheduledExecutors = Executors.newSingleThreadScheduledExecutor();
    protected static ExecutorService threadExecutors = Executors.newSingleThreadExecutor();
    protected static ScheduledFuture<?> proactiveService;
    protected static ScheduledFuture<?> reactiveService;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public FaultToleranceService() {
        super();
    }

    public static ExecutionStatus startDetectionServices(final Level level) {
        if (STARTED != FaultToleranceService.status && RECOVERY_MODE != FaultToleranceService.status) {
            try {
                if (StreamUtil.hasFaultToleranceType(level.getLstTechniques(), PROACTIVE)) {
                    new PFTService(level).startService();
                }
                if (StreamUtil.hasFaultToleranceType(level.getLstTechniques(), REACTVE)) {
                    new RFTService(level).startService();
                }
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                FaultToleranceService.status = ERROR;
                LoggerUtil.error(e);
            }
        } else {
            LoggerUtil.info(SERVICE_NAME_ALREADY_STARTED);
        }
        return FaultToleranceService.status;
    }

    public static ExecutionStatus startRecoveryServices(final String moduleId, final String taskStartupCommand, final Technique technique) {
        if (RECOVERY_MODE != FaultToleranceService.status) {
            new RecoveryService(moduleId, taskStartupCommand, technique).startService();
        }
        return FaultToleranceService.status;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static ExecutionStatus getStatus() {
        return FaultToleranceService.status;
    }
}
