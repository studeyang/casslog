package com.github.open.casslog.core.logging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @since 2.0 2022/6/15
 */
@RequiredArgsConstructor
public class LogExtendInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogExtendInitializer.class);

    private final List<AbstractLogExtend> cassLogExtends;

    @PostConstruct
    public void init() {
        cassLogExtends.forEach(cassLogExtend -> {
            try {
                cassLogExtend.loadConfiguration();
                LOGGER.info("Load Log4j Configuration of {} successfully.", cassLogExtend.logConfig());
            } catch (Exception e) {
                LOGGER.error("Load Log4j Configuration of {} fail, message: {}", cassLogExtend.logConfig(), e.getMessage());
            }
        });
    }

}
