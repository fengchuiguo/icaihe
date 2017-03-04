package com.robotsafebox.framework.push.PushTask;

/**
 * 推送内容实体类
 */
public class PushTask {

    private Long alias;//标识，系统中使用用户id
    private String alertContent;//推送内容
    private Integer extraType;//类型标识
    private Integer num = 3;//定时任务中需推送次数

    public PushTask() {
    }

    public PushTask(Long alias, String alertContent, Integer extraType) {
        this.alias = alias;
        this.alertContent = alertContent;
        this.extraType = extraType;
    }

    public PushTask(Long alias, String alertContent, Integer extraType, Integer num) {
        this.alias = alias;
        this.alertContent = alertContent;
        this.extraType = extraType;
        this.num = num;
    }

    public Long getAlias() {
        return alias;
    }

    public void setAlias(Long alias) {
        this.alias = alias;
    }

    public String getAlertContent() {
        return alertContent;
    }

    public void setAlertContent(String alertContent) {
        this.alertContent = alertContent;
    }

    public Integer getExtraType() {
        return extraType;
    }

    public void setExtraType(Integer extraType) {
        this.extraType = extraType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public int hashCode() {
        return alias.hashCode() + extraType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PushTask) {
            PushTask pushTask = (PushTask) obj;
            return (alias.equals(pushTask.alias) && extraType.equals(pushTask.extraType));
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "[alias:" + alias + ";alertContent:" + alertContent + ";extraType:" + extraType + ";num:" + num + "]";
    }

}
