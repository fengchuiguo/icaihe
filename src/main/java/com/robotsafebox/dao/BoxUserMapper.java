package com.robotsafebox.dao;

import com.robotsafebox.entity.BoxUser;

public interface BoxUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BoxUser record);

    int insertSelective(BoxUser record);

    BoxUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BoxUser record);

    int updateByPrimaryKey(BoxUser record);
}