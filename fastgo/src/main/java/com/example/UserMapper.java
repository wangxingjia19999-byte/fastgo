package com.example;

import java.util.List;


public interface UserMapper {
    List<User> selectAll();

    public void insert(User user);

    public void update(User user);

    public void delete(int id);

    public User selectById(int id);
}
