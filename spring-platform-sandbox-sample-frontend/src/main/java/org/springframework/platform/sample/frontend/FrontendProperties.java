package org.springframework.platform.sample.frontend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
* Created by sgibb on 6/19/14.
*/
@Component
@ConfigurationProperties
public class FrontendProperties {
    private String foo;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }
}
