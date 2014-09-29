package org.springframework.cloud.sample.frontend;

import com.netflix.client.ClientFactory;
import com.netflix.http4.NFHttpClient;
import com.netflix.http4.NFHttpClientFactory;
import com.netflix.niws.client.http.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.FeignConfigurer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//import feign.slf4j.Slf4jLogger;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableHystrix
@EnableEurekaClient
public class Application extends FeignConfigurer {

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
