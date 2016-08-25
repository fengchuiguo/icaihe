package com.robotsafebox.service;

import com.robotsafebox.entity.BoxRecord;

import java.util.List;
import java.util.Map;


public interface BoxRecordService {

    int saveBoxRecord(BoxRecord boxRecord);

    List<Map> searchOpenRecord(Long boxId, String userName);

    List<Map> searchUserRecord(Long userId);

}
