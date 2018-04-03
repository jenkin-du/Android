package com.android.d.intership.util;

import android.util.Base64;

/**图片解析器
 * Created by Administrator on 2016/12/21.
 */
public class ImageParser {

    /**
     *     将加密图片解析成字节数据
     */
    public static byte[] decode(String imageCode){

        return Base64.decode(imageCode,Base64.DEFAULT);
    }

    /**
     * 将字节数组转化为加密数据
     * @param bytes 照片的字节数组
     * @return 加密数据
     */
    public  static String encode(byte[] bytes){
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }




}
