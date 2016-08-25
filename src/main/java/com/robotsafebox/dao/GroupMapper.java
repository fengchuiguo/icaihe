package com.robotsafebox.dao;

import com.robotsafebox.entity.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);


    //    new add
    Group selectByGroupName(@Param("groupName") String groupName);

    List<Group> selectByLikeGroupName(@Param("groupName") String groupName);

    List<Group> selectByGroupByUserIdAndMemberType(@Param("userId") Long userId, @Param("type") Byte type);


}