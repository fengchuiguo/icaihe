package com.robotsafebox.service.impl;

import com.robotsafebox.dao.BoxMapper;
import com.robotsafebox.dao.BoxUserMapper;
import com.robotsafebox.dao.BoxWrittenOffMapper;
import com.robotsafebox.entity.Box;
import com.robotsafebox.entity.BoxUser;
import com.robotsafebox.entity.BoxWrittenOff;
import com.robotsafebox.service.BoxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class BoxServiceImpl implements BoxService {

    @Resource
    private BoxMapper boxMapper;

    @Resource
    private BoxUserMapper boxUserMapper;

    @Resource
    private BoxWrittenOffMapper boxWrittenOffMapper;

    public int saveBox(Box box) {
        if (box.getId() != null) {
            return boxMapper.updateByPrimaryKeySelective(box);
        }
        return boxMapper.insertSelective(box);
    }

    //注销财盒
    public void deleteBox(Long id) {

        Box box = boxMapper.selectByPrimaryKey(id);
        if (box == null) {
            return;
        }
        //备份财盒数据到财盒注销表中 box_written_off
        BoxWrittenOff boxWrittenOff = new BoxWrittenOff();

        boxWrittenOff.setWrittenOffTime(new Date());
        boxWrittenOff.setBoxId(box.getId());

        boxWrittenOff.setIchId(box.getIchId());
        boxWrittenOff.setIbeaconId(box.getIbeaconId());
        boxWrittenOff.setWifiId(box.getWifiId());
        boxWrittenOff.setWifiPassword(box.getWifiPassword());
        boxWrittenOff.setBoxName(box.getBoxName());
        boxWrittenOff.setGroupId(box.getGroupId());
        boxWrittenOff.setCreateTime(box.getCreateTime());
        boxWrittenOff.setUpdateTime(box.getUpdateTime());

        //找出有开箱权限的用户
        String boxUserIds = "";
        List<BoxUser> boxUsers = boxUserMapper.selectBoxUser(id, (byte) 1, null);
        if (boxUsers != null || boxUsers.size() > 0) {
            for (BoxUser boxUser : boxUsers) {
                boxUserIds = boxUserIds + boxUser.getUserId() + ",";
            }
            if (boxUserIds.endsWith(",")) {
                boxUserIds = boxUserIds.substring(0, boxUserIds.length() - 1);
            }
        }
        boxWrittenOff.setBoxUserIds(boxUserIds);

        boxWrittenOffMapper.insertSelective(boxWrittenOff);

        //删除财盒用户关联
        boxUserMapper.deleteByBoxId(id);

        //删除财盒
        boxMapper.deleteByPrimaryKey(id);
    }

    public Box getBox(Long id) {
        return boxMapper.selectByPrimaryKey(id);
    }

    public Box getBoxByIchId(String ichid) {
        return boxMapper.selectByIchId(ichid);
    }

    public Box getBoxByGroupId(Long groupId) {
        return boxMapper.selectByGroupId(groupId);
    }
}
