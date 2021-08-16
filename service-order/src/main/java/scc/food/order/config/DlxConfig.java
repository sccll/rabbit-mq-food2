package scc.food.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scc.food.mq.listener.DlxListener;

/**
 * @author Celine
 * @since 2021/08/13
 * 死信监听配置
 */
@Configuration
@ConditionalOnProperty("scc.mq.dlxEnabled")
public class DlxConfig {

    /**
     * 声明死信交换机 Topic
     * @return 交换机
     */
    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange("dlxExchange");
    }

    /**
     * 声明死信队列 Topic
     * @return 队列
     * durable持久化
     * exclusive 只能被当前创建的连接使用，而且当连接关闭后队列即被删除
     * autoDelete 当没有生产者或者消费者使用此队列，该队列会自动删除
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue("dlxQueue",true,false,false);
    }

    /**
     * 声明交换机和队列绑定
     * @return binding
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange())
                .with("#");
    }

    @Bean
    public SimpleMessageListenerContainer deadLetterListenerContainer(ConnectionFactory connectionFactory,
                                                                      DlxListener dlxListener) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(dlxQueue());
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(dlxListener);
        // 设置消费者能处理消息的最大个数
        container.setPrefetchCount(100);
        return container;
    }

}
