package io.spring.platform.bus.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.event.outbound.ApplicationEventPublishingMessageHandler;
import org.springframework.platform.config.client.RefreshEndpoint;
import org.springframework.platform.context.restart.RestartEndpoint;

/**
 * @author Spencer Gibb
 */
@Configuration
public class AmqpBusAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AmqpBusAutoConfiguration.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private ConfigurableEnvironment env;

    @Autowired(required = false)
    private RefreshEndpoint refreshEndpoint;

    @Autowired(required = false)
    private RestartEndpoint restartEndpoint;
    private int port;

    //TODO: how to fail gracefully if no rabbit?
    @Bean
    ApplicationListener<EmbeddedServletContainerInitializedEvent> servletInitListener() {
        return new ApplicationListener<EmbeddedServletContainerInitializedEvent>() {
            @Override
            public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
                port = event.getEmbeddedServletContainer().getPort();
            }
        };
    }

    @Bean
    protected FanoutExchange platformBusExchange() {
        //TODO: change to TopicExchange?
        FanoutExchange exchange = new FanoutExchange("spring.platform.bus");
        amqpAdmin.declareExchange(exchange);
        return exchange;
    }

    @Bean
    protected Queue localPlatformBusQueue() {
        Queue queue = amqpAdmin.declareQueue();
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(platformBusExchange()));
        return queue;
    }

    @Bean
    @ConditionalOnExpression("${spring.platform.bus.producer:false}")
    public ApplicationEventListeningMessageProducer platformBusProducer() {
        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
        producer.setEventTypes(TestRemoteApplicationEvent.class);
        producer.setOutputChannel(new DirectChannel()); //FIXME: hack
        return producer;
    }

    @Bean
    @ConditionalOnExpression("${spring.platform.bus.producer:false}")
    public IntegrationFlow platformBusOutboundFlow() {
        return IntegrationFlows.from(platformBusProducer())
                .handle(Amqp.outboundAdapter(this.rabbitTemplate)
                    .exchangeName("spring.platform.bus"))
                .get();
    }

    @Bean
    @ConditionalOnExpression("${spring.platform.bus.consumer:true}")
    public IntegrationFlow platformBusInboundFlow() {
        ApplicationEventPublishingMessageHandler messageHandler = new ApplicationEventPublishingMessageHandler();
        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, localPlatformBusQueue().getName()))
            .handle(messageHandler)
            .get();
    }
}
