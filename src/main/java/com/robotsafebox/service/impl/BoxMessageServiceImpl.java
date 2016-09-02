package com.robotsafebox.service.impl;

import com.robotsafebox.dao.BoxMessageMapper;
import com.robotsafebox.entity.BoxMessage;
import com.robotsafebox.service.BoxMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BoxMessageServiceImpl implements BoxMessageService {

    @Resource
    private BoxMessageMapper boxMessageMapper;

    public int saveBoxMessage(BoxMessage boxMessage) {
        if (boxMessage.getId() != null) {
            return boxMessageMapper.updateByPrimaryKeySelective(boxMessage);
        }
        return boxMessageMapper.insertSelective(boxMessage);
    }

}
