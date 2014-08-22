package io.spring.platform.bus;

import io.spring.platform.bus.event.RefreshRemoteApplicationEvent;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Spencer Gibb
 */
public class BusMvcEndpoint implements MvcEndpoint {

    private final ConfigurableEnvironment env;
    private BusEndpoint delegate;
    private ConfigurableApplicationContext context;

    public BusMvcEndpoint(BusEndpoint delegate, ConfigurableApplicationContext context) {
        this.delegate = delegate;
        this.context = context;
        env = context.getEnvironment();
    }

    @RequestMapping(value = "refresh", method = RequestMethod.POST)
    public void refresh(@RequestParam Map<String, String> params) {
        context.publishEvent(new RefreshRemoteApplicationEvent(this, env.getProperty("spring.application.name")));
    }

    @Override
    public String getPath() {
        return "/" + this.delegate.getId();
    }

    @Override
    public boolean isSensitive() {
        return this.delegate.isSensitive();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends Endpoint> getEndpointType() {
        return this.delegate.getClass();
    }
}
