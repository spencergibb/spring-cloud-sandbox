package org.springframework.platform.sample.frontend;

import io.spring.platform.bus.amqp.TestRemoteApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Spencer Gibb
 */
@Component
public class TestRemoteApplicationListener implements ApplicationListener<TestRemoteApplicationEvent> {
    @Override
    public void onApplicationEvent(TestRemoteApplicationEvent event) {
        System.out.println(event);
    }
}
