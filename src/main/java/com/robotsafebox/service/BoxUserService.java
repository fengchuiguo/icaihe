package com.robotsafebox.service;

import com.robotsafebox.entity.BoxUser;

import java.util.List;


public interface BoxUserService {

    int saveBoxUser(BoxUser boxUser);

    int delteBoxUser(Long id);

    List<BoxUser> searchBoxUser(Long boxId, Byte type, Long userId);

}
