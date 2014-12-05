package org.springframework.cloud.sample.frontend;

import com.netflix.client.ClientFactory;
import com.netflix.http4.NFHttpClient;
import com.netflix.http4.NFHttpClientFactory;
import com.netflix.niws.client.http.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.FeignConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//import feign.slf4j.Slf4jLogger;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableCircuitBreaker
@EnableDiscoveryClient
public class Application extends FeignConfiguration {

    @Bean
    public NFHttpClient nfHttpClient() {
        return NFHttpClientFactory.getDefaultClient();
    }

    @Bean
    public RestClient restClient() {
        return (RestClient) ClientFactory.getNamedClient("default");
    }

    @Bean
    public HelloClient helloClient() {
        //return feign().target(HelloClient.class, "http://samplebackendservice");
        //TODO: create proxy for interface if annotated with a marker
        return loadBalance(HelloClient.class, "http://samplebackendservice");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
