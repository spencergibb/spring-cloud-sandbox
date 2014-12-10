package org.springframework.cloud.sample.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableConfigurationProperties
@Slf4j
public class BackendApplication {

	@Bean
	public MyProps myProps() {
		return new MyProps();
	}

	@Bean
	@RefreshScope
	public MyService myService() {
		String name = myProps().getName();
		log.info("\n\n\n\n\n\n\n\n\n*****************\nUpdating MyService with name: "+name);
		return new MyService(name);
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}

