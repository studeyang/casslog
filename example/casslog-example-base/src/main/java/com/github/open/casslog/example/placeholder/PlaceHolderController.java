package com.github.open.casslog.example.placeholder;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 */
@Log4j2
@RequestMapping("placeholder")
@RestController
public class PlaceHolderController {

    @GetMapping
    public void index() {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("debug1");
        log.debug("debug1:" + slowString("debug1"));
        stopWatch.stop();

        stopWatch.start("debug2");
        log.debug("debug2:{}", slowString("debug2"));
        stopWatch.stop();

        stopWatch.start("debug3");
        log.debug("debug3:{}", () -> slowString("debug3"));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    private String slowString(String s) {
        System.out.println("slowString called via " + s);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        return "OK";
    }
}
