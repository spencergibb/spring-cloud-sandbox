package org.springframework.platform.sample.frontend;

import feign.Feign;
//import feign.slf4j.Slf4jLogger;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.platform.circuitbreaker.annotations.EnableCircuitBreaker;
import org.springframework.platform.feign.SpringDecoder;
import org.springframework.platform.feign.SpringEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableCircuitBreaker
public class Application {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    SpringDecoder springDecoder() {
        return new SpringDecoder();
    }

    @Bean
    SpringEncoder springEncoder() {
        return new SpringEncoder();
    }

    @Bean
    public HelloClient helloClient() {
        //TODO: feign configuration
        return Feign.builder()
                .encoder(springEncoder())
                .decoder(springDecoder())
                //.logger(new Slf4jLogger())
                .logger(new Logger.JavaLogger())
                .target(HelloClient.class, "http://localhost:7080");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
