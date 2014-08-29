package org.springframework.cloud.sample.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.logging.LoggingListener;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.FeignConfigurer;
import org.springframework.cloud.netflix.hystrix.annotations.EnableHystrix;
import org.springframework.web.client.RestTemplate;

//import feign.slf4j.Slf4jLogger;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableHystrix
@EnableEurekaClient
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
