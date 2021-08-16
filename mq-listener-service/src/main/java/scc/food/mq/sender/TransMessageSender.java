package scc.food.mq.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import scc.food.mq.entity.TransMessage;
import scc.food.mq.service.TransMessageService;

/**
 * @author Celine
 * @since 2021/08/13
 */
@Component
@Slf4j
@Configuration
public class TransMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final TransMessageService transMessageService;

    public TransMessageSender(RabbitTemplate rabbitTemplate, TransMessageService transMessageService) {
        this.rabbitTemplate = rabbitTemplate;
        this.transMessageService = transMessageService;
    }

    // @Transactional
    public void send(String exchange, String routingKey, Object body){
        log.info("send(): exchange:{} routingKey:{} body:{}", exchange, routingKey, body);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String payloadStr = mapper.writeValueAsString(body);

            // 发送前暂存消息
            TransMessage transMessage = transMessageService.messageSendReady(exchange, routingKey, payloadStr);

            // 设置消息属性
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message message = new Message(payloadStr.getBytes(), messageProperties);
            message.getMessageProperties().setMessageId(transMessage.getId());
            // 发送
            rabbitTemplate.convertAndSend(exchange, routingKey, message, new CorrelationData(transMessage.getId()));

            log.info("发送消息，消息ID:{}", transMessage.getId());

        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new RuntimeException("发送RabbitMQ消息失败！", e);
        }
    }
}
