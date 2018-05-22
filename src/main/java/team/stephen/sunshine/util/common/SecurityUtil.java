package team.stephen.sunshine.util.common;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.util.common.LogRecod;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    public static String md5(String input) {
        LogRecod.print(input);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (messageDigest == null) {
            return null;
        }
        //加密密码
        messageDigest.update(input.getBytes());
        byte[] md = messageDigest.digest();
        return byteArrayToHexString(md);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private static String byteToHexString(byte b) {
        int number = b & 0xff;
        return Integer.toHexString(number);
    }

    public static String getEncodedPassword(String rawPassword, String salt) {
        return new SimpleHash("md5", rawPassword,
                ByteSource.Util.bytes(salt), 2).toString();
    }
}
