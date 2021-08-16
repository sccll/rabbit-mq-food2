package scc.food.settlement.receiver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.Settlement;
import scc.food.common.enums.SettlementStatusEnum;
import scc.food.mq.listener.AbstractMessageListener;
import scc.food.mq.sender.TransMessageSender;
import scc.food.settlement.service.SettlementService;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class SettlementReceiver extends AbstractMessageListener {

    private final SettlementService settlementService;
    private final TransMessageSender transMessageSender;
    ObjectMapper objectMapper = new ObjectMapper();

    public SettlementReceiver(SettlementService settlementService, TransMessageSender transMessageSender) {
        this.settlementService = settlementService;
        this.transMessageSender = transMessageSender;
    }

    @Override
    public void receiveMessage(Message message) throws JsonProcessingException {
        String messageBody = new String(message.getBody());
        // 反序列化
        OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                OrderMessageDTO.class);
        log.info("handleOrderService:orderSettlement:{}", orderMessage);
        Settlement settlement = Settlement.builder()
                .amount(orderMessage.getPrice())
                .date(LocalDateTime.now())
                .orderId(orderMessage.getOrderId())
                .status(SettlementStatusEnum.SUCCESS.getName())
                .transactionId("15791457894564")
                .build();
        settlementService.insertSettlement(settlement);
        orderMessage.setSettlementId(settlement.getSettlementId());
        // 发送订单确认
        transMessageSender.send("settlementExchange", "key.order", orderMessage);
    }
}
