package com.robotsafebox.service.task;


import com.robotsafebox.framework.push.PushTask.PushTask;
import com.robotsafebox.framework.push.PushTask.PushTaskTool;
import com.robotsafebox.service.BoxRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务：报警推送
 * 每隔一分钟。检查需要推送的
 */

@Component("alertPushTask")
public class AlertPushTask {

    @Autowired
    protected BoxRecordService boxRecordService;

    @Scheduled(cron = "*/30 * * * * ?")  //每隔30秒执行一次
    public void doJob() {
        PushTaskTool.updateTask();
    }

}
