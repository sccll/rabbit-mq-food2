package scc.food.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import scc.food.mq.service.TransMessageService;

/**
 * @author Celine
 * moodymq.dlxEnabled 配置是否监听死信
 */
@Component
@Slf4j
@ConditionalOnProperty("scc.mq.dlxEnabled")
public class DlxListener implements ChannelAwareMessageListener {

    private final TransMessageService transMessageService;

    public DlxListener(TransMessageService transMessageService) {
        this.transMessageService = transMessageService;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String messageBody = new String(message.getBody());
        log.error("dead letter! message:{}", message);
        // 发邮件、打电话、发短信 sendEmail(logKey, messageProperties.getMessageId(), messageBody);
        log.error("sendEmail");
        MessageProperties messageProperties = message.getMessageProperties();

        // 死信入库
        transMessageService.messageDead(
                messageProperties.getMessageId(),
                messageProperties.getReceivedExchange(),
                messageProperties.getReceivedRoutingKey(),
                messageProperties.getConsumerQueue(),
                messageBody
        );
        channel.basicAck(messageProperties.getDeliveryTag(), false);
    }
}
