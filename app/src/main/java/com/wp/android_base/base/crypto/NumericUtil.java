package com.wp.android_base.base.crypto;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * Created by wp on 2019/3/7.
 * <p>
 * 处理字节，2进制转换为16进制的工具
 *
 * 二进制 ：1010 0001
 * 16进制：A1
 * 二进制，每4位转为1位16进制
 *
 *
 */
public class NumericUtil {

    private final static SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String HEX_PREFIX = "0x";

    /**
     * 生成指定位数的字节
     * @param size
     * @return
     */
    public static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    /**
     * 是否是有效16进制
     * @param value
     * @return
     */
    public static boolean isValidHex(String value) {
        if (value == null) {
            return false;
        }
        if (value.startsWith("0x") || value.startsWith("0X")) {
            value = value.substring(2, value.length());
        }

        if (value.length() == 0 || value.length() % 2 != 0) {
            return false;
        }

        String pattern = "[0-9a-fA-F]+";
        return Pattern.matches(pattern, value);
    }

    /**
     * 字节转16进制
     * @param input
     * @return
     */
    public static String bytesToHex(byte[] input) {
        StringBuilder stringBuilder = new StringBuilder();
        if (input.length == 0) {
            return "";
        }

        for (byte anInput : input) {
            //如果位数小于2，则左端补0
            stringBuilder.append(String.format("%02x", anInput));
        }

        return stringBuilder.toString();
    }


    /**
     * 16进制转字节
     * @param input
     * @return
     */
    public static byte[] hexToBytes(String input) {
        String cleanInput = cleanHexPrefix(input);

        int len = cleanInput.length();

        if (len == 0) {
            return new byte[]{};
        }

        byte[] data;
        int startIdx;
        if (len % 2 != 0) {
            data = new byte[(len / 2) + 1];
            data[0] = (byte) Character.digit(cleanInput.charAt(0), 16);
            startIdx = 1;
        } else {
            data = new byte[len / 2];
            startIdx = 0;
        }

        for (int i = startIdx; i < len; i += 2) {
            data[(i + 1) / 2] = (byte) ((Character.digit(cleanInput.charAt(i), 16) << 4)
                    + Character.digit(cleanInput.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 清除16进制前缀
     * @param input
     * @return
     */
    public static String cleanHexPrefix(String input) {
        if (hasHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }

    /**
     * 是否有16进制前缀
     * @param input
     * @return
     */
    private static boolean hasHexPrefix(String input) {
        return input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }
}
