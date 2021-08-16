package scc.food.reward.receiver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.Reward;
import scc.food.common.enums.RewardStatusEnum;
import scc.food.mq.listener.AbstractMessageListener;
import scc.food.mq.sender.TransMessageSender;
import scc.food.reward.service.RewardService;

import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class RewardReceiver extends AbstractMessageListener {

    private final RewardService rewardService;
    private final TransMessageSender transMessageSender;
    ObjectMapper objectMapper = new ObjectMapper();

    public RewardReceiver(RewardService rewardService, TransMessageSender transMessageSender) {
        this.rewardService = rewardService;
        this.transMessageSender = transMessageSender;
    }

    @Override
    public void receiveMessage(Message message) throws JsonProcessingException {
        // 将一个json转换成实体类
        String messageBody = new String(message.getBody());
        OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                OrderMessageDTO.class);
        Reward reward = Reward.builder()
                .orderId(orderMessage.getOrderId())
                .status(RewardStatusEnum.SUCCESS.getName())
                .amount(orderMessage.getPrice())
                .date(LocalDateTime.now())
                .build();
        rewardService.insertReward(reward);
        orderMessage.setRewardId(reward.getRewardId());
        log.info("handleOrderService:settlementOrder:{}", orderMessage);
        // 发送订单确认
        transMessageSender.send("rewardExchange", "key.order", orderMessage);
    }
}
