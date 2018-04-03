package com.example.lenovo.ptjob_company.com.company.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2016/9/12.
 */
public class CompanyUtil {
    public static boolean isPhoneNum(String phone_num) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(14[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phone_num);
        return m.matches();
    }

    public static boolean isPassWord(String password) {
        Pattern p = Pattern
                .compile("\\w{6,12}");
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
