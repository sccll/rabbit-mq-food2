package scc.food.mq.service;

import scc.food.mq.entity.TransMessage;

import java.util.List;

/**
 * @author Celine
 * @since 2021/08/13
 * 当数据量非常大的时候，可以使用redis实现，而不是使用数据库，声明接口，实现可以使用不同方式
 */
public interface TransMessageService {

    /**
     * 发送前暂存消息， 消息持久化
     *
     * @param exchange exchange
     * @param routingKey routingKey
     * @param body body
     * @return TransMessagePO
     */
    TransMessage messageSendReady(String exchange, String routingKey, String body);

    /**
     * 设置消息发送成功【找到交换机， confirm】，标记消息发送成功，删除/标记状态
     *
     * @param id 消息ID
     */
    void messageSendSuccess(String id);

    /**
     * 设置消息返回 消息没有找到队列
     * @param id id
     * @param exchange  exchange
     * @param routingKey routingKey
     * @param body body
     */
    void messageSendReturn(String id, String exchange, String routingKey, String body);

    /**
     * 查询应发未发消息， 定时任务查询为成功未发送成功
     * @return List<TransMessagePO>
     */
    List<TransMessage> listReadyMessages();

    /**
     * 记录消息发送次数
     * @param id id
     */
    void messageResend(String id);

    /**
     * 消息重发多次，放弃
     * @param id id
     */
    void messageDead(String id);

    /**
     * 消息消费前保存
     *
     * @param id 消费信息id
     * @param exchange 交换机
     * @param routingKey 交换key
     * @param queue 队列
     * @param body 消息体
     * @return TransMessagePo
     */
    TransMessage messageReceiveReady(
            String id,
            String exchange,
            String routingKey,
            String queue,
            String body);

    /**
     * 消息消费成功， 删除
     * @param id id
     */
    void messageReceiveSuccess(String id);

    /**
     * 保存监听到的死信消息
     * @param id id
     * @param exchange 交换机
     * @param routingKey key
     * @param queue 队列
     * @param body 消息体
     */
    void messageDead(String id, String exchange,
                     String routingKey, String queue,
                     String body);
}
