package scc.food.settlement.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.mq.config.DefaultRabbitConfig;
import scc.food.mq.service.TransMessageService;
import scc.food.settlement.receiver.SettlementReceiver;

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
    FanoutExchange settlementOrderExchange() {
        return new FanoutExchange("settlementOrderExchange");
    }

    @Bean
    public Queue settlementQueue() {
        return new Queue("settlementQueue", true);
    }

    @Bean
    Binding bindingRestaurantOrderDirect() {
        return BindingBuilder.bind(settlementQueue()).to(settlementOrderExchange());
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                         SettlementReceiver settlementReceiver) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("settlementQueue");
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(settlementReceiver);
        return container;
    }
}
