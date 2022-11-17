package com.github.open.casslog.level;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.ConfigurableWebEnvironment;

import java.util.Map;

/**
 * 静态日志级别监听
 *
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.3 2022/6/29
 */
@Slf4j
@Data
public class StaticLoggingLevelListener implements InitializingBean, EnvironmentAware {

    @Override
    public void afterPropertiesSet() {
        log.info("初始化日志级别完成");
    }

    @Override
    @SneakyThrows
    public void setEnvironment(Environment environment) {
        if (environment instanceof ConfigurableWebEnvironment) {
            setLoggingLevel((ConfigurableWebEnvironment) environment);
        }
    }

    private void setLoggingLevel(ConfigurableWebEnvironment environment) {
        Map<String, String> casslogLevelMap = Maps.newHashMap();
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            initCasslogLevelMap(propertySource, casslogLevelMap);
        }
        if (!casslogLevelMap.isEmpty()) {
            LoggingLevelSetter.set(casslogLevelMap);
            log.info("更新日志级别完成：{}", casslogLevelMap);
        }
    }

    private void initCasslogLevelMap(PropertySource<?> propertySource, Map<String, String> casslogLevelMap) {
        try {
            Object source = propertySource.getSource();
            boolean mapInstance = source instanceof Map;
            if (!mapInstance) {
                return;
            }
            ((Map) source).forEach((key, value) -> {
                String stringKey = (String) key;
                if (stringKey.startsWith(DynamicLoggingLevelListener.LEVEL_PREFIX)) {
                    casslogLevelMap.putIfAbsent(stringKey.substring(DynamicLoggingLevelListener.LEVEL_PREFIX.length()), value.toString());
                }
            });
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

}
