package com.example;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class MyBatisDemo {
    public static void main(String[] args) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> users = mapper.selectAll();
            for (User user : users) {
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
