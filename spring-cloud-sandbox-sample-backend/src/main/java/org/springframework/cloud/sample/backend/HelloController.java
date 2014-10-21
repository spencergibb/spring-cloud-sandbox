package org.springframework.cloud.sample.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sgibb on 6/17/14.
 */
@RestController
public class HelloController {

    @Autowired
    ConfigurableApplicationContext context;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Message hello(@RequestParam(value = "msg", defaultValue = "World") String msg) {
        return new Message(msg);
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public Message sendMessage(@RequestBody Message message) {
        return message;
    }

    @RequestMapping(value = "/remotehello", method = RequestMethod.POST)
    public Message remoteHello(@RequestBody Message message) {
        context.publishEvent(new MessageRemoteAppEvent(this, getAppName(), message.getBody()));
        return message;
    }

    private String getAppName() {
        return context.getEnvironment().getProperty("spring.application.name");
    }
}
