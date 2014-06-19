package org.springframework.platform.circuitbreaker;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.platform.circuitbreaker.annotations.EnableCircuitBreaker;
import org.springframework.platform.endpoint.HystrixStreamEndpoint;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * Created by sgibb on 6/19/14.
 */
@Configuration
public class CircuitBreakerConfiguration implements ImportAware {

    private AnnotationAttributes enableCircuitBreaker;

    @Bean
    HystrixCommandAspect hystrixCommandAspect() {
        return new HystrixCommandAspect();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CircuitBreakerAdvisor circuitBreakerAdvisor() {
        CircuitBreakerAdvisor advisor = new CircuitBreakerAdvisor();
        advisor.setAdvice(circuitBreakerInterceptor());
        advisor.setOrder(this.enableCircuitBreaker.<Integer>getNumber("order"));
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CircuitBreakerInterceptor circuitBreakerInterceptor() {
        CircuitBreakerInterceptor interceptor = new CircuitBreakerInterceptor();
        return interceptor;
    }

    @Bean
    //TODO: add enable/disable
    public HystrixStreamEndpoint hystrixStreamEndpoint() {
        return new HystrixStreamEndpoint();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableCircuitBreaker = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableCircuitBreaker.class.getName(), false));
        Assert.notNull(this.enableCircuitBreaker,
                "@EnableCircuitBreaker is not present on importing class " + importMetadata.getClassName());
    }

    @Autowired(required=false)
    void setConfigurers(Collection<CircuitBreakerConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException("Only one TransactionManagementConfigurer may exist");
        }
        //TODO: create CircuitBreakerConfigurer API
        CircuitBreakerConfigurer configurer = configurers.iterator().next();
        //this.txManager = configurer.annotationDrivenTransactionManager();
    }
}
