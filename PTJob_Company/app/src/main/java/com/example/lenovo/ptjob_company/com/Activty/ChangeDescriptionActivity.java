package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.ChangeDescriptionThread;

/**
 * 用于更改公司简介,实现TextWatcher接口
 * Created by lenovo on 2016/11/29.
 */
public class ChangeDescriptionActivity extends Activity implements TextWatcher{
    private TextView text;
    private EditText edit;
    private Button saveButton;
    private Context context=this;
    private Handler changeDescriptionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        if (msg.what == 200) {
            String response = ((StringBuilder) msg.obj).toString();
            if (response.equals("success")) {
                Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,PersonInfoActivity.class);
                startActivity(intent);
            }else if(response.equals("failed")){
                Toast.makeText(getApplicationContext(), "修改失败！", Toast.LENGTH_SHORT).show();
            }
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changedescription);
        init();
    }

    public void  init(){
        text= (TextView) findViewById(R.id.text);
        edit = (EditText) findViewById(R.id.edit);
        //添加监听，统计还可以输入多少字，上限为200个
        edit.addTextChangedListener(this);
        saveButton = (Button) findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = App.url+"ChangeDescriptionServlet";
                new ChangeDescriptionThread(url, App.phoneNumber, edit.getText().toString(),
                        changeDescriptionHandler).start();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        text.setHint("还可以输入"+(200-s.toString().length())+"字");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
