package org.springframework.cloud.hystrix.bus;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Spencer Gibb
 */
@Configuration
@ConditionalOnClass(AmqpTemplate.class)
public class HystrixStreamAutoConfiguration {

    public static final String QUEUE_NAME = "spring.cloud.bus.hystrix.stream";

    @Configuration
    @ConditionalOnExpression("${hystrix.stream.bus.enabled:false}")
    @IntegrationComponentScan(basePackageClasses = HystrixStreamChannel.class)
    @EnableScheduling
    protected static class HystrixStreamBusAutoConfiguration {

        @Autowired
        private AmqpTemplate amqpTemplate;

        @Bean
        public HystrixStreamTask hystrixStreamTask() {
            return new HystrixStreamTask();
        }

        @Bean
        public DirectChannel hystrixStream() {
            return new DirectChannel();
        }

        @ConditionalOnExpression("${hystrix.stream.bus.enabled:true}")
        @Bean
        public IntegrationFlow hystrixStreamOutboundFlow() {
            return IntegrationFlows.from("hystrixStream")
                    //TODO: set content type
                    /*.enrichHeaders(new ComponentConfigurer<HeaderEnricherSpec>() {
                        @Override
                        public void configure(HeaderEnricherSpec spec) {
                            spec.header("content-type", "application/json", true);
                        }
                    })*/
                    .handle(Amqp.outboundAdapter(this.amqpTemplate).exchangeName(QUEUE_NAME))
                    .get();
        }

        /*@Bean
        public DirectChannel wiretapChannel() {
            return MessageChannels.direct().get();
        }

        @Bean
        @GlobalChannelInterceptor(patterns = "hystrixStreamOutboundFlow*")
        public WireTap wireTap() {
            return new WireTap(wiretapChannel());
        }

        @Bean
        public IntegrationFlow loggingFlow() {
            LoggingHandler handler = new LoggingHandler("INFO");
            handler.setShouldLogFullMessage(true);
            return IntegrationFlows.from(wiretapChannel())
                    .handle(handler)
                    .get();
        }*/
    }

    @Configuration
    @ConditionalOnExpression("${hystrix.stream.bus.aggregator.enabled:false}")
    protected static class HystrixStreamAggregatorAutoConfiguration {

        @Autowired
        private ConnectionFactory connectionFactory;

        @Autowired
        private AmqpAdmin amqpAdmin;

        //TODO: how to fail gracefully if no rabbit?
        @Bean
        public DirectExchange hystrixStreamExchange() {
            //TODO: change to TopicExchange?
            DirectExchange exchange = new DirectExchange(QUEUE_NAME);
            amqpAdmin.declareExchange(exchange);
            return exchange;
        }

        @Bean
        public Queue hystrixStreamQueue() {
            Map<String, Object> args = new HashMap<>();
            args.put("x-message-ttl", 60000);
            Queue queue = new Queue(QUEUE_NAME, false, false, false, args);
            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(hystrixStreamExchange()).with(""));
            return queue;
        }

        @Bean
        public IntegrationFlow hystrixStreamAggregatorInboundFlow() {
            return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, hystrixStreamQueue()))
                    .channel("hystrixStreamAggregator")
                    .get();
        }

        @Bean
        public HystrixStreamAggregator hystrixStreamAggregator() {
            return new HystrixStreamAggregator();
        }
    }


}
