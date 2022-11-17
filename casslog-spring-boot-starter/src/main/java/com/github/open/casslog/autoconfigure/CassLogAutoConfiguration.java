package com.github.open.casslog.autoconfigure;

import com.github.open.casslog.core.logging.AbstractLogExtend;
import com.github.open.casslog.core.logging.LogExtendInitializer;
import com.github.open.casslog.level.DynamicLoggingLevelListener;
import com.github.open.casslog.level.StaticLoggingLevelListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0 2022/6/15
 */
@Configuration
public class CassLogAutoConfiguration {

    @Bean
    public LogExtendInitializer logExtendInitializer(ObjectProvider<List<AbstractLogExtend>> logExtends) {
        List<AbstractLogExtend> list = logExtends.getIfAvailable();
        return new LogExtendInitializer(list == null ? new ArrayList<>() : list);
    }

    @Bean
    @ConditionalOnProperty(value = {"nacos.config.group", "spring.application.name"})
    public DynamicLoggingLevelListener dynamicLoggingLevelListener() {
        return new DynamicLoggingLevelListener();
    }

    @Bean
    @ConditionalOnMissingBean(DynamicLoggingLevelListener.class)
    public StaticLoggingLevelListener staticLoggingLevelListener() {
        return new StaticLoggingLevelListener();
    }

}
