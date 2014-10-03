package org.springframework.cloud.hystrix.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableAutoConfiguration
public class HystrixAggregatorApp {
    public static void main(String[] args) {
        SpringApplication.run(HystrixAggregatorApp.class, args);
    }
}
