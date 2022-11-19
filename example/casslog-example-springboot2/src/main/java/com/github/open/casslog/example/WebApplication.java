package com.github.open.casslog.example;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2021/10/14
 */
@Slf4j
@SpringBootApplication
@NacosPropertySource(
        groupId = "${nacos.config.group}",
        dataId = "${spring.application.name}.yml",
        first = true,
        autoRefreshed = true
)
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
        log.trace("hah");
        log.debug("hah");
        log.info("hah");
        log.warn("hah");
        log.error("hah", new RuntimeException("test"));
    }

}
