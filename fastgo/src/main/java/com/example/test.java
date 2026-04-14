package com.example;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class test {
    public static void main(String[] args) {
        User newuser = new User();
        newuser.setName("张三");
        newuser.setContent("这是一个测试用户");
        newuser.setNum(42);
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.insert(newuser);
            session.commit();
            System.out.println("Inserted user: " + newuser);

            List<User> selectedUser = mapper.selectAll();
            System.out.println("Selected user: " + selectedUser);
            mapper.delete(newuser.getId());
            session.commit();
            System.out.println("Deleted user with ID: " + newuser.getId());
        }
            catch (Exception e) {
                e.printStackTrace();
            }
        newuser.selectById(newuser.getId()); 
    }
}
