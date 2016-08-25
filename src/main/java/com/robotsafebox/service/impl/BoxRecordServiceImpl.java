package com.robotsafebox.service.impl;

import com.robotsafebox.dao.BoxRecordMapper;
import com.robotsafebox.entity.BoxRecord;
import com.robotsafebox.service.BoxRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    public List<Map> searchOpenRecord(Long boxId, String userName) {
        return boxRecordMapper.selectOpenRecord(boxId, userName);
    }

    public List<Map> searchUserRecord(Long userId) {
        return boxRecordMapper.selectUserRecord(userId);
    }
}
