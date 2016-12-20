package com.robotsafebox.service.task;


import com.robotsafebox.entity.BoxMessage;
import com.robotsafebox.entity.BoxRecord;
import com.robotsafebox.framework.push.jpush.JPushUtils;
import com.robotsafebox.framework.utils.DateUtil;
import com.robotsafebox.service.BoxRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务：归还提醒
 * 每天上午九点 推送 当日“外借”到期需要归还物品的人
 */

@Component("returnReminderTask")
public class ReturnReminderTask {

    @Autowired
    protected BoxRecordService boxRecordService;


    @Scheduled(cron = "0 0 9 ? * *")  //每天上午九点
    public void doJob() {
        System.out.println("【returnReminderTask定时任务开始执行...】");

        int recordNum = 0;
        int pushSuccessNum = 0;

        //1查找出今天外借到期的记录
        List<BoxRecord> boxRecordList = boxRecordService.searchBackTimeIsTodayRecord();
        if (boxRecordList != null && boxRecordList.size() > 0) {
            for (BoxRecord boxRecord : boxRecordList) {
                //2插入到用户动态记录中
                BoxRecord newBoxRecord = new BoxRecord();
                newBoxRecord.setBoxId(boxRecord.getBoxId());
                newBoxRecord.setUserId(boxRecord.getUserId());
                newBoxRecord.setType((byte) (6));
                newBoxRecord.setCreateTime(DateUtil.getCurrentDateTime());
                newBoxRecord.setRemark("(外借归还提醒)" + boxRecord.getRemark());
                boxRecordService.saveBoxRecord(newBoxRecord);
                recordNum++;

                //3消息推送
                Boolean pushOK = JPushUtils.sendPush(boxRecord.getUserId(), "您有外借物品需要今天归还。", 6);
                if (pushOK) {
                    pushSuccessNum++;
                }
            }
        }

        System.out.println("【returnReminderTask定时任务：recordNum(" + recordNum + "),pushSuccessNum(" + pushSuccessNum + ")】");
        System.out.println("【returnReminderTask定时任务执行完毕...】");
    }

}

 /*
        1 Seconds (0-59)
        2 Minutes (0-59)
        3 Hours (0-23)
        4 Day of month (1-31)
        5 Month (1-12 or JAN-DEC)
        6 Day of week (1-7 or SUN-SAT)
        7 Year (1970-2099)
        取值：可以是单个值，如6；
            也可以是个范围，如9-12；
            也可以是个列表，如9,11,13
            也可以是任意取值，使用*
    */
//0 * * * * * 代表每分钟执行一次

//一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素。依次顺序为：
//
//        秒（0~59）
//        分钟（0~59）
//        小时（0~23）
//        天（月）（0~31，需要考虑月的天数）
//        月（0~11）
//        天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
//        年份（1970－2099）
//        常用符号代表意思：
//
//        * 字符代表所有可能的值。因此，* 在子表达式（月）里表示每个月的含义，* 在子表达式（天（星期））表示星期的每一天
//        ？字符仅被用于天（月）和天（星期）两个子表达式，表示不指定值当2个子表达式其中之一被指定了值以后，为了避免冲突，需要将另一个子表达式的值设为“？”
//        / 字符表示起始时间开始触发，然后每隔固定时间触发一次，例如在Minutes域使用5/20,则意味着5分钟触发一次
//
//几个示例：
//每天的凌晨4点 @Scheduled(cron = "0 0 4 * * *")
//每周日的凌晨5点  @Scheduled(cron = "0 0 5 * * SUN")
//每月第一天凌晨2点10分 @Scheduled(cron="0 10 02 01 * ?")
//每天从下午2点开始到2点55分结束每隔5分钟触发一次 @Schedule(cron = "0 0/5 14 * * ?")

//CRON表达式    含义
//"0 0 12 * * ?"    每天中午十二点触发
//"0 15 10 ? * *"    每天早上10：15触发
//"0 15 10 * * ?"    每天早上10：15触发
//"0 15 10 * * ? *"    每天早上10：15触发
//"0 15 10 * * ? 2005"    2005年的每天早上10：15触发
//"0 * 14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发
//"0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发
//"0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
//"0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发
//"0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发
//"0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发