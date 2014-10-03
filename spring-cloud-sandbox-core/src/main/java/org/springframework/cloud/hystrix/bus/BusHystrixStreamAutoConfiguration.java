package org.springframework.cloud.hystrix.bus;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Spencer Gibb
 */
@Configuration
@ConditionalOnClass(AmqpTemplate.class)
@EnableScheduling
@IntegrationComponentScan(basePackageClasses = HystrixStreamChannel.class)
//@ConditionalOnExpression("${bus.amqp.enabled:true}")
public class BusHystrixStreamAutoConfiguration {

    public static final String QUEUE_NAME = "spring.cloud.bus.hystrix.stream";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ConfigurableEnvironment env;

    @Bean
    public HystrixStreamTask hystrixStreamTask() {
        return new HystrixStreamTask();
    }

    @Bean
    public DirectChannel hystrixStream() {
        return new DirectChannel();
    }

    @Bean
    public DirectExchange hystrixStreamExchange() {
        //TODO: change to TopicExchange?
        DirectExchange exchange = new DirectExchange(QUEUE_NAME);
        amqpAdmin.declareExchange(exchange);
        return exchange;
    }

    @Bean
    public Queue hystrixStreamQueue() {
        Queue queue = new Queue(QUEUE_NAME);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(hystrixStreamExchange()).with(""));
        return queue;
    }

    //TODO: how to fail gracefully if no rabbit?
    @Bean
    public IntegrationFlow hystrixStreamOutboundFlow() {
        return IntegrationFlows.from("hystrixStream")
                //TODO: set content type
                /*.enrichHeaders(new ComponentConfigurer<HeaderEnricherSpec>() {
                    @Override
                    public void configure(HeaderEnricherSpec spec) {
                        spec.header("content_type", "application/json", true);
                    }
                })*/
                .handle(Amqp.outboundAdapter(this.amqpTemplate).exchangeName(QUEUE_NAME))
                .get();
    }

    @Bean
    protected Queue cloudBusHystrixStreamQueue() {
        Queue queue = new Queue(QUEUE_NAME);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    /*@Bean
    public IntegrationFlow cloudBusInboundFlow(Environment env) {
        ApplicationEventPublishingMessageHandler messageHandler = new ApplicationEventPublishingMessageHandler();
        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, localCloudBusQueue()))
                .handle(messageHandler)
                .get();
    }*/

    @Bean
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
    }
}
