package com.github.open.casslog.core.logging;

import com.github.open.casslog.core.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.3 2022/6/15
 */
@Slf4j
public abstract class AbstractLogExtend {

    private static final String FILE_PROTOCOL = "file";
    private static final Set<String> CAN_NOT_UPDATE_LOGGER = new HashSet<>();

    static {
        CAN_NOT_UPDATE_LOGGER.add(""); // root
    }

    public void loadConfiguration() {
        final LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        final Configuration contextConfiguration = loggerContext.getConfiguration();

        // load and start casslog extend configuration
        Configuration configurationExtend = loadConfiguration(loggerContext);
        configurationExtend.start();

        // append loggers and appenders to contextConfiguration
        Map<String, Appender> appenders = configurationExtend.getAppenders();
        for (Appender appender : appenders.values()) {
            addAppender(contextConfiguration, appender);
        }
        Map<String, LoggerConfig> loggersExtend = configurationExtend.getLoggers();
        loggersExtend.forEach((loggerName, loggerConfig) ->
                addLogger(contextConfiguration, loggerName, loggerConfig)
        );

        loggerContext.updateLoggers();
    }

    private void addLogger(Configuration contextConfiguration, String loggerName, LoggerConfig loggerConfig) {
        LoggerConfig contextLoggerConfig = contextConfiguration.getLoggerConfig(loggerName);
        if (CAN_NOT_UPDATE_LOGGER.contains(loggerName)) {
            return;
        }
        if (contextLoggerConfig != null) {
            contextConfiguration.removeLogger(loggerName);
            contextConfiguration.addLogger(loggerName, loggerConfig);
            log.warn("Logger '{}' updated.", loggerName);
            return;
        }
        contextConfiguration.addLogger(loggerName, loggerConfig);
        log.warn("Logger '{}' added.", loggerName);
    }

    private void addAppender(Configuration contextConfiguration, Appender appender) {
        Appender contextAppender = contextConfiguration.getAppender(appender.getName());
        if (contextAppender != null) {
            return;
        }
        contextConfiguration.addAppender(appender);
        log.warn("Appender '{}' added.", appender.getName());
    }

    /**
     * 日志配置
     *
     * @return 日志配置文件名
     */
    public abstract String logConfig();

    private Configuration loadConfiguration(LoggerContext loggerContext) {
        try {
            URL url = ResourceUtils.getResourceUrl(logConfig());
            ConfigurationSource source = getConfigurationSource(url);
            // since log4j 2.7 getConfiguration(LoggerContext loggerContext, ConfigurationSource source)
            return ConfigurationFactory.getInstance().getConfiguration(loggerContext, source);
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize Log4J2 logging from " + logConfig(), e);
        }
    }

    private ConfigurationSource getConfigurationSource(URL url) throws IOException {
        InputStream stream = url.openStream();
        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            return new ConfigurationSource(stream, ResourceUtils.getResourceAsFile(url));
        }
        return new ConfigurationSource(stream, url);
    }

    @Override
    public String toString() {
        return logConfig();
    }
}
