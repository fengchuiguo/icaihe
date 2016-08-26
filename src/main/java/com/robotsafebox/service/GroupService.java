package com.robotsafebox.service;

import com.robotsafebox.entity.Group;

import java.util.List;
import java.util.Map;

public interface GroupService {

    Group getGroup(Long id);

    int saveGroup(Group group);

    Group getGroupByGroupName(String groupName);

    List<Map> searchGroup(String groupName);

    List<Group> searchGroupByUserIdAndMemberType(Long userId,Byte type);

}
