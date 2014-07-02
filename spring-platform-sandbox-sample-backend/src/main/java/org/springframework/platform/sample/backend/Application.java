package org.springframework.platform.sample.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.platform.eureka.EurekaShutdownHandler;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    @Bean
    EurekaShutdownHandler eurekaShutdownHandler() {
        return new EurekaShutdownHandler();
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

