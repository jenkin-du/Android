package com.example.lenovo.ptjob_company.com.Entry;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.lenovo.ptjob_company.com.company.util.Encode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class RegisterThread extends Thread{
    String url;
    String companyName  ;
    String password ;

    String contactName ;
    String contactPhone ;
    String address;
    String description ;

    Handler handler;
    Message msg;
    public RegisterThread( String url,  String companyName  , String password , String contactName ,
            String contactPhone , String address,String description, Handler handler){
        this.url=url;
        this.companyName=companyName;
        this.password=password;
        this.contactName=contactName;
        this.contactPhone=contactPhone;
        this.address=address;

        this.description=description;
        this.handler=handler;
    }
    private void doGet(){


        URL httpURL;
        HttpURLConnection connection=null;
        try {

            Log.i("name------",companyName);
            String s="CompanyName="+companyName;
            url =url+"?companyName="+URLEncoder.encode(companyName,"utf-8")+"&password="+URLEncoder.encode(password,"utf-8")+"&contactName="+URLEncoder.encode(contactName,"utf-8")+
                "&contactPhone="+URLEncoder.encode(contactPhone,"utf-8")+"&address="+URLEncoder.encode(address,"utf-8")+"&description="+URLEncoder.encode(description,"utf-8");
//            url=URLEncoder.encode( url,"UTF-8");
//            url =url+"?"+;
            Log.i("url", "doGet: "+url);URLEncoder.encode(s,"utf-8");
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
        // doPost();
    }
}
