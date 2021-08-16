package scc.food.mq.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Celine
 * @since 2021/08/13
 */
@Builder
@Data
public class TransMessage {

    /**
     * 主键id
     */
    private String id;


    /**
     * 服务
     */
    private String service;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由的关键
     */
    private String routingKey;

    /**
     * 队列
     */
    private String queue;

    /**
     * 消息发送次数
     */
    private Integer sequence;

    /**
     * 消息体
     */
    private String payload;

    /**
     * 日期
     */
    private LocalDateTime date;
}
