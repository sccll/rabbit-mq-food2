package scc.food.mq.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import scc.food.mq.mapper.TransMessageMapper;
import scc.food.mq.enums.TransMessageTypeEnum;
import scc.food.mq.entity.TransMessage;
import scc.food.mq.service.TransMessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Celine
 * @since 2021/08/13
 * 实现可以使用不同方式： 数据库 / redis ...
 */
@Service
public class TransMessageServiceImpl implements TransMessageService {

    private final TransMessageMapper transMessageDao;

    @Value("${scc.mq.service}")
    String serviceName;

    public TransMessageServiceImpl(TransMessageMapper transMessageDao) {
        this.transMessageDao = transMessageDao;
    }

    @Override
    public TransMessage messageSendReady(String exchange, String routingKey, String body) {
        final String messageId = UUID.randomUUID().toString();
        TransMessage transMessage = TransMessage.builder()
                .id(messageId)
                .service(serviceName)
                .exchange(exchange)
                .routingKey(routingKey)
                .payload(body)
                .date(LocalDateTime.now())
                // 第几次发送
                .sequence(0)
                .type(TransMessageTypeEnum.SEND.getId())
                .build();
        transMessageDao.insert(transMessage);
        return transMessage;
    }

    @Override
    public void messageSendSuccess(String id) {
        transMessageDao.delete(id, serviceName);
    }

    @Override
    public void messageSendReturn(String id, String exchange, String routingKey, String body) {

        messageSendReady(exchange, routingKey, body);
    }

    @Override
    public List<TransMessage> listReadyMessages() {
        return transMessageDao.selectByTypeAndService(TransMessageTypeEnum.SEND.toString(), serviceName);
    }

    @Override
    public void messageResend(String id) {
        TransMessage transMessage = transMessageDao.selectByIdAndService(id, serviceName);
        // 这里会有并发问题， 建议使用分布式锁
        transMessage.setSequence(transMessage.getSequence() + 1);
        transMessageDao.update(transMessage);
    }

    @Override
    public void messageDead(String id) {
        TransMessage transMessage = transMessageDao.selectByIdAndService(id, serviceName);
        transMessage.setType(TransMessageTypeEnum.DEAD.getId());
        transMessageDao.update(transMessage);
    }

    @Override
    public void messageDead(String id, String exchange, String routingKey, String queue, String body) {
        TransMessage transMessage = TransMessage.builder()
                .id(id)
                .service(serviceName)
                .exchange(exchange)
                .routingKey(routingKey)
                .queue(queue)
                .payload(body)
                .date(LocalDateTime.now())
                // 第几次发送
                .sequence(0)
                .type(TransMessageTypeEnum.SEND.getId())
                .build();
        transMessageDao.insert(transMessage);
    }

    @Override
    public TransMessage messageReceiveReady(String id, String exchange, String routingKey, String queue, String body) {

        // 查询是否存在这个队列消息
        TransMessage transMessage = transMessageDao.selectByIdAndService(id, serviceName);
        // null， 第一次消费
        if (Objects.isNull(transMessage)){
            transMessage = TransMessage.builder()
                    .id(id)
                    .service(serviceName)
                    .exchange(exchange)
                    .routingKey(routingKey)
                    .queue(queue)
                    .payload(body)
                    .date(LocalDateTime.now())
                    .sequence(0)
                    .type(TransMessageTypeEnum.RECEIVE.getId())
                    .build();
            transMessageDao.insert(transMessage);
        } else {
            // 不为null, 之前消费过，没有成功。。。这里不会有并发问题，消息每次推送给一个客户端
            transMessage.setSequence(transMessage.getSequence() + 1);
            transMessageDao.update(transMessage);
        }
        return transMessage;
    }

    @Override
    public void messageReceiveSuccess(String id) {
        transMessageDao.delete(id, serviceName);
    }
}
