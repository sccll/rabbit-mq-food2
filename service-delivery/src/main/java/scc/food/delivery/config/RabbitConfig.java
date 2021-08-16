package scc.food.delivery.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.delivery.receiver.DeliveryReceiver;
import scc.food.mq.config.DefaultRabbitConfig;
import scc.food.mq.service.TransMessageService;

/**
 * @author celine
 * @since 2021/8/3
 */
@Configuration
public class RabbitConfig extends DefaultRabbitConfig {

    public RabbitConfig(TransMessageService transMessageService, ConnectionFactory connectionFactory) {
        super(transMessageService, connectionFactory);
    }

    @Bean
    DirectExchange deliveryExchange() {
        return new DirectExchange("deliveryExchange");
    }

    @Bean
    public Queue deliveryQueue() {
        return new Queue("deliveryQueue", true);
    }

    @Bean
    Binding bindingRestaurantOrderDirect() {
        return BindingBuilder.bind(deliveryQueue()).to(deliveryExchange()).with("key.delivery");
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                         DeliveryReceiver deliveryReceiver) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("deliveryQueue");
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(deliveryReceiver);
        return container;
    }

}
