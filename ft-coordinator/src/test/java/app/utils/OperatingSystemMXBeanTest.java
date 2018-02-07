package app.utils;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

import com.sun.management.OperatingSystemMXBean;

import app.commons.exceptions.SystemException;
import app.commons.monitors.ResourceMonitor;
import app.commons.monitors.impl.ResourceMonitorImpl;

@SuppressWarnings("restriction")
public class OperatingSystemMXBeanTest {

    private static final double LOAD = 0.8;
    private static final int TIME_SECONDS = 30;
    private static final long DURATION = 1000 * TIME_SECONDS;
    private final OperatingSystemMXBean threadBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    private void startStress() {
        final int numCore = this.threadBean.getAvailableProcessors();
        final int numThreadsPerCore = 1;
        for (int i = 0; i < numCore * numThreadsPerCore; i++) {
            new BusyThread("Thread-" + i, LOAD, DURATION).start();
        }
    }

    @Test
    public void testResourceMonitor() throws InterruptedException, SystemException {

        this.startStress();
        final ResourceMonitor monitor = new ResourceMonitorImpl();
        final long startTime = Instant.now().getEpochSecond();
        while (Instant.now().getEpochSecond() - startTime < TIME_SECONDS) {
            System.out.println("CPU Usage: " + monitor.getCpuUsage());
            System.out.println("MEM Usage: " + monitor.getMemUsage());
        }
        System.out.println("======================================================");
        System.out.println("CPU Usage: " + monitor.getCpuUsage());
        System.out.println("MEM Usage: " + monitor.getMemUsage());
    }

    @Test
    public void testMXBeanResourceUsage() throws InterruptedException, SystemException {

        final long nanoBefore = System.nanoTime();
        final long cpuBefore = this.threadBean.getProcessCpuTime();

        //        this.startStress();
        //        final long startTime = Instant.now().getEpochSecond();
        //        while (Instant.now().getEpochSecond() - startTime < TIME_SECONDS) {
        //            System.out.println("load: " + this.threadBean.getProcessCpuLoad());
        //        }

        final long cpuAfter = this.threadBean.getProcessCpuTime();
        final long nanoAfter = System.nanoTime();

        long percent;
        if (nanoAfter > nanoBefore) {
            percent = ((cpuAfter - cpuBefore) * 100L) / (nanoAfter - nanoBefore);
        } else {
            percent = 0;
        }

        System.out.println("Cpu usage: " + percent + "%");
        System.out.println("==============================================================================");
        System.out.println("CPU usage this last minute:  " + this.threadBean.getSystemLoadAverage());
        System.out.println("getFreePhysicalMemorySize:   " + this.threadBean.getFreePhysicalMemorySize());
        System.out.println("Available Processors:        " + this.threadBean.getAvailableProcessors());
        System.out.println("Return the OS architecture:  " + this.threadBean.getArch());
        System.out.println("Returns the OS name       :  " + this.threadBean.getName());
        System.out.println("Returns the OS version    :  " + this.threadBean.getVersion());
        System.out.println("ObjectName instance       :  " + this.threadBean.getObjectName());
    }

    private static class BusyThread extends Thread {

        private final double load;
        private final long duration;

        public BusyThread(final String name, final double load, final long duration) {
            super(name);
            this.load = load;
            this.duration = duration;
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // * @see java.lang.Thread#run()
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        @Override
        public void run() {
            final Instant startTime = Instant.now();
            try {
                while (Duration.between(startTime, Instant.now()).toMillis() < this.duration) {
                    if (System.currentTimeMillis() % 100 == 0) {
                        Thread.sleep((long) Math.floor((1 - this.load) * 100));
                    }
                }
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
