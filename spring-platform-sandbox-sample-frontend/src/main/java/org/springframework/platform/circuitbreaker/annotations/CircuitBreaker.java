package org.springframework.platform.circuitbreaker.annotations;

import java.lang.annotation.*;

/**
 * Created by sgibb on 6/19/14.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CircuitBreaker {
}
