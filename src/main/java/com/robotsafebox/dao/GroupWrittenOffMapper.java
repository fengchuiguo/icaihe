package com.robotsafebox.dao;

import com.robotsafebox.entity.GroupWrittenOff;

public interface GroupWrittenOffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GroupWrittenOff record);

    int insertSelective(GroupWrittenOff record);

    GroupWrittenOff selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GroupWrittenOff record);

    int updateByPrimaryKey(GroupWrittenOff record);
}