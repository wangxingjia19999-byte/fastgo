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
    public List<Message> list() {
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
}
