package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.HttpThread;
import com.example.lenovo.ptjob_company.com.company.util.CompanyUtil;
/*
* 主界面，登陆
* */
public class MainActivity extends Activity {
    private TextView registryTV;
    private Context context;
    private EditText nameCode;
    private EditText pwd; // 密码
    private Button logButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {

                String response = ((StringBuilder) msg.obj).toString();
                if (!response.equals("NO user")&&!response.equals("Wrong password")) {
                    Toast.makeText(getApplicationContext(), "欢迎", Toast.LENGTH_SHORT).show();

                    Log.i("login", "handleMessage: response="+response);

                    App.ID = response;
                    Intent intent = new Intent(context, MActivity.class);
                    startActivity(intent);
                } else if (response.equals("NO user")) {
                    Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
                } else if (response.equals("Wrong password")) {
                    Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
        context = this;
        registryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegistryActivity.class);
                startActivity(intent);
            }
        });
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = App.url+"CompanyServlet";
                String phone = nameCode.getText().toString();
                String password = pwd.getText().toString();
                App.phoneNumber = phone;
                if (!CompanyUtil.isPhoneNum(phone)) {
                    Toast.makeText(getApplicationContext(), "手机格式不对", Toast.LENGTH_SHORT).show();
                } else if (!CompanyUtil.isPassWord(password)) {
                    Toast.makeText(getApplicationContext(), "密码格式不对", Toast.LENGTH_SHORT).show();
                } else if (CompanyUtil.isPassWord(password) && CompanyUtil.isPhoneNum(phone)) {
                    new HttpThread(url, phone, password, handler).start();

                }
            }
        });


    }

    public void init() {
        registryTV = (TextView) findViewById(R.id.registryTV);
        nameCode = (EditText) findViewById(R.id.nameET);
        pwd = (EditText) findViewById(R.id.passwordET);
        logButton = (Button) findViewById(R.id.lbutton);


    }


}


