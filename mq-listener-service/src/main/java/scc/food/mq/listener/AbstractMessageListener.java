package scc.food.mq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import scc.food.mq.entity.TransMessage;
import scc.food.mq.service.TransMessageService;

import java.io.IOException;

/**
 * @author Celine
 * @since 2021/08/13
 */
@Slf4j
public abstract class AbstractMessageListener implements ChannelAwareMessageListener {

    @Autowired
    private TransMessageService transMessageService;

    @Value("#{new Integer('${scc.mq.resendTimes}')}")
    Integer resendTimes;


    /**
     * 消息处理
     * @param message 消息体
     * @throws JsonProcessingException 异常
     */
    public abstract void receiveMessage(Message message) throws JsonProcessingException;

    @Override
    public void onMessage(Message message, Channel channel) throws IOException, InterruptedException {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();

        // 消息消费之前保存，消息持久化
        TransMessage transMessagePo = transMessageService.messageReceiveReady(
                messageProperties.getMessageId(),
                messageProperties.getReceivedExchange(),
                messageProperties.getReceivedRoutingKey(),
                messageProperties.getConsumerQueue(),
                new String(message.getBody())
        );
        log.info("收到消息{}, 消费次数{}", messageProperties.getMessageId(),transMessagePo.getSequence());

        try {
            // 业务消息处理
            receiveMessage(message);
            // 消费确认ack，成功的回执
            channel.basicAck(deliveryTag , false);
            // 消费成功， 删除消息
            transMessageService.messageReceiveSuccess(messageProperties.getMessageId());
        } catch (Exception e){
            log.error("RabbitMQ 消息消费失败，" + e.getMessage(), e);
            // 重试次数太多，拒绝消费
            if (transMessagePo.getSequence() >= resendTimes){
                channel.basicReject(deliveryTag, false);
            } else {
                // 延时， 重回到队列，重新消费, 按照2的指数级递增
                Thread.sleep((long)(Math.pow(2, transMessagePo.getSequence())) * 1000);
                channel.basicNack(deliveryTag, false, true);
            }
        }
    }
}
