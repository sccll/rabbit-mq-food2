package scc.food.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.OrderHeader;
import scc.food.common.enums.OrderHeaderStatusEnum;
import scc.food.mq.sender.TransMessageSender;
import scc.food.order.service.OrderHeaderService;
import scc.food.order.mapper.OrderHeaderMapper;
import scc.food.order.vo.OrderHeaderCreateVO;

import java.time.LocalDateTime;

/**
 * @author celine
 * @since 2021/8/3
 */
@Service
public class OrderHeaderServiceImpl extends ServiceImpl<OrderHeaderMapper, OrderHeader> implements OrderHeaderService {

    private final TransMessageSender transMessageSender;

    public OrderHeaderServiceImpl(TransMessageSender transMessageSender) {
        this.transMessageSender = transMessageSender;
    }

    @Override
    public void createOrderHeader(OrderHeaderCreateVO orderVo) {
        OrderHeader orderHeader = OrderHeader.builder()
                .accountId(orderVo.getAccountId())
                .address(orderVo.getAddress())
                .productId(orderVo.getProductId())
                .status(OrderHeaderStatusEnum.ORDER_CREATING.getName())
                .date(LocalDateTime.now())
                .build();
        baseMapper.insert(orderHeader);
        // 订单创建完成之后需要给餐厅发送消息，准备餐厅订单数据
        OrderMessageDTO orderMessage = OrderMessageDTO.builder()
                .orderId(orderHeader.getOrderId())
                .productId(orderVo.getProductId())
                .accountId(orderVo.getAccountId())
                .build();
        // 使用分布式框架发送
        transMessageSender.send("restaurantExchange", "key.restaurant", orderMessage);
    }
}
