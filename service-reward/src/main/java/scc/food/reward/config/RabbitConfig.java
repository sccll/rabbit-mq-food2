package scc.food.reward.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.mq.config.DefaultRabbitConfig;
import scc.food.mq.service.TransMessageService;
import scc.food.reward.receiver.RewardReceiver;

/**
 * @author celine
 * @since 2021/8/3
 */
@Configuration
public class RabbitConfig extends DefaultRabbitConfig {

    public RabbitConfig(TransMessageService transMessageService, ConnectionFactory connectionFactory) {
        super(transMessageService, connectionFactory);
    }

    /**
     * 声明一个交换机，起名 rewardExchange
     */
    @Bean
    TopicExchange rewardExchange() {
        return new TopicExchange("rewardExchange");
    }

    @Bean
    public Queue rewardQueue() {
        // 队列是否持久化 true
        return new Queue("rewardQueue", true);
    }

    @Bean
    Binding bindingRestaurantOrderDirect() {
        return BindingBuilder.bind(rewardQueue()).to(rewardExchange()).with("key.reward");
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                         RewardReceiver rewardReceiver) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("rewardQueue");
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(rewardReceiver);
        return container;
    }

}
