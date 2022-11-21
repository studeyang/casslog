package com.github.open.casslog.autoconfigure;

import com.github.open.casslog.logging.CassLogStarterBanner;
import com.github.open.casslog.logging.CassLoggingSystem;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0.0
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PrepareCassLogLoggingApplicationListener implements ApplicationListener<ApplicationEvent> {

    private static final String LOG4J_SKIPJANSI = "log4j.skipJansi";

    static {
        // 2.10 版本默认关闭，需手动开启
        if (System.getProperty(LOG4J_SKIPJANSI) == null) {
            System.setProperty(LOG4J_SKIPJANSI, "false");
        }
        System.setProperty(ConfigurationFactory.CONFIGURATION_FILE_PROPERTY, CassLoggingSystem.getLogConfigFile());
        System.setProperty(LoggingSystem.SYSTEM_PROPERTY, CassLoggingSystem.class.getName());

        System.setProperty("CONSOLE_PATTERN", "%d{yyyy-MM-dd HH:mm:ss,SSS} " +
                "%highlight{%5level}{ERROR=RED, WARN=Yellow, INFO=Green, DEBUG=Cyan, TRACE=White} " +
                "%t %traceId [%style{%c{1.}:%4line}{cyan}] - %m%n");
    }

    public PrepareCassLogLoggingApplicationListener() {
        // 利用 ApplicationListener 优先加载的特性，
        // 在spring初始化之前，完成日志相关的配置
        CassLogStarterBanner.print();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //什么都不干
    }

}
