package com.robotsafebox.service.impl;

import com.robotsafebox.dao.BoxUserMapper;
import com.robotsafebox.entity.BoxUser;
import com.robotsafebox.service.BoxUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BoxUserServiceImpl implements BoxUserService {

    @Resource
    private BoxUserMapper boxUserMapper;

    public int saveBoxUser(BoxUser boxUser) {
        if (boxUser.getId() != null) {
            return boxUserMapper.updateByPrimaryKeySelective(boxUser);
        }
        return boxUserMapper.insertSelective(boxUser);
    }

    public int delteBoxUser(Long id) {
        return boxUserMapper.deleteByPrimaryKey(id);
    }

    public List<BoxUser> searchBoxUser(Long boxId, Byte type, Long userId) {
        return boxUserMapper.selectBoxUser(boxId, type, userId);
    }
}
