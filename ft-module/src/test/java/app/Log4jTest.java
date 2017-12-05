package app;

import app.commons.utils.LoggerUtil;
import junit.framework.TestCase;

public class Log4jTest extends TestCase {

    public void testLog4J() {
        try {
            LoggerUtil.debug("debug test ");
            LoggerUtil.info("info test ");
            LoggerUtil.warn("warn test");
            LoggerUtil.error("error test", new Exception("fake exception"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
        assertTrue(true);
    }
}
