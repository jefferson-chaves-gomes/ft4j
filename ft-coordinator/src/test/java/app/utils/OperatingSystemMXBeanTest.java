package app.utils;

import java.lang.management.ManagementFactory;
import java.time.Instant;

import org.junit.Test;

import app.commons.exceptions.SystemException;

public class OperatingSystemMXBeanTest {

    @Test
    public void testResourceUsage() throws InterruptedException, SystemException {

        final int numCore = 4;
        final int numThreadsPerCore = 2;
        final double load = 0.8;
        final long duration = 1000 * 60;
        for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
            new BusyThread("Thread" + thread, load, duration).start();
        }

        final com.sun.management.OperatingSystemMXBean threadBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        final long startTime = Instant.now().getEpochSecond();
        while (Instant.now().getEpochSecond() - startTime < 60) {
            System.out.println("load: " + threadBean.getProcessCpuLoad() + "     time: " + threadBean.getProcessCpuTime());
        }

        System.out.println("CPU usage this last minute:  " + threadBean.getSystemLoadAverage());
        System.out.println("CPU usage this last minute:  " + threadBean.getFreePhysicalMemorySize());

        System.out.println("Available Processors:        " + threadBean.getAvailableProcessors());
        System.out.println("Return the OS architecture:  " + threadBean.getArch());
        System.out.println("Returns the OS name       :  " + threadBean.getName());
        System.out.println("Returns the OS version    :  " + threadBean.getVersion());
        System.out.println("ObjectName instance       :  " + threadBean.getObjectName());
    }

    //    https://stackoverflow.com/questions/19781087/using-operatingsystemmxbean-to-get-cpu-usage
    //    https://www.programcreek.com/java-api-examples/index.php?api=java.lang.management.OperatingSystemMXBean
    //    https://stackoverflow.com/questions/47177/how-do-i-monitor-the-computers-cpu-memory-and-disk-usage-in-java/27282046

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
            final long startTime = System.currentTimeMillis();
            try {
                // Loop for the given duration
                while (System.currentTimeMillis() - startTime < this.duration) {
                    // Every 100ms, sleep for the percentage of unladen time
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
