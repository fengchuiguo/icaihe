package com.robotsafebox.framework.push.PushTask;

import com.robotsafebox.framework.push.jpush.JPushUtils;

import java.util.HashSet;

/**
 * 定时推送，辅助工具类.
 */
public class PushTaskTool {

    private static HashSet<PushTask> pushTaskList = new HashSet<PushTask>(); //需要推送的数据
    private static final String SOUND = "warn.m4a";

    public static void main(String[] args) {
        addTask(new PushTask(10L, "你好！", 2));
        addTask(new PushTask(20L, "我好！", 2));
        addTask(new PushTask(30L, "大家好！", 2));
        addTask(new PushTask(30L, "大家好！", 2));
        updateTask();
        removeTask(20L, 2);
        updateTask();
        updateTask();

        updateTask();
    }


    //新增推送任务
    public static synchronized void addTask(PushTask pushTask) {
        if (!pushTaskList.contains(pushTask)) {
            pushTaskList.add(pushTask);
        }
    }

    //执行推送，更新待推送次数
    public static synchronized void updateTask() {
        if (pushTaskList.size() > 0) {
            HashSet<PushTask> delList = new HashSet<PushTask>();//用来装需要删除的元素
            for (PushTask pushTask : pushTaskList) {
                if (pushTask.getNum() > 0) {
                    Boolean pushOK = JPushUtils.sendPush(pushTask.getAlias(), pushTask.getAlertContent(), pushTask.getExtraType(), SOUND);
                    if (pushOK) {
                        System.out.println("报警定时推送:" + pushTask);
                        if (pushTask.getNum() > 1) {
                            pushTask.setNum(pushTask.getNum() - 1);
                        } else {
                            delList.add(pushTask);
                        }
                    }
                }
            }
            if (delList.size() > 0) {
                pushTaskList.removeAll(delList);
            }
        }
    }

    //取消后续推送
    public static synchronized void removeTask(Long userId, Integer extraType) {
        PushTask pushTask = new PushTask(userId, null, extraType);
        if (pushTaskList.contains(pushTask)) {
            pushTaskList.remove(pushTask);
        }
    }

    public static HashSet<PushTask> getPushTaskList() {
        return pushTaskList;
    }

    public static void setPushTaskList(HashSet<PushTask> pushTaskList) {
        PushTaskTool.pushTaskList = pushTaskList;
    }
}
