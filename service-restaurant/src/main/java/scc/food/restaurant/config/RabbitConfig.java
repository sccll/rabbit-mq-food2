package scc.food.restaurant.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.mq.config.DefaultRabbitConfig;
import scc.food.mq.service.TransMessageService;
import scc.food.restaurant.receiver.RestaurantReceiver;

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
    DirectExchange restaurantExchange() {
        return new DirectExchange("restaurantExchange");
    }

    @Bean
    public Queue restaurantQueue() {
        return new Queue("restaurantQueue");
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(restaurantQueue()).to(restaurantExchange()).with("key.restaurant");
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                         RestaurantReceiver receiver) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("restaurantQueue");
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(receiver);
        return container;
    }

}
