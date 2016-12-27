package com.robotsafebox.framework.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.UnsupportedEncodingException;

public class PropertiesConfig {

    public static PropertiesConfiguration sysConfig;

    private static PropertiesConfiguration smsConfigProperties;

    private static PropertiesConfiguration pushConfigProperties;

    private static String getConfig(String key) {
        if (sysConfig == null) {
            try {
                sysConfig = new PropertiesConfiguration("config.properties");
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
        return sysConfig.getString(key);
    }

    public static String getSmsConfigByKey(String key) {
        if (smsConfigProperties == null) {
            try {
                smsConfigProperties = new PropertiesConfiguration("sms.properties");
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
        return smsConfigProperties.getString(key);
    }

    public static String getPushConfigByKey(String key) {
        if (pushConfigProperties == null) {
            try {
                pushConfigProperties = new PropertiesConfiguration("push.properties");
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
        return pushConfigProperties.getString(key);
    }

    public static String getImagePath() {
        return getConfig("backgroundVistUrl");
    }

    public static String getConfigByKey(String key) {
        try {
            return new String(getConfig(key).getBytes("iso8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getUploadRoot() {
        return PropertiesConfig.getConfig("uploadRoot");
    }

//  判断是否是生产环境
    public static Boolean isProductionEnvironment() {
        return PropertiesConfig.getConfig("isProductionEnvironment").equals("true");
    }

}
