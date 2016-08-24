package com.robotsafebox.service;

import com.robotsafebox.entity.BoxUser;


public interface BoxUserService {

    int saveBoxUser(BoxUser boxUser);

    int delteBoxUser(Long id);

}
