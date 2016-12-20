package com.robotsafebox.dao;

import com.robotsafebox.entity.HardwareReportLog;

import java.util.List;
import java.util.Map;

public interface HardwareReportLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HardwareReportLog record);

    int insertSelective(HardwareReportLog record);

    HardwareReportLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HardwareReportLog record);

    int updateByPrimaryKey(HardwareReportLog record);

    List<HardwareReportLog> queryAll();

    //new add

    List<HardwareReportLog> selectRecordByMap(Map map);


}