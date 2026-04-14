package com.example;

import org.apache.ibatis.annotations.Select;

public class User {
    private int id;
    private String name;
    private String content;
    private int num;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;

    @Select("SELECT id FROM test WHERE id=#{id}")
    public User selectById(int id) {
        return null; // 这里的实现会被MyBatis自动生成
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }
    public java.sql.Timestamp getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(java.sql.Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", num=" + num +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
