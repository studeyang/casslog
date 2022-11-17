package com.github.open.xx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2022/3/4
 */
@Slf4j
@Service
public class ExampleService {

    @Bean("asyncTest")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(3,
                10,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }

    //    @Async("asyncTest")
    @Async
    public void asyncMethod() {
        log.info("async method call.");
    }

}
