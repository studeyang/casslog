package com.github.open.xx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2021/10/15
 */
@RestController
@Slf4j
public class ExampleController {

    // Same as @Slf4j
//    private static final Logger log = LoggerFactory.getLogger(ExampleController.class);

    private static final String SUCCESS = "success";


    @GetMapping("/log")
    public String log(@RequestParam(value = "param", defaultValue = "I_AM_PARAM") String param) {

        if (log.isDebugEnabled()) {
            log.debug("This is a debug message. 中文");
            log.debug("This is a debug message. {}", param);
        }

        log.info("This is an info message. 中文");
        log.info("This is an info message. {}", param);

        log.warn("This is a warn message. 中文");
        log.warn("This is a warn message. {}", param);

        log.error("This is an error message. 中文");
        log.error("This is an error message. {}", param);

        return SUCCESS;
    }

}
