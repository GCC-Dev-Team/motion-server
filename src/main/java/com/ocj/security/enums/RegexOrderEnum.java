package com.ocj.security.enums;

public enum RegexOrderEnum {
    User_Name_Regex("^[a-zA-Z0-9]*$","用户注册用户名判断的正则"),
    Tag_Regex("^[\\u4e00-\\u9fa5a-zA-Z0-9]+$","只能中文英文与数字!"),
    Email_Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$","邮箱判断"),
    Secure_Password("^(?=.*[a-zA-Z])(?=.*\\d).{9,}$","安全密码的正则表达式，包含至少一个字母、至少一个数字，并且总长度大于8位的字符串");

    RegexOrderEnum(String regexOrder, String message) {
        this.regexOrder = regexOrder;
        this.message = message;
    }

    public String getRegexOrder() {
        return regexOrder;
    }

    public String getMessage() {
        return message;
    }

    private String regexOrder;

    private String message;
}
