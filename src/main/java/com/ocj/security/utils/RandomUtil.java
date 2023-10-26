package com.ocj.security.utils;


import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    /**
     * 生成字符串（String类型）
     * @return
     */
    public static String generateRandomNumberString() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // Generate a random digit between 0 and 9
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }

    /**
     * 生成纯数字，限定位数
     * @param digit
     * @return
     */
    public static String generateRandomNumberString(Integer digit) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(digit);

        for (int i = 0; i < digit; i++) {
            int bit = random.nextInt(digit); // Generate a random digit between 0 and 9
            stringBuilder.append(bit);
        }

        return stringBuilder.toString();
    }

    /**
     * 生成含有字母数字的限定位数的字符串
     * @param length
     * @return
     */
    public static String generateRandomString(Integer length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer.");
        }

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replace("-", "");

        if (length > uuidString.length()) {
            throw new IllegalArgumentException("Desired length is greater than UUID length.");
        }

        return uuidString.substring(0, length);
    }
}
