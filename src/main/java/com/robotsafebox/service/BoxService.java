package com.robotsafebox.service;

import com.robotsafebox.entity.Box;


public interface BoxService {

    int saveBox(Box box);

    Box getBox(Long id);

    Box getBoxByIchId(String ichid);

}
