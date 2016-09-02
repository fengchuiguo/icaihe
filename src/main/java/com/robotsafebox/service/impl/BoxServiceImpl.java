package com.robotsafebox.service.impl;

import com.robotsafebox.dao.BoxMapper;
import com.robotsafebox.entity.Box;
import com.robotsafebox.service.BoxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BoxServiceImpl implements BoxService {

    @Resource
    private BoxMapper boxMapper;

    public int saveBox(Box box) {
        if (box.getId() != null) {
            return boxMapper.updateByPrimaryKeySelective(box);
        }
        return boxMapper.insertSelective(box);
    }

    public Box getBox(Long id) {
        return boxMapper.selectByPrimaryKey(id);
    }

    public Box getBoxByIchId(String ichid) {
        return boxMapper.selectByIchId(ichid);
    }


}
