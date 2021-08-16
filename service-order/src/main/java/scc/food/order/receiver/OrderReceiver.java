package scc.food.order.receiver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.OrderHeader;
import scc.food.common.enums.OrderHeaderStatusEnum;
import scc.food.mq.listener.AbstractMessageListener;
import scc.food.mq.sender.TransMessageSender;
import scc.food.order.service.OrderHeaderService;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Component
public class OrderReceiver extends AbstractMessageListener {

    private final OrderHeaderService orderHeaderService;

    private final TransMessageSender transMessageSender;
    ObjectMapper objectMapper = new ObjectMapper();

    public OrderReceiver(OrderHeaderService orderHeaderService, TransMessageSender transMessageSender) {
        this.orderHeaderService = orderHeaderService;
        this.transMessageSender = transMessageSender;
    }

    @Override
    public void receiveMessage(Message message) throws JsonProcessingException {
            System.out.println("--------------消费者开始消费--------------");
            // 将一个json转换成实体类
            String messageBody = new String(message.getBody());
            OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);
            // order为生产者传过来的消息
            OrderHeader order = orderHeaderService.getById(orderMessage.getOrderId());

            // 问题？ 所有的服务都绑定订单，我们怎么知道这个消息是哪一个为服务发过来的？
            // 解答： 可以设置类型，通过类型或订单状态判断

            String orderStatus = order.getStatus();
            if (OrderHeaderStatusEnum.ORDER_CREATING.getName().equals(orderStatus)) {
                if (orderMessage.getConfirmed() && null != orderMessage.getPrice()) {
                    // 新创建的订单商家同意了且设定了价格
                    order.setStatus(OrderHeaderStatusEnum.RESTAURANT_CONFIRMED.getName());
                    order.setPrice(orderMessage.getPrice());
                    orderHeaderService.updateById(order);
                    // 商家确认订单， 通知骑手取货
                    transMessageSender.send("deliveryExchange", "key.delivery", orderMessage);
                } else {
                    // 商家不接单， 订单取消
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                    orderHeaderService.updateById(order);
                }
            } else if (OrderHeaderStatusEnum.RESTAURANT_CONFIRMED.getName().equals(orderStatus)) {
                // 如果餐厅已经确认且有骑手接单
                if (null != orderMessage.getDeliveryId()) {
                    order.setStatus(OrderHeaderStatusEnum.DELIVERY_CONFIRMED.getName());
                    order.setDeliveryId(orderMessage.getDeliveryId());
                    orderHeaderService.updateById(order);
                    // 发送结算， 结算使用Fanout扇形分发消息机制， routing.key不重要
                    // 特别注意， fanout分发机制使得 order和settlement不能是同一个交换机Exchange，不然order会接受消息
                    transMessageSender.send("settlementOrderExchange", "key.settlement", orderMessage);
                } else {
                    // 没有骑手接单， 失败
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                    orderHeaderService.updateById(order);
                }
            } else if (OrderHeaderStatusEnum.DELIVERY_CONFIRMED.getName().equals(orderStatus)) {
                // 骑手确认，有结算
                if (null != orderMessage.getSettlementId()) {
                    order.setStatus(OrderHeaderStatusEnum.SETTLEMENT_CONFIRMED.getName());
                    order.setSettlementId(orderMessage.getSettlementId());
                    orderHeaderService.updateById(order);
                    // 结算完成之后，发送积分清算
                    transMessageSender.send("rewardExchange", "key.reward", orderMessage);
                } else {
                    // 没有结算， 失败
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                    orderHeaderService.updateById(order);
                }
            } else if (OrderHeaderStatusEnum.SETTLEMENT_CONFIRMED.getName().equals(orderStatus)) {
                if (null != orderMessage.getRewardId()) {
                    order.setStatus(OrderHeaderStatusEnum.ORDER_CREATED.getName());
                    order.setRewardId(orderMessage.getRewardId());
                } else {
                    // 没有， 失败
                    order.setStatus(OrderHeaderStatusEnum.ORDER_FAILED.getName());
                }
                orderHeaderService.updateById(order);
            }
    }
}
