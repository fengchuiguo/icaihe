package com.robotsafebox.enums;

public enum ReportLogActionEnum {
    WIFI_INIT(1, "WIFI配置成功"),
    GIVE_ALARM(2, "报警"),
    LOW_POWER(3, "电量不足"),
    OPEN_BOX(4, "开箱"),
    CLOSE_BOX(5, "关箱");

    private int action;
    private String info;

    ReportLogActionEnum(int action, String info) {
        this.action = action;
        this.info = info;
    }

    public int getAction() {
        return action;
    }

    public String getInfo() {
        return info;
    }


}
