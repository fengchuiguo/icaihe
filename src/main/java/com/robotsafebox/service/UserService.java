package com.robotsafebox.service;

import com.robotsafebox.entity.User;

public interface UserService {

    User getUser(Integer id);

    User getUser(String phone);

    int saveUser(User user);

}
