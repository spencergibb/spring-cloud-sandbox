package org.springframework.cloud.hystrix.bus;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * @author Spencer Gibb
 */
@MessagingGateway
public interface HystrixStreamChannel {

    @Gateway(requestChannel = "hystrixStream")
    public String send(String s);
}
