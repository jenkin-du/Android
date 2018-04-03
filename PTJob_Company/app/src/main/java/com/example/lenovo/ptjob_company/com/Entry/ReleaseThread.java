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
 * Created by lenovo on 2016/10/26.
 */
public class ReleaseThread extends Thread {
    String url;
    String title;//兼职标题
    String type;//兼职类型
    String datePicker;//选择的日期
    String startTime;//开始时间
    String endTime;//结束时间
    String lastTime;//持续时间
    String numberOfEmployee;//招工人数
    String salary;//薪资
    String salaryUnit;//薪资单位
    String gender;//性别要求
    String experience;//经验要求
    String height;//身高要求
    String description;//工作描述
    String detail;//细节描述
    String contactNameOfRealease;//联系人姓名
    String contactPhoneOfRealease;//联系人号码
   // String addressOfRealease;//工作地址
    String province;
    String city;
    String zone;
    String detailAddress;
    Handler handler;
    Message msg;
    String phone;

    public ReleaseThread(String detailAddress,String province,String city,
                         String zone, String contactNameOfRealease, String contactPhoneOfRealease,
                         String datePicker, String description, String detail,
                         String endTime, String experience, String gender,
                         String height, String lastTime, String numberOfEmployee,
                         String salary, String salaryUnit, String startTime,
                         String title, String type, String url, Handler handler,String phone) {
        this.detailAddress = detailAddress;
        this.province =province;
        this.city=city;
        this.zone=zone;
       // this.addressOfRealease = addressOfRealease;
        this.contactNameOfRealease = contactNameOfRealease;
        this.contactPhoneOfRealease = contactPhoneOfRealease;
        this.datePicker = datePicker;
        this.description = description;
        this.detail = detail;
        this.endTime = endTime;
        this.experience = experience;
        this.gender = gender;
        this.handler = handler;
        this.height = height;
        this.lastTime = lastTime;
        this.numberOfEmployee = numberOfEmployee;
        this.salary = salary;
        this.salaryUnit = salaryUnit;
        this.startTime = startTime;
        this.title = title;
        this.type = type;
        this.url = url;
        this.phone = phone;
    }

    private void doGet() {


        URL httpURL;
        HttpURLConnection connection = null;
        try {


            url = url + "?title=" + URLEncoder.encode(title, "utf-8") +
                    "&type=" + URLEncoder.encode(type, "utf-8") +
                    "&datePicker=" + URLEncoder.encode(datePicker, "utf-8") +
                    "&startTime=" + URLEncoder.encode(startTime, "utf-8") +
                    "&endTime=" + URLEncoder.encode(endTime, "utf-8") +
                    "&lastTime=" + URLEncoder.encode(lastTime, "utf-8")+

                    "&numberOfEmployee=" + URLEncoder.encode(numberOfEmployee, "utf-8") +
                    "&salary=" + URLEncoder.encode(salary, "utf-8") +
                    "&salaryUnit=" + URLEncoder.encode(salaryUnit, "utf-8") +
                    "&gender=" + URLEncoder.encode(gender, "utf-8") +
                    "&experience=" + URLEncoder.encode(experience, "utf-8")+
                    "&description=" + URLEncoder.encode(description, "utf-8")+
                    "&height=" + URLEncoder.encode(height, "utf-8") +
                    "&detail=" + URLEncoder.encode(detail, "utf-8") +
                    "&contactNameOfRealease=" + URLEncoder.encode(contactNameOfRealease, "utf-8") +
                    "&contactPhoneOfRealease=" + URLEncoder.encode(contactPhoneOfRealease, "utf-8") +
                    "&province=" + URLEncoder.encode(province, "utf-8") +
                    "&city=" + URLEncoder.encode(city, "utf-8")   +
                    "&zone=" + URLEncoder.encode(zone, "utf-8")+
                    "&detailAddress=" + URLEncoder.encode(detailAddress, "utf-8")+
                    "&phone=" + URLEncoder.encode(phone, "utf-8");

            httpURL = new URL(url);
            Log.i("httpURL",httpURL.toString());

            connection = (HttpURLConnection) httpURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            Log.i("tag", connection.getResponseCode() + "");
            if (connection.getResponseCode() == 200) {

                msg = new Message();
                msg.what = 200;


                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str = "";


                StringBuilder sb = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                msg.obj = sb;
                handler.sendMessage(msg);
                Log.i("tag", sb.toString());

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
    public void run() {
        doGet();
        // doPost();
    }
}
