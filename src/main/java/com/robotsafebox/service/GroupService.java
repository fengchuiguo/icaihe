package com.robotsafebox.service;

import com.robotsafebox.entity.Group;

import java.util.List;

public interface GroupService {

    int saveGroup(Group group);

    Group getGroupByGroupName(String groupName);

    List<Group> searchGroup(String groupName);

    List<Group> searchGroupByUserIdAndMemberType(Long userId,Byte type);

}
