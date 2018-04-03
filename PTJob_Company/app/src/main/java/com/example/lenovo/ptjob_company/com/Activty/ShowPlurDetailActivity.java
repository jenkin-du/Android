package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetStatusThread;
import com.example.lenovo.ptjob_company.com.Entry.UpdateStatusThread;
import com.example.lenovo.ptjob_company.com.Model.Pluralist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 *
 * 显示兼职者的详细信息并对兼职申请状态进行更改
 * Created by lenovo on 2017/1/17.
 */
public class ShowPlurDetailActivity extends Activity{
    private TextView nameTV;
    private TextView genderTV;
    private TextView ageTV;
    private TextView phoneTV;
    private TextView educationTV;
    private TextView isemployedTV;
    private Button okB;
    private Button noB;
    private Button cancelB;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {
                String response = ((StringBuilder) msg.obj).toString();
                Log.i("status",response);
                if(response.equals("EMPLOYED")){
                    isemployedTV.setText("已录用");
                }else if(response.equals("WORKED")){
                    isemployedTV.setText("已工作");
                }else if(response.equals("UNEMPLOYED")){
                    isemployedTV.setText("未录用");
                }else if(response.equals("FINISHED")){
                    isemployedTV.setText("结束工作");
                }else if(response.equals("ENROLLED")){
                    isemployedTV.setText("正在申请");
                }
                else{
                    isemployedTV.setText("未查到信息");
                }

            }
        }
    };

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plurdetail);
        init();
        Intent intent = getIntent();
        Pluralist plur = (Pluralist) intent.getSerializableExtra("plur");
        // Log.i("plu2",plur.toString());
        nameTV.setText(plur.getName());
        genderTV.setText(plur.getGender());
        ageTV.setText(plur.getAge()+"");
        phoneTV.setText(plur.getPhone());
        educationTV.setText(plur.getEducationBackground());

        String pID = plur.getpId();
        final String url = App.url+"RecruitStatusServlet";
        Log.i("D_iID",App.iID);
        Log.i("D-pid",pID);
        App.pID = pID;
        new GetStatusThread(url,App.iID,pID,handler).start();
        final String updateURL = App.url+"EmployServlet";
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isemployedTV.getText().toString().equals("已录用"))
                {
                    Toast.makeText(getApplicationContext(), "已经成功录用，无需重复录用", Toast.LENGTH_SHORT).show();
                }else if(isemployedTV.getText().toString().equals("已工作")){
                    Toast.makeText(getApplicationContext(), "雇佣者已在工作，无需重复录用", Toast.LENGTH_SHORT).show();
                }else if (isemployedTV.getText().toString().equals("结束工作")){
                    Toast.makeText(getApplicationContext(), "当前雇佣关系已解除，不可再次录用", Toast.LENGTH_SHORT).show();

                }else if (isemployedTV.getText().toString().equals("未录用")){
                    Toast.makeText(getApplicationContext(), "您已拒绝录用请求，不可再次录用", Toast.LENGTH_SHORT).show();

                }else {
                    new UpdateStatusThread(updateURL, App.iID, App.pID, "EMPLOYED", handler1).start();
                    Toast.makeText(getApplicationContext(), "成功录用！请联系雇佣者报道！！", Toast.LENGTH_SHORT).show();

                    new GetStatusThread(url, App.iID, App.pID, handler).start();
                }
            }
        });

        noB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isemployedTV.getText().toString().equals("已录用"))
                {
                    Toast.makeText(getApplicationContext(), "已经成功录用，不可拒绝请求", Toast.LENGTH_SHORT).show();
                }else if(isemployedTV.getText().toString().equals("已工作")){
                    Toast.makeText(getApplicationContext(), "已经成功工作，不可拒绝请求", Toast.LENGTH_SHORT).show();
                }else if (isemployedTV.getText().toString().equals("结束工作")){
                    Toast.makeText(getApplicationContext(), "当前雇佣关系已解除，不可拒绝请求", Toast.LENGTH_SHORT).show();

                }else if (isemployedTV.getText().toString().equals("未录用")){
                    Toast.makeText(getApplicationContext(), "您已拒绝录用请求，无需再次拒绝", Toast.LENGTH_SHORT).show();

                }else {
                    new UpdateStatusThread(updateURL,App.iID, App.pID,"UNEMPLOYED",handler1).start();
                    new GetStatusThread(url,App.iID,App.pID,handler).start();

                    Toast.makeText(getApplicationContext(), "您已拒绝此申请者请求！", Toast.LENGTH_SHORT).show();


                }

            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isemployedTV.getText().toString().equals("已录用"))
                {
                    new UpdateStatusThread(updateURL,App.iID, App.pID,"FINISHED",handler1).start();
                    Toast.makeText(getApplicationContext(), "取消录用成功", Toast.LENGTH_SHORT).show();
                    new GetStatusThread(url,App.iID,App.pID,handler).start();
                }else if(isemployedTV.getText().toString().equals("已工作")){
                    new UpdateStatusThread(updateURL,App.iID, App.pID,"FINISHED",handler1).start();

                    Toast.makeText(getApplicationContext(), "取消录用成功", Toast.LENGTH_SHORT).show();
                    new GetStatusThread(url,App.iID,App.pID,handler).start();

                }else if (isemployedTV.getText().toString().equals("结束工作")){
                    Toast.makeText(getApplicationContext(), "当前雇佣关系已解除，不可取消录用", Toast.LENGTH_SHORT).show();

                }else if (isemployedTV.getText().toString().equals("未录用")){
                    Toast.makeText(getApplicationContext(), "您未录用请求，取消录用成功", Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(getApplicationContext(), "您已拒绝此申请者请求，无需取消录用！", Toast.LENGTH_SHORT).show();

                }
                new UpdateStatusThread(updateURL,App.iID, App.pID,"FINISHED",handler1).start();
                new GetStatusThread(url,App.iID,App.pID,handler).start();
            }
        });
    }

    public void init(){
        nameTV = (TextView) findViewById(R.id.p_nameTV);
        genderTV = (TextView) findViewById(R.id.p_genderTV);
        ageTV = (TextView) findViewById(R.id.p_ageTV);
        phoneTV = (TextView) findViewById(R.id.p_phoneTV);
        educationTV = (TextView) findViewById(R.id.p_educationTV);
        isemployedTV = (TextView) findViewById(R.id.p_isemployedTV);

        okB = (Button) findViewById(R.id.okB);
        noB  = (Button) findViewById(R.id.noB);
        cancelB  = (Button) findViewById(R.id.nofurtuerB);
    }
}
