package com.robotsafebox.service.impl;

import com.robotsafebox.dao.UserMapper;
import com.robotsafebox.entity.User;
import com.robotsafebox.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User getUser(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public User getUser(String phone) {
        return userMapper.selectByPhone(phone);
    }

    public int saveUser(User user) {
        if (user.getId() != null) {
            return userMapper.updateByPrimaryKeySelective(user);
        }
        return userMapper.insertSelective(user);
    }

    public User getCreateUserByBoxId(Long boxId) {
        return userMapper.selectCreateUserByBoxId(boxId);
    }

    public User getCreateUserByGroupId(Long groupId) {
        return userMapper.selectCreateUserByGroupId(groupId);
    }
}
