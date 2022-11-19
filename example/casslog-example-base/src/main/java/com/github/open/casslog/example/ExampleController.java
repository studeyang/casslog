package com.github.open.casslog.example;

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

    private static final String SUCCESS = "success";

    @Autowired
    private ExampleService exampleService;

    @GetMapping("/log")
    public String log() {

        log.debug("This is a debug message.");
        log.info("This is an info message.");
        log.warn("This is a warn message.");
        log.error("This is an error message.");

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
