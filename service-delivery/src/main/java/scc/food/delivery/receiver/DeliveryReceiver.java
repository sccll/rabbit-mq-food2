package scc.food.delivery.receiver;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.Delivery;
import scc.food.common.enums.DeliveryStatusEnum;
import scc.food.delivery.service.DeliveryService;
import scc.food.mq.listener.AbstractMessageListener;
import scc.food.mq.sender.TransMessageSender;

import java.util.List;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class DeliveryReceiver extends AbstractMessageListener {

    private final DeliveryService deliveryService;
    private final TransMessageSender transMessageSender;
    ObjectMapper objectMapper = new ObjectMapper();

    public DeliveryReceiver(DeliveryService deliveryService, TransMessageSender transMessageSender) {
        this.deliveryService = deliveryService;
        this.transMessageSender = transMessageSender;
    }


    @Override
    public void receiveMessage(Message message) throws JsonProcessingException {
        // 将一个json转换成实体类
        String messageBody = new String(message.getBody());
        OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                OrderMessageDTO.class);
        // 查找所有可以接单的骑手
        List<Delivery> deliveryList = deliveryService.getBaseMapper().selectList(Wrappers.<Delivery>lambdaQuery()
                .eq(Delivery::getStatus, DeliveryStatusEnum.AVAILABLE.getName()));
        orderMessage.setDeliveryId(deliveryList.get(0).getDeliveryId());
        log.info("onMessage:restaurantOrderMessage:{}", orderMessage);
        // 发送订单确认
        transMessageSender.send("deliveryExchange", "key.order", orderMessage);
    }
}
