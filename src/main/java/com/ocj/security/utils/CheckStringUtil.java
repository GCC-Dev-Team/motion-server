package com.ocj.security.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckStringUtil {
    public static boolean checkStringLength(String str,int begin,int end) {
        int length = str.length();
        //长度检查，如果满足就返回ture
        return length > begin && length < end;

    }
    public static boolean isSecureString(String str) {
        // 使用正则表达式匹配字母和数字的组合
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d).{9,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        //安全性检查如果满足就返回ture
        return matcher.matches();
    }

    public static boolean isEmail(String str) {
        // 使用正则表达式匹配字母和数字的组合
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        //安全性检查如果满足就返回ture
        return matcher.matches();
    }

    public static boolean isUserName(String str) {
        // 使用正则表达式匹配字母和数字的组合
        String regex = "^[a-zA-Z0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        //安全性检查如果满足就返回ture
        return matcher.matches();
    }
}
