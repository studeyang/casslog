package com.github.open.xx.logextend;

import com.github.open.casslog.core.logging.AbstractLogExtend;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0 2022/6/16
 */
@Component
public class TestLogExtend extends AbstractLogExtend {

    @Override
    public String logConfig() {
        return "classpath:com/github/open/xx/logextend/test-log4j.xml";
    }

}
