package com.ocj.security.utils;

import com.ocj.security.enums.RegexOrderEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCheckStringUtil {
    /**
     * 检查字符串的长度
     *
     * @param str
     * @param begin
     * @param end
     * @return
     */
    public static boolean checkStringLength(String str, int begin, int end) {
        int length = str.length();
        //长度检查，如果满足就返回ture
        return length > begin && length < end;

    }

    public static boolean isSecureString(String str) {
        //判断密码是否符合安全性正则表达式
     return regexCheck(str,RegexOrderEnum.Secure_Password);
    }

    public static boolean isEmail(String str) {
        //判断是否是邮箱
        return regexCheck(str,RegexOrderEnum.Email_Regex);
    }

    public static boolean isUserName(String str) {
        //用户名判断
        return regexCheck(str,RegexOrderEnum.User_Name_Regex);
    }

    public static boolean regexCheck(String str, RegexOrderEnum regexOrderEnum) {
        Pattern pattern = Pattern.compile(regexOrderEnum.getRegexOrder());
        Matcher matcher = pattern.matcher(str);
        //安全性检查如果满足就返回ture
        return matcher.matches();
    }
}
