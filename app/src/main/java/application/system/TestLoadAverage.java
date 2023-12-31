/*
 *
 * Copyright (c) 2022 Matthieu Le Franc
 *
 * You are prohibited from sharing and distributing this creation without our prior authorization, more specifically:
 *
 * TO PROVIDE A COPY OF OUR GAME TO ANY THIRD PARTY;
 * TO USE THIS CREATION FOR COMMERCIAL PURPOSES;
 * TO USE THIS CREATIONS FOR PROFIT;
 * TO ALLOW ANY THIRD PARTY TO ACCESS TO THIS CREATION IN AN UNFAIR OR ABUSIVE MANNER;
 *
 */
package application.system;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class TestLoadAverage {

    public static void testCompute() {
        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        if (bean == null) {
            throw new NullPointerException(
                    "Unable to collect operating system metrics, jmx bean is null");
        }
        System.out.println("System architecture : " + bean.getArch());
        System.out.println("Number of processor(s) : " + bean.getAvailableProcessors());
        System.out.println("Operating system name : " + bean.getName());
        System.out.println("Operating system version : " + bean.getVersion());
        System.out.println("############################");
        // System.out.println("System load average for the last minute : "+bean.getSystemLoadAverage());
    }

    public static int getNumberOfProcessor() {
        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        if (bean == null) {
            throw new NullPointerException(
                    "Unable to collect operating system metrics, jmx bean is null");
        }
        return bean.getAvailableProcessors();
    }
}
