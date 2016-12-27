package com.robotsafebox.service.impl;

import com.robotsafebox.dao.BoxWrittenOffMapper;
import com.robotsafebox.entity.BoxWrittenOff;
import com.robotsafebox.service.BoxWrittenOffService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BoxWrittenOffServiceImpl implements BoxWrittenOffService {

    @Resource
    private BoxWrittenOffMapper boxWrittenOffMapper;

    public int saveBoxWrittenOff(BoxWrittenOff boxWrittenOff) {
        if (boxWrittenOff.getId() != null) {
            return boxWrittenOffMapper.updateByPrimaryKeySelective(boxWrittenOff);
        }
        return boxWrittenOffMapper.insertSelective(boxWrittenOff);
    }
}
