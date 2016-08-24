package com.robotsafebox.service.impl;

import com.robotsafebox.dao.GroupMapper;
import com.robotsafebox.entity.Group;
import com.robotsafebox.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupMapper groupMapper;

    public int saveGroup(Group group) {
        if (group.getId() != null) {
            return groupMapper.updateByPrimaryKeySelective(group);
        }
        return groupMapper.insertSelective(group);
    }

    public Group getGroupByGroupName(String groupName) {
        if (groupName == null || "".equals(groupName)) {
            return null;
        }
        return groupMapper.selectByGroupName(groupName);
    }

    public List<Group> searchGroup(String groupName) {
        return groupMapper.selectByLikeGroupName(groupName);
    }

}