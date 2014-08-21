package org.springframework.platform.sample.backend;

import io.spring.platform.bus.amqp.RemoteApplicationEvent;
import lombok.Data;

/**
 * @author Spencer Gibb
 */
@Data
public class MessageRemoteAppEvent extends RemoteApplicationEvent {
    private String message;

    public MessageRemoteAppEvent(Object source, String originService, String message) {
        super(source, originService);
        this.message = message;
    }
}
