package com.github.open.xx;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dbses.DbsesLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2021/10/15
 */
@Slf4j
@RestController
public class ExampleController {

    // Same as @Slf4j
//    private static final Logger log = LoggerFactory.getLogger(ExampleController.class);

    @NacosValue(value = "${nacos.config}", autoRefreshed = true)
    private String config;

    private static final String SUCCESS = "success";

    @Autowired
    private ExampleService exampleService;

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

        DbsesLogger.log();

        return SUCCESS;
    }

    @GetMapping("/traceid")
    public void traceid() {

        log.info("main thread");

        exampleService.asyncMethod();

        ExecutorService executorService = new ThreadPoolExecutor(
                5,
                10,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        for (int i = 0; i < 30; i++) {
            executorService.execute(RunnableWrapper.of(() -> log.info("new thread")));
        }

    }


}
