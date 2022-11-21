package com.github.open.casslog.logging;

import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0.0
 */
public class CassLoggingSystem extends Log4J2LoggingSystem {

    public static final String LOG_FILE = "casslog.yml";

    public static String getLogConfigFile() {
        return LOG_FILE;
    }

    public CassLoggingSystem(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    protected String[] getStandardConfigLocations() {
        return new String[]{getLogConfigFile()};
    }

}
