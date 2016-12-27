package com.robotsafebox.dao;

import com.robotsafebox.entity.Box;

public interface BoxMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Box record);

    int insertSelective(Box record);

    Box selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Box record);

    int updateByPrimaryKey(Box record);

    //new add
    Box selectByIchId(String ichid);

    Box selectByGroupId(Long groupId);

}