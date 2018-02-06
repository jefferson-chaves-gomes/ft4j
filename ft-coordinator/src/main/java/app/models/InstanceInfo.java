package app.models;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Locale;

import app.commons.utils.EnumUtil.OS;

public class InstanceInfo {

    public static int CORES_NUMBER;
    public static OS OS_TYPE = OS.OTHER;

    static {
        CORES_NUMBER = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }

    public static OS getOs() {
        return os;
    }

    class OsInfo {

        public static final String OS_ARCH;
        public static final String OS_NAME;
        public static final String OS_VERSION;

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Constructors.
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        static {
            OS_ARCH = ManagementFactory.getOperatingSystemMXBean().getArch();
            OS_NAME = ManagementFactory.getOperatingSystemMXBean().getName();
            OS_VERSION = ManagementFactory.getOperatingSystemMXBean().getVersion();
        }

        public determineOS() {
            try {
                String osName = System.getProperty("os.name");
                if (osName == null) {
                    throw new IOException("os.name not found");
                }
                osName = osName.toLowerCase(Locale.ENGLISH);
                if (osName.contains("windows")) {
                    os = OS.WINDOWS;
                } else if (osName.contains("linux")
                        || osName.contains("mpe/ix")
                        || osName.contains("freebsd")
                        || osName.contains("irix")
                        || osName.contains("digital unix")
                        || osName.contains("unix")) {
                    os = OS.UNIX;
                } else if (osName.contains("mac os")) {
                    os = OS.MAC;
                } else if (osName.contains("sun os")
                        || osName.contains("sunos")
                        || osName.contains("solaris")) {
                    os = OS.POSIX_UNIX;
                } else if (osName.contains("hp-ux")
                        || osName.contains("aix")) {
                    os = OS.POSIX_UNIX;
                } else {
                    os = OS.OTHER;
                }

            } catch (final Exception ex) {
                os = OS.OTHER;
            } finally {
                os.setVersion(System.getProperty("os.version"));
            }
        }
    }
}