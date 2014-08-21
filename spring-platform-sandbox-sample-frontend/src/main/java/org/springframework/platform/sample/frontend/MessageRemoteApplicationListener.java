package org.springframework.platform.sample.frontend;

import org.springframework.context.ApplicationListener;
import org.springframework.platform.sample.backend.MessageRemoteAppEvent;
import org.springframework.stereotype.Component;

/**
 * @author Spencer Gibb
 */
@Component
public class MessageRemoteApplicationListener implements ApplicationListener<MessageRemoteAppEvent> {
    @Override
    public void onApplicationEvent(MessageRemoteAppEvent event) {
        System.out.println(event);
    }
}
