package com.robotsafebox.service;

import com.robotsafebox.entity.Box;


public interface BoxService {

    int saveBox(Box box);

    void deleteBox(Long id);

    Box getBox(Long id);

    Box getBoxByIchId(String ichid);

    Box getBoxByGroupId(Long groupId);

}
