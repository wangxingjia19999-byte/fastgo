package com.market.mapper;
import com.market.pojo.Goods;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import org.apache.ibatis.annotations.Update;

@Mapper
public interface GoodsMapper {

    @Select("SELECT * FROM goods")
    List<Goods> listGoods();

    @Select("SELECT * FROM goods WHERE cid = #{cid}")
    List<Goods> listGoodsByCategoryId(String cid);

    @Select("SELECT * FROM goods WHERE id = #{id}")
    Goods getGoodsById(Integer id);

    @Insert("INSERT INTO goods (name, price, description, phone_num, cid, owner, `condition`, created_at) VALUES (#{name}, #{price}, #{description}, #{phoneNum}, #{cid}, #{owner}, #{condition}, #{createdAt})")
    void insertGoods(Goods goods);

    @Update("UPDATE goods SET name = #{name}, price = #{price}, description = #{description}, phone_num = #{phoneNum}, cid = #{cid}, owner = #{owner}, `condition` = #{condition}, created_at = #{createdAt} WHERE id = #{id}")
    void updateGoods(Goods goods);

    @Delete("DELETE FROM goods WHERE id = #{id}")
    void deleteGoods(Integer id);
}
