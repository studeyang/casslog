package com.github.open.casslog.level;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.3 2022/6/29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingLevelSetter {

    private static final String ROOT_LOGGER = "root";

    public static void set(Map<String, String> levelMap) {
        if (levelMap == null) {
            return;
        }
        levelMap.forEach((loggerName, levelValue) ->
                setLogLevel(getLoggerName(loggerName), getLevel(levelValue)));
    }

    public static void setLogLevel(String loggerName, Level level) {
        LoggerConfig loggerConfig = getLoggerConfig(loggerName);
        if (loggerConfig == null) {
            loggerConfig = new LoggerConfig(loggerName, level, true);
            getLoggerContext().getConfiguration().addLogger(loggerName, loggerConfig);
        } else {
            loggerConfig.setLevel(level);
        }
        getLoggerContext().updateLoggers();
    }

    private static LoggerConfig getLoggerConfig(String name) {
        name = (StringUtils.hasText(name) ? name : LogManager.ROOT_LOGGER_NAME);
        return getLoggerContext().getConfiguration().getLoggers().get(name);
    }

    private static LoggerContext getLoggerContext() {
        return (LoggerContext) LogManager.getContext(false);
    }

    static Level getLevel(String level) {
        try {
            return Level.valueOf(level);
        } catch (IllegalArgumentException e) {
            return Level.INFO;
        }
    }

    private static String getLoggerName(String loggerName) {
        // Log4j 的 RootLogger 获取时传空
        if (ROOT_LOGGER.equals(loggerName)) {
            return LogManager.ROOT_LOGGER_NAME;
        }
        return loggerName;
    }

}
