package scc.food.mq.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import scc.food.mq.entity.TransMessage;
import scc.food.mq.service.TransMessageService;

import java.util.List;


/**
 * @author Celine
 * @since 2021/08/13
 * 可以使用xx-job定时任务直接调用resendMessage方法
 */
@EnableScheduling
@Configuration
@Component
@Slf4j
public class ResendTask {

    private final TransMessageService transMessageService;
    private final RabbitTemplate rabbitTemplate;

    @Value("#{new Integer('${scc.mq.resendTimes}')}")
    Integer resendTimes;

    public ResendTask(TransMessageService transMessageService, RabbitTemplate rabbitTemplate) {
        this.transMessageService = transMessageService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelayString = "${scc.mq.resendFreq}")
    public void resendMessage(){
        log.info("resendMessage() invoked.");
        // 所有未发成功的消息
        List<TransMessage> messageList = transMessageService.listReadyMessages();
        log.info("resendMessage(): messageList:{}", messageList);
        for (TransMessage po: messageList) {
            log.info("resendMessage(): po:{}", po);

            if (po.getSequence() > resendTimes){
                log.error("message resend too many times! transMessagePO:{}", po);
                transMessageService.messageDead(po.getId());
                continue;
            }
            // 设置消息属性
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message message = new Message(po.getPayload().getBytes(), messageProperties);
            message.getMessageProperties().setMessageId(po.getId());
            // 发送
            rabbitTemplate.convertAndSend(
                    po.getExchange(),
                    po.getRoutingKey(),
                    message,
                    new CorrelationData(po.getId()));

            log.info("message sent, ID:{}", po.getId());
            // 发消息次数 + 1
            transMessageService.messageResend(po.getId());
        }
    }
}
