package com.robotsafebox.dao;

import com.robotsafebox.entity.GroupMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GroupMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GroupMember record);

    int insertSelective(GroupMember record);

    GroupMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GroupMember record);

    int updateByPrimaryKey(GroupMember record);

    //new add
    List<Map> selectGroupMemberByGroupId(@Param("groupId") Long groupId);

    GroupMember selectGroupMemberByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);

    int deleteGroupMemberByGroupId(@Param("groupId") Long groupId);

    int deleteGroupMemberByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);

}