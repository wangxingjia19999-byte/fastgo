package com.market.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.market.mapper.GoodsMapper;
import com.market.pojo.Goods;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    public List<Goods> listGoods() {
        return goodsMapper.listGoods();
    }

    public List<Goods> listGoodsByCategoryId(String categoryId) {
        return goodsMapper.listGoodsByCategoryId(categoryId);
    }

    public Goods getGoodsById(int id) {
        return goodsMapper.getGoodsById(id);
    }

    public void createGoods(Goods goods) {
        goodsMapper.insertGoods(goods);
    }

    public void updateGoods(Goods goods) {
        goodsMapper.updateGoods(goods);
    }

    public void deleteGoods(int id) {
        goodsMapper.deleteGoods(id);
    }

}
