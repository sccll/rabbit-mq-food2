package scc.food.mq.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import scc.food.mq.entity.TransMessage;

import java.util.List;

/**
 * @author Celine
 * @since 2021/08/13
 */
@Mapper
@Repository
public interface TransMessageMapper {

    /**
     * 插入
     *
     * @param transMessage 消息
     */
    @Insert("INSERT INTO trans_message (id, type, service, " +
            "exchange, routing_key, queue, sequence, payload," +
            "date) " +
            "VALUES(#{id}, #{type}, #{service},#{exchange}," +
            "#{routingKey},#{queue},#{sequence}, #{payload},#{date})")
    void insert(TransMessage transMessage);

    /**
     * 更新
     *
     * @param transMessage 反式消息
     */
    @Update("UPDATE trans_message set type=#{type}, " +
            "service=#{service}, exchange =#{exchange},"+
            "routing_key =#{routingKey}, queue =#{queue}, " +
            "sequence =#{sequence}, payload =#{payload}, " +
            "date =#{date} " +
            "where id=#{id} and service=#{service}")
    void update(TransMessage transMessage);


    /**
     * 根据id和服务查询消息
     *
     * @param id      id
     * @param service 服务
     * @return {@link TransMessage}
     */
    @Select("SELECT id, type, service, exchange, " +
            "routing_key routingKey, queue, sequence, " +
            "payload, date " +
            "FROM trans_message " +
            "where id=#{id} and service=#{service}")
    TransMessage selectByIdAndService(@Param("id") String id,
                                      @Param("service") String service);

    /**
     * 根据类型和服务查询消息
     *
     * @param type    类型
     * @param service 服务
     * @return {@link List<TransMessage>}
     */
    @Select("SELECT id, type, service, exchange, " +
            "routing_key routingKey, queue, sequence, " +
            "payload, date " +
            "FROM trans_message " +
            "WHERE type = #{type} and service = #{service}")
    List<TransMessage> selectByTypeAndService(
            @Param("type") String type,
            @Param("service") String service);

    /**
     * 删除
     *
     * @param id      id
     * @param service 服务
     */
    @Delete("DELETE FROM trans_message " +
            "where id=#{id} and service=#{service}")
    void delete(@Param("id") String id,
                @Param("service") String service);
}
