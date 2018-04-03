package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetPersonInfoThread;

/**
 * 企业中心界面
 * Created by lenovo on 2016/10/22.
 */
public class PersonInfoActivity extends Activity {
    private TextView companyName;
    private TextView personName;
    private TextView personPhone;
    private TextView companyDescription;
    private LinearLayout changePasswordLL;
    private LinearLayout changeDescriptionLL;
    private Context context;
    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {

        if (msg.what == 200) {

            String response = ((StringBuilder) msg.obj).toString();
            String str[] = response.split(",");
            String pName = str[0];//联系人姓名
            String cName = str[1];//公司名
            String companyDes = str[2];//公司简介

            if(!pName.equals("")) {
                personName.setText(pName);
            }
            if(!cName.equals("")) {
                companyName.setText(cName);
            }if(!companyDescription.equals("")){
                companyDescription.setText(companyDes);
            }
        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personinfo);
        init();
        context=this;
        String url = App.url+"GetPersonInfoServlet";
        new GetPersonInfoThread(url, App.phoneNumber,handler).start();
        personPhone.setText(App.phoneNumber);
        changePasswordLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChangePasswordActivity.class);
                startActivity(intent);


            }
        });
        changeDescriptionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChangeDescriptionActivity.class);
                startActivity(intent);

            }
        });
    }
    private void init(){
        companyName= (TextView) findViewById(R.id.companyName);
        personName= (TextView) findViewById(R.id.personName);
        personPhone= (TextView) findViewById(R.id.personPhone);
        companyDescription= (TextView) findViewById(R.id.companyDescription);
        changePasswordLL = (LinearLayout) findViewById(R.id.changepasswordLL);
        changeDescriptionLL =  (LinearLayout) findViewById(R.id.changedescriptionLL);
    }
}
