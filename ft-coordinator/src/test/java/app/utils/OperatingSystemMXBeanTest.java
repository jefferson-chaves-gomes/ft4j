package app.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.junit.Test;

import app.commons.exceptions.SystemException;

public class OperatingSystemMXBeanTest {

    @Test
    public void testResourceUsage() throws InterruptedException, SystemException {

        long a, b, c;
        for (long i = 0; i < 1000000; i++) {
            a = i * 2;
            b = a;
            c = a + b;
            a = c - b;
        }

        OperatingSystemMXBean threadBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println("CPU usage this last minute:  " + threadBean.getSystemLoadAverage());
        System.out.println("Available Processors:        " + threadBean.getAvailableProcessors());
        System.out.println("Return the OS architecture:  " + threadBean.getArch());
        System.out.println("Returns the OS name       :  " + threadBean.getName());
        System.out.println("Returns the OS version    :  " + threadBean.getVersion());
        System.out.println("ObjectName instance       :  " + threadBean.getObjectName());
    }
}
