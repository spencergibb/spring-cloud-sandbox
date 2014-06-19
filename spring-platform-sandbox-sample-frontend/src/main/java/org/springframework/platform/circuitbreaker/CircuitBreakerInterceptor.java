package org.springframework.platform.circuitbreaker;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by sgibb on 6/19/14.
 */
public class CircuitBreakerInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //TODO: wire in hystrix
        return invocation.proceed();
    }
}
