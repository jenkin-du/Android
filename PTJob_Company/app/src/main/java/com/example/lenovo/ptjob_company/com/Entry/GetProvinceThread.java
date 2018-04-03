package com.example.lenovo.ptjob_company.com.Entry;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2016/11/23.
 */
public class GetProvinceThread extends Thread{
    String url;
    Handler handler;
    Message msg;
    public GetProvinceThread(String url, Handler handler){
        this.url =url;
        this.handler = handler;
    }

    public void doGet() {
        URL httpURL;
        HttpURLConnection connection=null;
        try {
            httpURL =  new URL(url);

            connection= (HttpURLConnection) httpURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

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
                Log.i("province", sb.toString());

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
