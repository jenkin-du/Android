package com.android.milkapp2.util;


import com.android.milkapp2.model.Datagram;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

;

/**
 * 数据包解析器
 * Created by Administrator on 2016/8/26.
 */
public class DatagramParser {

    /**
     * 装包
     */
    public static<T> String toJsonDatagram(String request,String type, Object obj){

        Datagram datagram=new Datagram();
        datagram.setJsonStream(JSONParser.toJSONString(obj));
        datagram.setType(type);
        datagram.setRequest(request);

        return JSONParser.toJSONString(datagram);
    }

    /**
     * 装包
     */
    public static<T> String toJsonDatagram(Datagram datagram){

        return JSONParser.toJSONString(datagram);
    }

    /**
     * 从json格式的数据包流中获取Javabean对象
     */

    public static<T> T getEntity(String jsonDatagram,Type type){

        Datagram datagram=JSONParser.toJavaBean(jsonDatagram,new TypeToken< Datagram>(){}.getType());
        return JSONParser.toJavaBean(datagram.getJsonStream(),type);

    }

    /**
     * 从json格式的数据包流中获取Javabean对象
     */

    public static<T> T getEntity(String jsonDatagram,Class<T> cls){

        Datagram datagram=JSONParser.toJavaBean(jsonDatagram, Datagram.class);
        String jsonStream=datagram.getJsonStream();


        return JSONParser.toJavaBean(jsonStream,cls);
    }

    /**
     * 从json格式的数据包流中获取请求
     */
    public static String getRequest(String jsonDatagram){

    	Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken<Datagram>(){}.getType());

        return datagram.getRequest();
    }

    /**
     * 从json格式的数据包流中获取类型
     */
    public static String getType(String jsonDatagram){

        Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken<Datagram>(){}.getType());

        return datagram.getType();
    }
}
