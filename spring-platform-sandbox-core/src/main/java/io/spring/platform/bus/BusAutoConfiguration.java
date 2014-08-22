package io.spring.platform.bus;

import io.spring.platform.bus.event.RefreshListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.EnvironmentEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.platform.config.client.RefreshEndpoint;

/**
 * @author Spencer Gibb
 */
@Configuration
public class BusAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(BusAutoConfiguration.class);

    @Bean
    @ConditionalOnClass(RefreshEndpoint.class)
    @ConditionalOnExpression("${bus.refresh.enabled:true}")
    public RefreshListener refreshListener() {
        return new RefreshListener();
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnExpression("${endpoints.bus.enabled:true}")
    protected static class BusEndpointConfiguration {

        @Bean
        BusEndpoint busEndpoint() {
            return new BusEndpoint();
        }

        @Bean
        BusMvcEndpoint busMvcEndpoint(ConfigurableApplicationContext context) {
            return new BusMvcEndpoint(busEndpoint(), context);
        }
    }
}
