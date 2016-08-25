package com.robotsafebox.framework.tools;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


/**
 * 开箱协议
 */
public class AgreementTool {

    public static String getOpenBoxAgreement(String key) {
        try {
            return ShakeUseData(Hex.decodeHex(key.toCharArray()));
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String ShakeUseData(byte[] data) {
        byte[] randCmd = new byte[4];
        byte t = 0;

        randCmd[0] = (byte) (data[0] >= 0 ? data[0] : data[0] + 256);
        randCmd[1] = (byte) (data[1] >= 0 ? data[1] : data[1] + 256);
        t = (byte) (data[0] ^ data[1]);
        randCmd[2] = (byte) (t >= 0 ? t : t + 256);
        t = (byte) (data[0] & data[1]);
        randCmd[3] = (byte) (t >= 0 ? t : t + 256);

//        System.out.println("1ranmd== " + randCmd[0] + ", " + randCmd[1] + ", " + randCmd[2] + ", " + randCmd[3]);
//        System.out.println("0ranmd== " + Hex.encodeHexString(randCmd));
        byte[] result = new byte[]{randCmd[2], randCmd[3]};
        return Hex.encodeHexString(result);
    }

    public static void main(String[] args) throws DecoderException {
        String ceshi = "3bdd";
//        e619
        System.out.println(ShakeUseData(Hex.decodeHex(ceshi.toCharArray())));
    }


}
