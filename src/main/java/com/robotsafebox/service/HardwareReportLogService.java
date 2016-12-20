package com.robotsafebox.service;

import com.robotsafebox.entity.HardwareReportLog;

import java.util.List;
import java.util.Map;


public interface HardwareReportLogService {

    int saveHardwareReportLog(HardwareReportLog hardwareReportLog);

    List<HardwareReportLog> getHardwareReportLogList();

    List<HardwareReportLog> selectRecordByMap(Map map);

}
