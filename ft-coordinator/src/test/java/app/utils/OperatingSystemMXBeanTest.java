package app.utils;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

import com.sun.management.OperatingSystemMXBean;

import app.commons.exceptions.SystemException;

@SuppressWarnings("restriction")
public class OperatingSystemMXBeanTest {

    private static final double LOAD = 0.8;
    private static final int TIME_SECONDS = 60;
    private static final long DURATION = 1000 * TIME_SECONDS;

    @Test
    public void testResourceUsage() throws InterruptedException, SystemException {

        final OperatingSystemMXBean threadBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        final int numCore = threadBean.getAvailableProcessors();
        final int numThreadsPerCore = 1;
        for (int i = 0; i < numCore * numThreadsPerCore; i++) {
            new BusyThread("Thread-" + i, LOAD, DURATION).start();
        }

        long nanoBefore = System.nanoTime();
        long cpuBefore = threadBean.getProcessCpuTime();

        final long startTime = Instant.now().getEpochSecond();
        while (Instant.now().getEpochSecond() - startTime < TIME_SECONDS) {
            System.out.println("load: " + threadBean.getProcessCpuLoad());
        }

        long cpuAfter = threadBean.getProcessCpuTime();
        long nanoAfter = System.nanoTime();

        long percent;
        if (nanoAfter > nanoBefore)
            percent = ((cpuAfter - cpuBefore) * 100L) / (nanoAfter - nanoBefore);
        else
            percent = 0;

        System.out.println("Cpu usage: " + percent + "%");
        System.out.println("==============================================================================");
        System.out.println("CPU usage this last minute:  " + threadBean.getSystemLoadAverage());
        System.out.println("getFreePhysicalMemorySize:   " + threadBean.getFreePhysicalMemorySize());
        System.out.println("Available Processors:        " + numCore);
        System.out.println("Return the OS architecture:  " + threadBean.getArch());
        System.out.println("Returns the OS name       :  " + threadBean.getName());
        System.out.println("Returns the OS version    :  " + threadBean.getVersion());
        System.out.println("ObjectName instance       :  " + threadBean.getObjectName());
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
            Instant startTime = Instant.now();
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
