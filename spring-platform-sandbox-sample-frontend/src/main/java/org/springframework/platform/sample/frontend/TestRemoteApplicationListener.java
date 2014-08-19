package org.springframework.platform.sample.frontend;

import io.spring.platform.bus.amqp.RemoteApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Spencer Gibb
 */
@Component
public class TestRemoteApplicationListener implements ApplicationListener<RemoteApplicationEvent> {
    @Override
    public void onApplicationEvent(RemoteApplicationEvent event) {
        System.out.println(event);
    }
}
