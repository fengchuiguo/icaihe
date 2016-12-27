package com.robotsafebox.dao;

import com.robotsafebox.entity.BoxWrittenOff;

public interface BoxWrittenOffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BoxWrittenOff record);

    int insertSelective(BoxWrittenOff record);

    BoxWrittenOff selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BoxWrittenOff record);

    int updateByPrimaryKey(BoxWrittenOff record);
}