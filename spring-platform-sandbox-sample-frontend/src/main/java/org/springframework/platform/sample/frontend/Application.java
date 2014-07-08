package org.springframework.platform.sample.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.platform.logging.LoggingListener;
import org.springframework.platform.netflix.circuitbreaker.annotations.EnableCircuitBreaker;
import org.springframework.platform.netflix.feign.FeignConfigurer;
import org.springframework.web.client.RestTemplate;

//import feign.slf4j.Slf4jLogger;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableCircuitBreaker
public class Application extends FeignConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HelloClient helloClient() {
        return loadBalance(HelloClient.class, "http://samplebackendservice");
    }

    @Bean
    public LoggingListener loggingListener() {
        return new LoggingListener();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
