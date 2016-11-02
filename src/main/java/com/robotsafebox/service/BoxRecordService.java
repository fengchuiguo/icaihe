package com.robotsafebox.service;

import com.robotsafebox.entity.BoxRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface BoxRecordService {

    int saveBoxRecord(BoxRecord boxRecord);

    List<Map> searchOpenRecord(Long boxId, String userName);

    List<Map> searchOpenRecordByMap(Map paramMap);

    List<Map> searchAlarmRecordByMap(Map paramMap);

    List<Map> searchUserRecord(Long userId);

    List<Map> searchUserRecordByMap(Map paramMap);

    List<BoxRecord> searchBackTimeIsTodayRecord();

}
