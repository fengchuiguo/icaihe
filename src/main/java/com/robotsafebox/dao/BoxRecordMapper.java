package com.robotsafebox.dao;

import com.robotsafebox.entity.BoxRecord;

public interface BoxRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BoxRecord record);

    int insertSelective(BoxRecord record);

    BoxRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BoxRecord record);

    int updateByPrimaryKey(BoxRecord record);
}