package org.springframework.platform.circuitbreaker;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.platform.circuitbreaker.annotations.CircuitBreaker;

import java.lang.reflect.Method;

/**
 * Created by sgibb on 6/19/14.
 */
public class CircuitBreakerAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    @Override
    public Pointcut getPointcut() {
        return new CircuitBreakerPointcut();
    }

    private static class CircuitBreakerPointcut extends StaticMethodMatcherPointcut {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            AnnotationAttributes ann = AnnotatedElementUtils.getAnnotationAttributes(method, CircuitBreaker.class.getName());
            return ann != null;
        }
    }
}
