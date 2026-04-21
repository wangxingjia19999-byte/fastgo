package com.market.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import com.market.pojo.Message;

@Mapper
public interface  MessageMapper {
    @Select("SELECT * FROM message WHERE goods_id = #{goodsId}")
    List<Message> getMessageByGoodsId(String goodsId);

    @Select("SELECT * FROM message WHERE id = #{id}")
    Message getMessageById(Integer id);

    @Select("SELECT * FROM message")
    List<Message> listMessages();

    @Insert("INSERT INTO message (content, goods_id, type, author, time) VALUES (#{content}, #{goodsId}, #{type}, #{author}, #{time})")
    void insertMessage(Message message);

    @Update("UPDATE message SET content = #{content}, goods_id = #{goodsId}, type = #{type}, author = #{author}, time = #{time} WHERE id = #{id}")
    void updateMessage(Message message);

    @Delete("DELETE FROM message WHERE id = #{id}")
    void deleteMessage(Integer id);
}
