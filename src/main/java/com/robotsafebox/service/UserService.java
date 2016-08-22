package com.robotsafebox.service;

import com.robotsafebox.entity.User;

public interface UserService {

    User getUser(String phone);

    int addUser(User user);

}
