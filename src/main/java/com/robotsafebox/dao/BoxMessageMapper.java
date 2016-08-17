package com.robotsafebox.dao;

import com.robotsafebox.entity.BoxMessage;

public interface BoxMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BoxMessage record);

    int insertSelective(BoxMessage record);

    BoxMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BoxMessage record);

    int updateByPrimaryKey(BoxMessage record);
}