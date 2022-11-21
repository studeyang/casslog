package com.github.open.casslog.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.UUID;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0.0
 */
@Slf4j
@ConditionalOnBean(PrepareCassLogLoggingApplicationListener.class)
public class CassLogPropertyPostProcessor implements EnvironmentPostProcessor {

    private static final String LOG_FILENAME = "log.filename";
    private static final String DEFAULT_LOG_FILENAME = "casslog";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        String logFileName = System.getProperty(LOG_FILENAME);

        // 如果未指定日志名，则用应用名称
        if (logFileName == null || DEFAULT_LOG_FILENAME.equals(logFileName)) {
            logFileName = environment.getProperty("spring.application.name");
        }

        // 如果应用名称未配置，则随机生成
        if (logFileName == null) {
            logFileName = "app-" + UUID.randomUUID().toString();
            log.warn("日志名称被随机指定为：{}", logFileName);
        }

        System.setProperty(LOG_FILENAME, logFileName);
    }
}
