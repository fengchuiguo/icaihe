package com.robotsafebox.service.impl;

import com.robotsafebox.dao.HardwareReportLogMapper;
import com.robotsafebox.entity.HardwareReportLog;
import com.robotsafebox.service.HardwareReportLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class HardwareReportLogServiceImpl implements HardwareReportLogService {

    @Resource
    private HardwareReportLogMapper hardwareReportLogMapper;

    public int saveHardwareReportLog(HardwareReportLog hardwareReportLog) {
        return hardwareReportLogMapper.insert(hardwareReportLog);
    }

    public List<HardwareReportLog> getHardwareReportLogList() {
        return hardwareReportLogMapper.queryAll();
    }

}
