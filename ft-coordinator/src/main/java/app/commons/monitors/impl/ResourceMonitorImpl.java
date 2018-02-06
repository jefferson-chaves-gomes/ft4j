package app.commons.monitors.impl;

import java.io.IOException;

import app.commons.monitors.ResourceMonitor;
import app.commons.utils.LoggerUtil;
import app.commons.utils.RuntimeUtil;
import app.commons.utils.RuntimeUtil.Command;

public class ResourceMonitorImpl implements ResourceMonitor {

    private static final String WIN_GET_CPU_USAGE = "wmic cpu get loadpercentage";
    private static final int UNKNOWN = -1;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.commons.monitors.ResourceMonitor#getCpuUsage()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public short getCpuUsage() {
        try {
            final String result = RuntimeUtil.execAndGetResponseString(new Command(WIN_GET_CPU_USAGE));
            return Short.valueOf(result);
        } catch (IOException | InterruptedException e) {
            LoggerUtil.warn("Error attempting to read the CPU usage", e);
        }
        return UNKNOWN;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.commons.monitors.ResourceMonitor#getMemUsage()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public short getMemUsage() {
        return UNKNOWN;
    }
}