package com.robotsafebox.service.impl;

import com.robotsafebox.dao.*;
import com.robotsafebox.entity.Box;
import com.robotsafebox.entity.Group;
import com.robotsafebox.entity.GroupMember;
import com.robotsafebox.entity.GroupWrittenOff;
import com.robotsafebox.service.BoxService;
import com.robotsafebox.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupMapper groupMapper;
    @Resource
    private GroupMemberMapper groupMemberMapper;
    @Resource
    private GroupWrittenOffMapper groupWrittenOffMapper;
    @Resource
    private BoxMapper boxMapper;
    @Resource
    private BoxUserMapper boxUserMapper;
    @Resource
    private BoxService boxService;

    public Group getGroup(Long id) {
        return groupMapper.selectByPrimaryKey(id);
    }

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

    public List<Map> searchGroup(String groupName) {
        return groupMapper.selectByLikeGroupName(groupName);
    }

    public List<Group> searchGroupByUserIdAndMemberType(Long userId, Byte type) {
        return groupMapper.selectByGroupByUserIdAndMemberType(userId, type);
    }

    //注销群组
    public void groupWrittenOff(Long groupId, Long userId) {
        Group group = groupMapper.selectByPrimaryKey(groupId);
        if (group == null) {
            return;
        }
        //1，插入数据要已注销表
        GroupWrittenOff groupWrittenOff = new GroupWrittenOff();
        //1.1，注销人（现在的逻辑，其实也就是群组创建人），注销时间
        groupWrittenOff.setWrittenOffUserId(userId);
        groupWrittenOff.setWrittenOffTime(new Date());
        //1.2，注销时,群组成员的人员id
        String groupMemberUserIds = "";
        List<Map> mapList = groupMemberMapper.selectGroupMemberByGroupId(groupId);
        if (mapList != null && mapList.size() > 0) {
            for (Map item : mapList) {
                groupMemberUserIds += item.get("userId").toString() + ",";
            }
            if (groupMemberUserIds.endsWith(",")) {
                groupMemberUserIds = groupMemberUserIds.substring(0, groupMemberUserIds.length() - 1);
            }
        }
        groupWrittenOff.setGroupMemberUserIds(groupMemberUserIds);
        //1.3其他属性
        groupWrittenOff.setGroupId(group.getId());
        groupWrittenOff.setGroupName(group.getGroupName());
        groupWrittenOff.setGroupCreateTime(group.getGroupCreateTime());
        groupWrittenOff.setGroupAddress(group.getGroupAddress());
        groupWrittenOff.setAddressX(group.getAddressX());
        groupWrittenOff.setAddressY(group.getAddressY());
        groupWrittenOff.setCreateTime(group.getCreateTime());
        groupWrittenOff.setUpdateTime(group.getUpdateTime());
        //1.4保存
        groupWrittenOffMapper.insertSelective(groupWrittenOff);

        //2，删除群组的所有成员
        groupMemberMapper.deleteGroupMemberByGroupId(groupId);

        //3，删除群组
        groupMapper.deleteByPrimaryKey(groupId);

        //4，注销财盒
        Box box = boxMapper.selectByGroupId(groupId);
        if (box != null && box.getId() != null) {
            boxService.deleteBox(box.getId());
        }

    }

    //退出群组
    public void groupQuit(Long groupId, Long userId) {
        //1，删除与群组的关联
        groupMemberMapper.deleteGroupMemberByGroupIdAndUserId(groupId, userId);
        //2，删除与群组中财盒的关联
        Box box = boxMapper.selectByGroupId(groupId);
        if (box != null) {
            boxUserMapper.deleteByBoxIdAndUserId(box.getId(), userId);
        }
    }

}
