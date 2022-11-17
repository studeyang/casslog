package com.dbses;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2022/1/21
 */
@Slf4j
public class DbsesLogger {

    public static void log() {
        log.debug("This is a debug message.");
        log.info("This is an info message.");
        log.warn("This is a warn message.");
        log.error("This is an error message.");
    }

}
