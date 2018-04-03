package com.example.lenovo.ptjob_company.com.Util;


import com.example.lenovo.ptjob_company.com.Model.Datagram;
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
    public static<T> String toJsonDatagram(String request,String response, T t){

        Datagram datagram=new Datagram();
        datagram.setRequest(request);
        datagram.setResponse(response);
        datagram.setJsonStream(JSONParser.toJSONString(t));

        return JSONParser.toJSONString(datagram);
    }

    /**
     * 从json格式的数据包流中获取Javabean对象
     */

    public static<T> T getEntity(String jsonDatagram,Type type){

    	Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken< Datagram>(){}.getType());
    	String jsonStream=datagram.getJsonStream();
    	
        T t=JSONParser.toJavaBean(jsonStream,type);

        
        return t;
    }

    /**
     * 从json格式的数据包流中获取Javabean对象
     */

    public static<T> T getEntity(String jsonDatagram,Class<T> cls){

        Datagram datagram=JSONParser.toJavaBean(jsonDatagram, Datagram.class);
        String jsonStream=datagram.getJsonStream();

        T t=JSONParser.toJavaBean(jsonStream,cls);


        return t;
    }

    /**
     * 从json格式的数据包流中获取请求
     */
    public static String getRequest(String jsonDatagram){

    	Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken<Datagram>(){}.getType());
    	String request=datagram.getRequest();

        return request;
    }

    /**
     * 从json格式的数据包流中获取请求
     */
    public static String getResponse(String jsonDatagram){

        Datagram datagram=JSONParser.toJavaBean(jsonDatagram, new TypeToken<Datagram>(){}.getType());
        String response=datagram.getResponse();

        return response;
    }
}
