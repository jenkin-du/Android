package com.example.lenovo.ptjob_company.com.Entry;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by lenovo on 2016/11/30.
 */
public class ChangeDescriptionThread extends Thread{
    String url;
    String phone;
    String description;
    Handler handler;
    Message msg;
    public ChangeDescriptionThread(  String url, String phone,
                                  String description,Handler handler){
        this.url=url;
        this.phone=phone;
        this.description=description;

        this.handler=handler;

    }
    public void doGet(){
        URL httpURL;
        HttpURLConnection connection=null;
        try {



            url =url+"?phone="+ phone+"&description=" + URLEncoder.encode(description, "utf-8") ;

            Log.i("url", "doGet: "+url);

            httpURL =  new URL(url);



            connection= (HttpURLConnection) httpURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            Log.i("tag",connection.getResponseCode()+"");
            if(connection.getResponseCode()==200) {

                msg =new Message();
                msg.what=200;


                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str = "";



                StringBuilder sb = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                msg.obj=sb;
                handler.sendMessage(msg);
                Log.i("tag", sb.toString());

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
        }

    }

    @Override
    public void run() {
        doGet();
    }
}
