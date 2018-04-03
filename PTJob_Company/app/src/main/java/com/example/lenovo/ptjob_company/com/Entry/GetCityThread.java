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
 * Created by lenovo on 2016/11/23.
 */
public class GetCityThread extends Thread{
    String url;
    String provinceName;
    Handler handler;
    Message msg;
    public GetCityThread(String url, String provinceName, Handler handler){
        this.url=url;
        this.provinceName=provinceName;
        this.handler=handler;
    }
    public void doGet() {
        URL httpURL;
        HttpURLConnection connection = null;
        try {
            url = url + "?provinceName=" + URLEncoder.encode(provinceName,"UTF-8");
            Log.i("url",url);
            httpURL = new URL(url);


            connection = (HttpURLConnection) httpURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (connection.getResponseCode() == 200) {

                msg = new Message();
                msg.what = 200;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str = "";

                StringBuilder sb = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                msg.obj=sb;
                handler.sendMessage(msg);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
        @Override
        public void run () {
            doGet();
        }

}
