package com.github.open.casslog.accesslog;

import com.github.open.casslog.core.logging.AbstractLogExtend;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 1.0 2022/6/15
 */
public class AccessLogConfigExtend extends AbstractLogExtend {

    @Override
    public String logConfig() {
        return "classpath:com/github/open/casslog/accesslog/accesslog-log4j.xml";
    }

}
