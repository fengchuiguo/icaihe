package com.robotsafebox.framework.sms;

import com.robotsafebox.framework.properties.PropertiesConfig;
import com.robotsafebox.framework.sms.yunpian.YPSmsSendUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 短信发送
 */
public class SmsSendUtils {

    public static String sendSms(String mobile, String numCode, String customerContent) {
        String text;
        if (StringUtils.isNotBlank(customerContent)) {
            text = customerContent;
        } else {
            text = "您的验证码是" + numCode;
        }
        text = "【i财盒】" + text;
        try {

            YPSmsSendUtils.sendSms(PropertiesConfig.getSmsConfigByKey("yunpian_APIKEY"), text, mobile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
