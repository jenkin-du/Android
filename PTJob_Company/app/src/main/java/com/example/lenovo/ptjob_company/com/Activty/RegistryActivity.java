package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.CheckCompanyThread;
import com.example.lenovo.ptjob_company.com.Entry.RegisterThread;
import com.example.lenovo.ptjob_company.com.company.util.CompanyUtil;

/**
 * 用于注册公司信息
 * Created by lenovo on 2016/9/11.
 */
public class RegistryActivity extends Activity {
    private EditText companyNameET;
    private EditText passwordET;
    private EditText confirmPasswordET;
    private EditText contactNameET;
    private EditText contactPhoneET;
    private EditText addressET;
    private EditText descriptionET;
    private Button submitButton;

    private ImageButton backImageButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {

                Log.i("djkds", "handleMessage: ");
                String response = ((StringBuilder) msg.obj).toString();
                if (response.equals("SUCCEEDED")) {
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                 } else if (response.equals("FAILED")) {
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private Handler checkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {
                final String url = App.url+"RegisterServlet";
                final String companyName = companyNameET.getText().toString();
                final String password = passwordET.getText().toString();
                final  String contactName = contactNameET.getText().toString();
                final  String contactPhone = contactPhoneET.getText().toString();
                final String address = addressET.getText().toString();
                final  String description = descriptionET.getText().toString();


                String response = ((StringBuilder) msg.obj).toString();
                if (response.equals("Contains")) {
                    Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
                } else if (response.equals("Not Contains")) {
                    new RegisterThread(url, companyName, password, contactName, contactPhone, address, description, handler).start();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registry);
        init();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String companyName = companyNameET.getText().toString();
                final String password = passwordET.getText().toString();
                final String confirmPassword = confirmPasswordET.getText().toString();
                final  String contactName = contactNameET.getText().toString();
                final  String contactPhone = contactPhoneET.getText().toString();
                final String address = addressET.getText().toString();
                final  String description = descriptionET.getText().toString();
                 if (companyName.equals("")) {
                    Toast.makeText(getApplicationContext(), "公司名不能为空", Toast.LENGTH_SHORT).show();

                } else if (!CompanyUtil.isPassWord(password) || !CompanyUtil.isPassWord(confirmPassword)) {
                     Toast.makeText(getApplicationContext(), "密码格式不正确", Toast.LENGTH_SHORT).show();

                 } else if (!(password.equals(confirmPassword))) {
                    Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                }else if(contactName.equals("")){
                     Toast.makeText(getApplicationContext(), "请输入联系人姓名", Toast.LENGTH_SHORT).show();

                 } else if (contactPhone.equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入联系方式", Toast.LENGTH_SHORT).show();

                }  else if (!CompanyUtil.isPhoneNum(contactPhone)) {
                     Toast.makeText(getApplicationContext(), "手机格式不正确", Toast.LENGTH_SHORT).show();

                 }  else if(address.equals("")){
                    Toast.makeText(getApplicationContext(), "请输入公司地址", Toast.LENGTH_SHORT).show();

                } else if(description.equals("")){
                    Toast.makeText(getApplicationContext(), "请输入公司简介", Toast.LENGTH_SHORT).show();

                }else if (!companyName.equals("")) {
                    String checkURL = App.url+"CheckCompanyServlet";
                    new CheckCompanyThread(checkURL,contactPhone,checkHandler).start();

                }
            }
        });

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistryActivity.this.finish();
            }
        });
    }

    public void init() {
        companyNameET = (EditText) findViewById(R.id.rnameET);
        passwordET = (EditText) findViewById(R.id.rpasswordET);
        confirmPasswordET = (EditText) findViewById(R.id.rconfirmpasswordET);
        contactNameET = (EditText) findViewById(R.id.rcontactpersonET);
        contactPhoneET = (EditText) findViewById(R.id.rcontactnumberET);
        addressET = (EditText) findViewById(R.id.rcontactaddressET);
        descriptionET = (EditText) findViewById(R.id.rdescriptionET);
        submitButton = (Button) findViewById(R.id.rbutton);
        backImageButton = (ImageButton) findViewById(R.id.backIB);
    }
}
