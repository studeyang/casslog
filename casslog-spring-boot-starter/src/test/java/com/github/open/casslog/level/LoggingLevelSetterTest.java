package com.github.open.casslog.level;

import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0 2022/6/29
 */
public class LoggingLevelSetterTest {

    @Test
    public void test_getLevel() {
        Assert.assertEquals(Level.DEBUG, LoggingLevelSetter.getLevel("debug"));
        Assert.assertEquals(Level.DEBUG, LoggingLevelSetter.getLevel("DEBUG"));
        Assert.assertEquals(Level.INFO, LoggingLevelSetter.getLevel("info"));
        Assert.assertEquals(Level.INFO, LoggingLevelSetter.getLevel("INFO"));
    }
}