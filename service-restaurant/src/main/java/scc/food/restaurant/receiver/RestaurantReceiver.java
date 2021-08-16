package scc.food.restaurant.receiver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
import scc.food.common.dto.OrderMessageDTO;
import scc.food.common.entity.Product;
import scc.food.common.entity.Restaurant;
import scc.food.common.enums.ProductStatusEnum;
import scc.food.common.enums.RestaurantStatusEnum;
import scc.food.mq.listener.AbstractMessageListener;
import scc.food.mq.sender.TransMessageSender;
import scc.food.restaurant.service.ProductService;
import scc.food.restaurant.service.RestaurantService;

/**
 * @author celine
 * @since 2021/8/3
 * 消费者消费消息业务处理
 */
@Slf4j
@Component
public class RestaurantReceiver extends AbstractMessageListener {

    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final TransMessageSender transMessageSender;

    ObjectMapper objectMapper = new ObjectMapper();

    public RestaurantReceiver(RestaurantService restaurantService, ProductService productService, TransMessageSender transMessageSender) {
        this.restaurantService = restaurantService;
        this.productService = productService;
        this.transMessageSender = transMessageSender;
    }

    @Override
    public void receiveMessage(Message message) throws JsonProcessingException {
        // 将一个json转换成实体类
        String messageBody = new String(message.getBody());
        OrderMessageDTO orderMessage = objectMapper.readValue(messageBody,
                OrderMessageDTO.class);

        // 外卖产品信息
        Product product = productService.getById(orderMessage.getProductId());
        log.info("onMessage:product:{}", product);

        // 生产外卖产品的餐厅信息
        Restaurant restaurant = restaurantService.getById(product.getRestaurantId());
        log.info("onMessage:restaurant:{}", restaurant);

        // 餐厅营业中且售卖产品
        if (RestaurantStatusEnum.OPEN.getName().equals(restaurant.getStatus()) && ProductStatusEnum.AVAILABLE.getName().equals(product.getStatus())) {
            orderMessage.setConfirmed(true);
            orderMessage.setPrice(product.getPrice());
        } else {
            orderMessage.setConfirmed(false);
        }
        log.info("sendMessage:restaurantOrderMessage:{}", orderMessage);
        // 发送订单确认
        transMessageSender.send("restaurantExchange", "key.order", orderMessage);
    }
}
