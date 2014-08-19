package io.spring.platform.bus.amqp;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author Spencer Gibb
 */
@Data
public class TestRemoteApplicationEvent extends ApplicationEvent {
    private String message;
    public TestRemoteApplicationEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
