package org.springframework.cloud.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.util.ClassUtils;

/**
 * Listens for EnvironmentChangeEvents with keys that start with 'logging.level.' and
 * changes the logger to the new level.
 * Created by sgibb on 7/7/14.
 */
public class LoggingListener /*implements ApplicationListener<EnvironmentChangeEvent>*/ {
    private static final Logger logger = LoggerFactory.getLogger(LoggingListener.class);
    public static final String LOGGING_LEVEL_PREFIX = "logging.level.";

    @Autowired
    Environment environment;

    //@Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        LoggingSystem system = LoggingSystem.get(ClassUtils.getDefaultClassLoader());

        for (String key : event.getKeys()) {
            if (key.startsWith(LOGGING_LEVEL_PREFIX)) {
                String name = key.substring(LOGGING_LEVEL_PREFIX.length());
                String property = environment.getProperty(key);
                LogLevel level = LogLevel.valueOf(property);

                logger.info("Logger {} changed to level {}", name, level);

                if (name.equals("root")) {
                    name = null;
                }
                system.setLogLevel(name, level);
            }
        }
    }
}
