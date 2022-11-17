package com.github.open.casslog.level;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 */
public class ConfigServiceFactory {

    public static ConfigService getInstance(Environment environment) throws NacosException {
        // 初始化 nacos 连接
        String serverAddr = environment.getProperty("nacos.config.server-addr");
        String namespace = environment.getProperty("nacos.config.namespace");
        String username = environment.getProperty("nacos.config.username");
        String password = environment.getProperty("nacos.config.password");

        Properties nacosConnectProperties = new Properties();
        if (username != null && !username.isEmpty()) {
            nacosConnectProperties.put("username", username);
        }
        if (password != null && !password.isEmpty()) {
            nacosConnectProperties.put("password", password);
        }
        nacosConnectProperties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        nacosConnectProperties.put(PropertyKeyConst.NAMESPACE, namespace);
        ConfigService configService = NacosFactory.createConfigService(nacosConnectProperties);

        return configService;
    }

}