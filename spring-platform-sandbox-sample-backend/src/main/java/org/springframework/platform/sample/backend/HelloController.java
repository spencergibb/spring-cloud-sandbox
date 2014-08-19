package org.springframework.platform.sample.backend;

import io.spring.platform.bus.amqp.TestRemoteApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sgibb on 6/17/14.
 */
@RestController
public class HelloController {

    @Autowired
    ConfigurableApplicationContext context;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Message hello() {
        return new Message("World");
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public Message sendMessage(@RequestBody Message message) {
        context.publishEvent(new TestRemoteApplicationEvent(this, message.getBody()));
        return message;
    }
}
