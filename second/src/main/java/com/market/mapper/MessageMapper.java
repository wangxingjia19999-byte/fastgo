package com.market.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import com.market.pojo.Message;

@Mapper
public interface  MessageMapper {
    @Select("SELECT * FROM message WHERE goods_id = #{goodsId}")
    Message getMessageByGoodsId(String goodsId);

    @Select("SELECT * FROM message WHERE id = #{id}")
    Message getMessageById(Integer id);

    @Select("SELECT * FROM message")
    List<Message> listMessages();

    @Update("INSERT INTO message (content, goods_id) VALUES (#{content}, #{goodsId})")
    void insertMessage(Message message);

    @Update("UPDATE message SET content = #{content} WHERE id = #{id}")
    void updateMessage(Message message);    
}
