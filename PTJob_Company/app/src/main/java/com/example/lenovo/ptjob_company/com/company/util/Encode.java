package com.example.lenovo.ptjob_company.com.company.util;
import java.io.UnsupportedEncodingException;
/**
 * Created by lenovo on 2016/10/21.
 */
public class Encode {
    public static String doEncoding(String string)
    {
        try {
            string= new String(string.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return string;
    }
}
