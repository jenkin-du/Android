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
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.ChangePasswordThread;

/**
 * 用于更改公司密码
 * Created by lenovo on 2016/11/27.
 */
public class ChangePasswordActivity extends Activity{
    private EditText oldPasswordET;
    private EditText newpasswordET;
    private EditText confirmnewpassword;
    private Button submitB;
    private Context context=this;
    private Handler changePasswordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        if (msg.what == 200) {

            String response = ((StringBuilder) msg.obj).toString();
            if (response.equals("success")) {
                Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,PersonInfoActivity.class);
                startActivity(intent);
            }else if(response.equals("WrongInitialPassword")){
                Toast.makeText(getApplicationContext(), "原始密码不正确！", Toast.LENGTH_SHORT).show();
            }else if(response.equals("failed")){
                Toast.makeText(getApplicationContext(), "修改失败！", Toast.LENGTH_SHORT).show();
            }
        }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        init();

        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = App.url+"ChangePasswordServlet";
                String oldPassword = oldPasswordET.getText().toString();
                String newPassword = newpasswordET.getText().toString();
                if(newPassword.equals(confirmnewpassword.getText().toString())) {
                    new ChangePasswordThread(url, App.phoneNumber, oldPassword,
                            newPassword, changePasswordHandler).start();
                }else{
                    Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void init(){
        oldPasswordET= (EditText) findViewById(R.id.oldpassword);
        newpasswordET= (EditText) findViewById(R.id.newpassword);
        confirmnewpassword= (EditText) findViewById(R.id.confirmnewpassword);
        submitB= (Button) findViewById(R.id.submitB);
    }
}
