package com.robotsafebox.service.impl;

import com.robotsafebox.dao.GroupWrittenOffMapper;
import com.robotsafebox.entity.GroupWrittenOff;
import com.robotsafebox.service.GroupWrittenOffService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GroupWrittenOffServiceImpl implements GroupWrittenOffService {

    @Resource
    private GroupWrittenOffMapper groupWrittenOffMapper;

    public int saveGroupWrittenOff(GroupWrittenOff groupWrittenOff) {
        if (groupWrittenOff.getId() != null) {
            return groupWrittenOffMapper.updateByPrimaryKeySelective(groupWrittenOff);
        }
        return groupWrittenOffMapper.insertSelective(groupWrittenOff);
    }
}
