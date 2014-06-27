package org.springframework.platform.sample.frontend;

import com.netflix.config.ConfigurationManager;
import feign.Feign;
//import feign.slf4j.Slf4jLogger;
import feign.Logger;
import feign.ribbon.LoadBalancingTarget;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.platform.circuitbreaker.annotations.EnableCircuitBreaker;
import org.springframework.platform.feign.SpringDecoder;
import org.springframework.platform.feign.SpringEncoder;
import org.springframework.platform.feign.SpringMvcContract;
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
        //TODO: feign/ribbon configuration

        //TODO: build Spring env/archaius bridge
        ConfigurationManager.getConfigInstance().setProperty("exampleBackend.ribbon.listOfServers", "localhost:7080");
        //exampleBackend.ribbon.NIWSServerListClassName=my.package.MyServerList
        return Feign.builder()
                .encoder(springEncoder())
                .decoder(springDecoder())
                //.logger(new Slf4jLogger())
                .logger(new Logger.JavaLogger())
                .contract(new SpringMvcContract())
                //.target(HelloClient.class, "http://localhost:7080");
                .target(LoadBalancingTarget.create(HelloClient.class, "http://exampleBackend"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
