package org.springframework.platform.logging;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.platform.context.environment.EnvironmentChangeEvent;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Listens for EnvironmentChangeEvents with keys that start with 'logging.level.' and
 * changes the logger to the new level.
 * Created by sgibb on 7/7/14.
 */
public class LoggingListener implements ApplicationListener<EnvironmentChangeEvent> {
    private static final Logger logger = LoggerFactory.getLogger(LoggingListener.class);

    @Autowired
    Environment environment;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        Set<String> loggingProps = Sets.filter(event.getKeys(), new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String key) {
                return key.startsWith("logging.level.");
            }
        });

        if (loggingProps.isEmpty()) {
            return;
        }

        LoggingSystem system = LoggingSystem.get(ClassUtils.getDefaultClassLoader());

        for (String key: loggingProps) {
            String name = key.substring("logging.level.".length());
            String property = environment.getProperty(key);
            LogLevel level = LogLevel.valueOf(property);
            logger.info("Logger {} changed to level {}", name, level);
            if (name.equals("root")) {
                name = null;
            }
            system.setLogLevel(name, level);

            /*logger.trace("Testing trace");
            logger.debug("Testing debug");
            logger.info("Testing info");
            logger.warn("Testing warn");
            logger.error("Testing error");*/
        }
    }
}
