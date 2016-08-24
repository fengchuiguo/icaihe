package com.robotsafebox.service.impl;

import com.robotsafebox.dao.BoxRecordMapper;
import com.robotsafebox.entity.BoxRecord;
import com.robotsafebox.service.BoxRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BoxRecordServiceImpl implements BoxRecordService {

    @Resource
    private BoxRecordMapper boxRecordMapper;

    public int saveBoxRecord(BoxRecord boxRecord) {
        if (boxRecord.getId() != null) {
            return boxRecordMapper.updateByPrimaryKeySelective(boxRecord);
        }
        return boxRecordMapper.insertSelective(boxRecord);
    }
}
