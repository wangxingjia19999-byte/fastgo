package com.market.pojo;

import lombok.Data;

@Data
public class Message {
    private Integer id;
    private String content;
    private String goodsId;
    private String type;
    private String author;
    private String time;
}
