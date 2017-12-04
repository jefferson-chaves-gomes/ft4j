package app;

import app.commons.LoggerUtil;
import junit.framework.TestCase;

public class Log4jTest extends TestCase {

	public void testLog4J() {
		LoggerUtil.debug("debug hohohoho ");
		LoggerUtil.info("info hihihihi ");
		LoggerUtil.warn("warn hehehehe");
		LoggerUtil.error("error hahahaha", new Exception("fake exception"));
	}
}
