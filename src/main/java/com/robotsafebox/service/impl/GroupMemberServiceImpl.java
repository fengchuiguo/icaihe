package com.robotsafebox.service.impl;

import com.robotsafebox.dao.GroupMemberMapper;
import com.robotsafebox.entity.GroupMember;
import com.robotsafebox.service.GroupMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    @Resource
    private GroupMemberMapper groupMemberMapper;

    public int saveGroupMember(GroupMember groupMember) {
        if (groupMember.getId() != null) {
            return groupMemberMapper.updateByPrimaryKeySelective(groupMember);
        }
        return groupMemberMapper.insertSelective(groupMember);
    }

    public List<Map> searchGroupMemberByGroupId(Long groupId) {
        return groupMemberMapper.selectGroupMemberByGroupId(groupId);
    }

    public GroupMember getGroupMemberByGroupIdAndUserId(Long groupId, Long userId) {
        return groupMemberMapper.selectGroupMemberByGroupIdAndUserId(groupId, userId);
    }

}
