package org.springframework.cloud.hystrix.bus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * @author Spencer Gibb
 */
@MessageEndpoint
@Slf4j
public class HystrixStreamAggregator {

    @ServiceActivator(inputChannel = "hystrixStreamAggregator")
    public void handle(String payload) {
        log.info("Received hystrix stream payload: {}", payload);
    }

}
