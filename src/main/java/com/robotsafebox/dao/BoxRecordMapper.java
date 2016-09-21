package com.robotsafebox.dao;

import com.robotsafebox.entity.BoxRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BoxRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BoxRecord record);

    int insertSelective(BoxRecord record);

    BoxRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BoxRecord record);

    int updateByPrimaryKey(BoxRecord record);

    //new add
    List<Map> selectOpenRecord(@Param("boxId") Long boxId, @Param("userName") String userName);

    List<Map> selectOpenRecordByMap(Map map);

    List<Map> selectUserRecord(@Param("userId") Long userId);

    List<Map> selectUserRecordByMap(Map map);

}