package com.robotsafebox.framework.tools;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;


/**
 * 打卡签到
 */
public class SignInTool {

    //ichid固定头部前几位
    private static String ICHID_HEAD = "ICH1066";

    //坐标定位限制距离（单位"米"）
    public static double SOGNIN_DISTINCE = 1000;

    private static double EARTH_RADIUS = 6378.137;

    //解析出ichid
    public static String getIchId(String major, String minor) {
        if (StringUtils.isBlank(major) || StringUtils.isBlank(minor)) {
            return "";
        }
        String major_hex = Integer.toHexString(Integer.valueOf(major)).toUpperCase();
        String minor_hex = Integer.toHexString(Integer.valueOf(minor)).toUpperCase();
        String result = ICHID_HEAD + buchong(minor_hex) + buchong(major_hex);
        System.out.println("SignInTool.getIchId: " + result);
        return result;
    }

    //判断是否在使用距离中
    //函数说明：Lat纬度/Lon经度 就是latitude/longitude
    //系统定义：addressX 经度；addressY 纬度。
    public static Boolean checkDistanceOK(String addressX1, String addressY1, String addressX2, String addressY2) {
        if (getDistance(Double.valueOf(addressY1), Double.valueOf(addressX1), Double.valueOf(addressY2), Double.valueOf(addressX2)) <= SOGNIN_DISTINCE) {
            return true;
        }
        return false;
    }

    public static double getDistance(String addressX1, String addressY1, String addressX2, String addressY2) {
        return getDistance(Double.valueOf(addressY1), Double.valueOf(addressX1), Double.valueOf(addressY2), Double.valueOf(addressX2));
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    private static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        System.out.println("SignInTool.getDistance: " + s);
        return s;
    }

    private static String buchong(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //不满4位，前面补0；
        if (str.length() < 4) {
            switch (str.length()) {
                case 3:
                    str = "0" + str;
                    break;
                case 2:
                    str = "00" + str;
                    break;
                case 1:
                    str = "000" + str;
                    break;
            }
        }
        return str;
    }


    public static void main(String[] args) throws DecoderException {
        String test_final_str = "ICH106688000027";
        String major = "39";//27
        String minor = "34816";//8800
//        System.out.println(Integer.toHexString(39).toUpperCase());
//        System.out.println(Integer.toHexString(34816).toUpperCase());
        System.out.println(getIchId(major, minor).equals(test_final_str));

//      测试大致结果：71m
        getDistance(37.480563, 121.467113, 37.480591, 121.467926);

//        getDistance(37.480563, 121.467113, 91.480591, 121.467926);

    }

}
