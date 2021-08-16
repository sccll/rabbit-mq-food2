package scc.food.mq.enums;

import lombok.Getter;

/**
 * @author Celine
 * @since 2021/08/13
 */
@Getter
public enum TransMessageTypeEnum {

    /**
     * 发送者消息
     */
    SEND(1, "发送者消息"),

    /**
     * 消费者消息
     */
    RECEIVE(2, "消费者消息"),

    /**
     * 死信消息
     */
    DEAD(2, "死信消息");

    private Integer id;

    private String name;

    TransMessageTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
