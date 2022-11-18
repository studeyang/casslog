package com.github.open.casslog.level;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import io.github.open.config.support.MultiProfilesYamlConfigParseSupport;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态日志级别监听
 *
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.3 2022/6/29
 */
@Slf4j
@Data
public class DynamicLoggingLevelListener extends StaticLoggingLevelListener {

    static final String LEVEL_PREFIX = "casslog.level.";

    @NacosConfigListener(groupId = "${nacos.config.group}", dataId = "${spring.application.name}.yml", timeout = 5000)
    public void casslogLevelChange(String newLog) {

        Map<String, Object> properties = new MultiProfilesYamlConfigParseSupport().parse(newLog);
        Map<String, String> casslogLevelMap = new HashMap<>();

        for (Object object : properties.keySet()) {
            String propertyName = String.valueOf(object);

            if (propertyName.startsWith(LEVEL_PREFIX)) {

                casslogLevelMap.put(propertyName.substring(LEVEL_PREFIX.length()),
                        (String) properties.getOrDefault(propertyName, "info"));
            }
        }

        if (!casslogLevelMap.isEmpty()) {
            LoggingLevelSetter.set(casslogLevelMap);
            log.info("更新日志级别完成：{}", casslogLevelMap);
        }
    }

}
