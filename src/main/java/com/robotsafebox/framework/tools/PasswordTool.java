package com.robotsafebox.framework.tools;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 密码：此处使用2次md5加密。加密方式可以根据需要修改。
 */
public class PasswordTool {

    private static int hashIterations = 2;

    public static String encryptPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return null;
        }
        for (int i = 0; i < hashIterations; i++) {
            password = DigestUtils.md5Hex(password.getBytes());
        }
        return password;
    }

    public static void main(String[] strings) {
        System.out.println(encryptPassword("ich666"));
//      cf3061f0abf1d19a9a168694356f1e26
    }


}