/*
 * ******************************************************************************
 * Copyright 2017 Contributors to Exact Sciences Institute, Department Computer Science, University of Brasília - UnB
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package app.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

import app.commons.exceptions.SystemException;
import app.commons.monitors.ResourceMonitor;
import app.commons.monitors.impl.ResourceMonitorImpl;

public class ResourceMonitorTest {

    private static final double LOAD = 0.8;
    private static final int TIME_SECONDS = 10;
    private static final long DURATION = 1000 * TIME_SECONDS;
    private final OperatingSystemMXBean threadBean = ManagementFactory.getOperatingSystemMXBean();

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

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
