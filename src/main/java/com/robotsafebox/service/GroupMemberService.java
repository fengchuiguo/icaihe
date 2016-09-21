package com.robotsafebox.service;

import com.robotsafebox.entity.GroupMember;

import java.util.List;
import java.util.Map;


public interface GroupMemberService {

    int saveGroupMember(GroupMember groupMember);

    List<Map> searchGroupMemberByGroupId(Long groupId);

    GroupMember getGroupMemberByGroupIdAndUserId(Long groupId, Long userId);

}
