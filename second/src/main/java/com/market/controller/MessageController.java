package com.market.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.market.mapper.MessageMapper;
import com.market.pojo.Message;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageMapper messageMapper;

    @GetMapping
    public List<Message> list(@RequestParam(required = false) String goodsId) {
        if (goodsId != null && !goodsId.isBlank()) {
            return messageMapper.getMessageByGoodsId(goodsId);
        }
        return messageMapper.listMessages();
    }

    @GetMapping("/{id}")
    public Message get(@PathVariable int id) {
        return messageMapper.getMessageById(id);
    }

    @PostMapping
    public void create(@RequestBody Message message) {
        messageMapper.insertMessage(message);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody Message message) {
        message.setId(id);
        messageMapper.updateMessage(message);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        messageMapper.deleteMessage(id);
    }
}
