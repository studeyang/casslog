package com.github.open.xx;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/studeyang">studeyang</a>
 * @date 2021/10/15
 */
@Slf4j
@RestController
@RequestMapping("/level")
public class LevelController {

    @PostMapping("/set")
    public void config(@RequestParam String loggerName, @RequestParam String level) {

        if (loggerName.isEmpty()) {
            loggerName = LogManager.ROOT_LOGGER_NAME;
        }
//
//        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
//        Configuration configuration = loggerContext.getConfiguration();
//        LoggerConfig loggerConfig = configuration.getLoggers().get(loggerName);
//        loggerConfig.setLevel(Level.valueOf(level.toUpperCase()));
//        loggerContext.updateLoggers();

        Configurator.setAllLevels(loggerName, Level.valueOf(level.toUpperCase()));

    }

}
