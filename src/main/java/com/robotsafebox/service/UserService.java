package com.robotsafebox.service;

import com.robotsafebox.entity.User;

public interface UserService {

    User getUser(Long id);

    User getUser(String phone);

    int saveUser(User user);

    User getCreateUserByBoxId(Long boxId);

    User getCreateUserByGroupId(Long groupId);

}
