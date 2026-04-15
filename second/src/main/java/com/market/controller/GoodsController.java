package com.market.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.market.service.GoodsService;
import com.market.pojo.Goods;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping
    public List<Goods> list() {
        return goodsService.listGoods();
    }

    @GetMapping("/{id}")
    public Goods get(@PathVariable int id) {
        return goodsService.getGoodsById(id);
    }

    @PostMapping
    public void create(@RequestBody Goods goods) {
        goodsService.createGoods(goods);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Goods goods) {
        // accept a full Goods JSON (including id) or ignore path id
        goodsService.updateGoods(goods);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        goodsService.deleteGoods(id);
    }

}
