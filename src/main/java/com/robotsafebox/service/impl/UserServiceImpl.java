package com.robotsafebox.service.impl;

import com.robotsafebox.dao.UserMapper;
import com.robotsafebox.entity.User;
import com.robotsafebox.service.UserService;

import javax.annotation.Resource;

public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User getUser(String phone) {
        return userMapper.selectByPhone(phone);
    }

    public int addUser(User user) {
        return userMapper.insertSelective(user);
    }
}
