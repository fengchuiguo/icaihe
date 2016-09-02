package com.robotsafebox.framework.push.jpush;


import cn.jiguang.commom.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.robotsafebox.enums.ReportLogActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPushUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(PushExample.class);


    private static final String appKey = "XXXXXX";
    private static final String masterSecret = "XXXXXX";


    public static final String AndroidNotification_TITLE = "i财盒消息";

    public static void main(String[] args) {
        Long alias = 2l;
        String alertContent = "测试内容";
        Integer extraType = 1;
        System.out.println(sendPush(alias, alertContent, extraType));
    }


    public static Boolean sendPush(Long userId, String alertContent, Integer extraType) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_android_and_ios(userId.toString(), alertContent, extraType);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            return result.isResultOK();
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
        return false;
    }

    private static PushPayload buildPushObject_android_and_ios(String alias, String alertContent, Integer extraType) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(alertContent)
                        .addPlatformNotification(AndroidNotification.newBuilder().setTitle(AndroidNotification_TITLE)
                                .addExtra("type", extraType).build())
                        .addPlatformNotification(IosNotification.newBuilder().autoBadge()
                                .addExtra("type", extraType).build())
                        .build())
                .build();
    }

    public static String getAlertContent(String boxName, Integer type) {
        String actioninfo = "";
        for (ReportLogActionEnum reportLogActionEnum : ReportLogActionEnum.values()) {
            if (reportLogActionEnum.getAction() == type) {
                actioninfo = reportLogActionEnum.getInfo();
            }
        }
        if (actioninfo == "") {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("您的财盒").append("”").append(boxName).append("“").append(actioninfo);
        return stringBuffer.toString();
    }

}
