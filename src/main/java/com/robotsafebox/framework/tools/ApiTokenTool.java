package com.robotsafebox.framework.tools;

import com.robotsafebox.framework.properties.Constant;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;

public class ApiTokenTool {

    private static Cipher cipher_ENCRYPT;
    private static Cipher cipher_DECRYPT;

    // 一个类可以使用不包含在任何方法体中的静态代码块，当类被载入时，静态代码块被执行，且只被执行一次，静态块常用来执行类属性的初始化。
    static {
        try {
            //生成KEY
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //key转换
            Key key = new SecretKeySpec(keyBytes, "AES");

            //加密
            cipher_ENCRYPT = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher_ENCRYPT.init(Cipher.ENCRYPT_MODE, key);

            //解密
            cipher_DECRYPT = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher_DECRYPT.init(Cipher.DECRYPT_MODE, key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getToken(String userId) {
        return encrypt(userId);
    }

    public static String getUserIdByToken(String encryptStr) {
        if (StringUtils.isBlank(encryptStr)) {
            return null;
        }
        String result = desrypt(encryptStr);
        if (result == null) {
            return null;
        }
        if (!result.contains(Constant.API_TOKEN_SPLIT)) {
            return null;
        }
        String[] checks = result.split(Constant.API_TOKEN_SPLIT);
        if (checks.length != 3) {
            return null;
        }
        if (!checks[0].equals(Constant.API_TOKEN_SALT)) {
            return null;
        }
        long oldtime = Long.valueOf(checks[1]);
        //yy/1000/60/60/24:相差多少天
        if (((System.currentTimeMillis() - oldtime) / 1000 / 60 / 60 / 24) > Constant.API_TOKEN_DAY) {
            return null;
        }
//        System.out.println("ApiTokenTool getUserId : " + checks[2]);
        return checks[2];
    }

    public static void main(String[] args) {
        String encryptStr = getToken("12");
        getUserIdByToken(encryptStr);
//       demo: iCaiHe_1471251922377_12
    }


    private ApiTokenTool() {
    }

    //加密
    private static String encrypt(String str) {
        byte[] result;
        try {
            str = Constant.API_TOKEN_SALT + Constant.API_TOKEN_SPLIT + System.currentTimeMillis() + Constant.API_TOKEN_SPLIT + str;
            result = cipher_ENCRYPT.doFinal(str.getBytes(Constant.UTF_8));
            System.out.println("ApiTokenTool encrypt : " + URLEncoder.encode(Base64.encodeBase64String(result), Constant.UTF_8));
            return URLEncoder.encode(Base64.encodeBase64String(result), Constant.UTF_8);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("ApiTokenTool encrypt : null");
        return null;
    }

    //解密
    private static String desrypt(String encryptStr) {
        byte[] result = null;
        try {


            System.out.println("ApiTokenTool--TEST--encryptStr: " + encryptStr);
            try {
                System.out.println("ApiTokenTool--TEST--URLDecoder.decode: " + URLDecoder.decode(encryptStr, Constant.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("ApiTokenTool--TEST--Base64.decodeBase64: " + Base64.decodeBase64(encryptStr));

//            result = cipher_DECRYPT.doFinal(Base64.decodeBase64(encryptStr));

//          说明：SpringMvc获取参数时候，对于URL拼接的参数，获取后好像自己执行过了URLDecoder.decode，MAC上postman会附加参数到url，所以会自动转换
//          但是对于普通post和get请求，token被当做传递的参数的时候，不会处理，需要自己转换一下。
            result = cipher_DECRYPT.doFinal(Base64.decodeBase64(URLDecoder.decode(encryptStr, Constant.UTF_8)));

        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("ApiTokenTool desrypt : " + (result == null ? null : new String(result)));
        return result == null ? null : new String(result);
    }

//    注意：
//    加密后的byte数组是不能强制转换成字符串的, 换言之,字符串和byte数组在这种情况下不是互逆的,
//    要避免这种情况，我们需要做一些修订，可以考虑将二进制数据转换成十六进制表示.

}
