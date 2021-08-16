package scc.food.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import scc.food.mq.service.TransMessageService;

/**
 * @author celine
 * @since 2021/8/3
 */
@Slf4j
public class DefaultRabbitConfig {

    private final TransMessageService transMessageService;
    private final ConnectionFactory connectionFactory;

    public DefaultRabbitConfig(TransMessageService transMessageService, ConnectionFactory connectionFactory) {
        this.transMessageService = transMessageService;
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public RabbitTemplate customRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("correlationData:{}, ack:{}, cause:{}", correlationData, ack, cause);
            if (ack && null != correlationData) {
                String messageId = correlationData.getId();
                log.info("消息已经正确投递到交换机， id:{}", messageId);
                // 删除消息
                transMessageService.messageSendSuccess(messageId);
            } else {
                log.error("消息投递至交换机失败，correlationData:{}", correlationData);
            }
        });

        rabbitTemplate.setReturnsCallback((returnCallback) -> {
            log.error("消息丢失，exchange没有到queue，消息无法路由；ReturnCallbackMsg，message:{} ,replyCode:{}, replyText:{}, exchange:{}, routingKey:{}",
                    returnCallback.getMessage(), returnCallback.getReplyCode(), returnCallback.getReplyText(), returnCallback.getExchange(), returnCallback.getRoutingKey());
            // 重新持久化消息
            transMessageService.messageSendReturn(
                    returnCallback.getMessage().getMessageProperties().getMessageId(),
                    returnCallback.getExchange(),
                    returnCallback.getRoutingKey(),
                    new String(returnCallback.getMessage().getBody())
            );
        });
        return rabbitTemplate;
    }
}
