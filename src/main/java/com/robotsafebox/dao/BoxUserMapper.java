package com.robotsafebox.dao;

import com.robotsafebox.entity.BoxUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoxUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BoxUser record);

    int insertSelective(BoxUser record);

    BoxUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BoxUser record);

    int updateByPrimaryKey(BoxUser record);

    //    new add
    List<BoxUser> selectBoxUser(@Param("boxId") Long boxId, @Param("type") Byte type, @Param("userId") Long userId);

    int deleteByBoxId(@Param("boxId") Long boxId);

    int deleteByBoxIdAndUserId(@Param("boxId") Long boxId, @Param("userId") Long userId);

}