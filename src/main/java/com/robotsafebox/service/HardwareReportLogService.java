package com.robotsafebox.service;

import com.robotsafebox.entity.HardwareReportLog;

import java.util.List;


public interface HardwareReportLogService {

    int saveHardwareReportLog(HardwareReportLog hardwareReportLog);

    List<HardwareReportLog> getHardwareReportLogList();

}
