package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Model.Information;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 显示详细招聘信息
 * Created by lenovo on 2016/12/5.
 */
public class PluralityDetailActivity extends Activity{
    private TextView title;
    private TextView address;
    private TextView date;
    private TextView category;

    private TextView companyname;
    private TextView numberofemployee;
    private TextView payment;
    private TextView genderrequest;

    private TextView detaildate;
    private TextView time;
    private TextView detailaddress;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pluralitydetail);
        init();
        Bundle bundle=getIntent().getBundleExtra("1");
        Information info=bundle.getParcelable("11");

       try {
           title.setText(info.getTitle());//兼职标题

           String allAddress = info.getWorkAddress();
           String[] strs = allAddress.split(",");
           address.setText(strs[1]);//兼职所在区县

           Date startWorkTime = (Date) bundle.getSerializable("Startworktime");
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

           String startTime = sdf.format(startWorkTime);
           date.setText(startTime);//开始工作日期

           category.setText(info.getCategory());//工作种类

           companyname.setText(info.getContactName());//发布公司名

           numberofemployee.setText(info.getRecruitNumber()+"人");//招工人数

           payment.setText(info.getSalary());//薪资

           genderrequest.setText(info.getGenderRequest());//性别要求

           Calendar c = Calendar.getInstance();
           c.setTime(startWorkTime);

           c.add(Calendar.DAY_OF_WEEK, info.getWorkDays());

           Date endDate = c.getTime();
           String endDateStr = sdf.format(endDate);
           detaildate.setText(startTime+"至"+endDateStr);//工作时间

           detailaddress.setText(strs[0]);//详细地址

           time.setText(info.getWorkTime());

           description.setText(info.getDescription());

       }catch (Exception e){
           e.printStackTrace();

        }

    }

    private void init(){
        title = (TextView) findViewById(R.id.title);
        address = (TextView) findViewById(R.id.address);
        date = (TextView) findViewById(R.id.date);
        category = (TextView) findViewById(R.id.category);

        companyname = (TextView) findViewById(R.id.companyName);
        numberofemployee = (TextView) findViewById(R.id.numberofEmployee);
        payment = (TextView) findViewById(R.id.payment);
        genderrequest = (TextView) findViewById(R.id.genderrequest);

        detaildate = (TextView) findViewById(R.id.detaildate);
        detailaddress = (TextView) findViewById(R.id.detailaddress);
        time = (TextView) findViewById(R.id.time);
        description = (TextView) findViewById(R.id.description);



    }
}
