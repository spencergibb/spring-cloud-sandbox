package org.springframework.platform.circuitbreaker;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by sgibb on 6/19/14.
 */
public class CircuitBreakerInterceptor implements MethodInterceptor {

    @Value("${spring.application.name:default}")
    String groupKey;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();

        //TODO: support other configuration methods
        HystrixCommand.Setter setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                .andCommandKey(HystrixCommandKey.Factory.asKey(methodName));

        String name = groupKey + ":" + methodName;
        //TODO: cache these commands?
        ProxiedCommand command = new ProxiedCommand(name, setter, invocation);

        //TODO: support other HystrixExecutable methods: queue (returns Future) & observe (returns Observable)
        return command.execute();
    }

    public class ProxiedCommand extends HystrixCommand<Object> {

        private final String name;
        private final MethodInvocation invocation;

        protected ProxiedCommand(String name, Setter setter, MethodInvocation invocation) {
            super(setter);
            this.name = name;
            this.invocation = invocation;
        }

        @Override
        protected Object run() throws Exception {
            try {
                return invocation.proceed();
            } catch (Exception e) {
                throw e;
            } catch (Throwable t) {
                throw new Exception(t);
            }
        }

        @Override
        protected Object getFallback() {
            //TODO: add fallback support
            /*if (fallback.isPresent()) {
                Supplier supplier = fallback.get();
                return supplier.get();
            }*/
            Throwable e = getFailedExecutionException();
            //LOG.debug("No fallback with exception", e);
            throw new UnsupportedOperationException("No fallback available for "+name, e);
        }
    }
}
