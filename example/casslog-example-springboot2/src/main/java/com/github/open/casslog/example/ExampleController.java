package com.github.open.casslog.example;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2021/10/15
 */
@Slf4j
@RestController
public class ExampleController {
    @NacosValue(value = "${nacos.config}", autoRefreshed = true)
    private String config;

    private static final String SUCCESS = "success";

    @GetMapping("/config")
    public String config() {
        return config;
    }

    @GetMapping("/log")
    public String log() {

        log.debug("This is a debug message.");
        log.info("This is an info message.");
        log.warn("This is a warn message.");
        log.error("This is an error message.");

        return SUCCESS;
    }

}
