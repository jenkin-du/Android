package com.example.lenovo.ptjob_company.com.Entry;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 2016/9/25.
 */
public class HttpThread extends Thread {
    String url;
    String namecode;
    String pwd;
    Handler handler;
    Message msg;
    public HttpThread(String url, String namecode, String pwd, Handler handler){
        this.url=url;
        this.namecode=namecode;
        this.pwd=pwd;
        this.handler=handler;
    }

    private void doGet(){
        url =url+"?nameCode="+namecode+"&pwd="+pwd;
        URL httpURL;
        HttpURLConnection connection=null;
        try {
             httpURL =  new URL(url);
             Log.i("url", "doGet: "+url);


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
                String str;
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
        }finally {
            connection.disconnect();
        }

    }
    private void doPost(){

        try {
            URL httpURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            OutputStream out = connection.getOutputStream();
            String content = "namecode="+namecode+"&password="+pwd;
            out.write(content.getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str = br.readLine();

            while(str!=null){
                sb.append(str);
            }
            Log.i("tag",sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        doGet();
       // doPost();
    }
}
