package app.commons;

import org.apache.log4j.Logger;

public final class LoggerUtil {

	private static final Logger LOGGER = Logger.getLogger(LoggerUtil.class);

	private LoggerUtil() {
		super();
	}

	synchronized public static void error(final Throwable t) {
		error(t.getMessage(), t);
	}

	synchronized public static void error(final Object message, final Throwable t) {
		LOGGER.error(message, t);
	}

	synchronized public static void warn(final Object message) {
		LOGGER.warn(message);
	}
	
	synchronized public static void warn(final Object message, final Throwable t) {
		LOGGER.warn(message, t);
	}

	synchronized public static void info(final Object message) {
		LOGGER.info(message);
	}
	
	synchronized public static void debug(final Object string) {
		LOGGER.debug(string);
	}
}