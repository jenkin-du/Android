package com.example.lenovo.ptjob_company.com.Activty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Adapter.PlurInfoAdapter;
import com.example.lenovo.ptjob_company.com.Fragment.ReleaseFragment;
import com.example.lenovo.ptjob_company.com.Model.Information;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 主功能实现界面
 * Created by lenovo on 2016/10/20.
 */
public class ChooseActivity extends FragmentActivity{
    private ImageButton releaseIB;
    private ImageButton chatIB;
    private ImageButton personInfoIB;
    private Context context;
    private Button releaseB;
    private Button onRunB;
    private Button timeOutB;

    private FragmentManager manager;

    private ListView listview;
    private ArrayList<Information> list;

    private PlurInfoAdapter adapter;
    Date startTime = null;
    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {

        if (msg.what == 200) {

            String response = ((StringBuilder) msg.obj).toString();
            Gson gson = new Gson();
            //解析成Information的集合
            ArrayList<Information> iList = gson.fromJson(response, new TypeToken<ArrayList<Information>>(){}.getType());
            for (Information information:iList
                 ) {
                Information info=new Information();
                info.setCategory (information.getCategory());
                info.setTitle(information.getTitle());
                String address = information.getWorkAddress();
                info.setWorkAddress(address);

                //计算结束时间，判断消息是否过时
                 startTime = information.getStartWorkTime();
                info.setStartWorkTime(startTime);

                Calendar c = Calendar.getInstance();
                c.setTime(startTime);

                c.add(Calendar.DAY_OF_WEEK, information.getWorkDays());
                Date now = new Date();
                if(now.after(c.getTime())){
                    info.setStatus("已结束");
                }else{
                    info.setStatus("招聘中");
                }

                info.setContactName(information.getContactName());
                info.setRecruitNumber(information.getRecruitNumber());
                info.setSalary(information.getSalary());
                info.setGenderRequest(information.getGenderRequest());
                info.setWorkDays(information.getWorkDays());
                info.setDescription(information.getDescription());
                info.setWorkTime(information.getWorkTime());
                info.setiId(information.getiId());

                list.add(info);
                adapter.notifyDataSetChanged();

            }


        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        init();

        context = this;

        //兼职管理
        releaseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChooseActivity.class);
                startActivity(intent);
            }
        });
        //聊天
        chatIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                startActivity(intent);

            }
        });
        //企业中心
        personInfoIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonInfoActivity.class);
                startActivity(intent);
            }
        });
        //发布消息
        releaseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ReleaseActivity.class);

                startActivity(intent);
            }
        });
        //正在招聘
        onRunB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OnRunInfoActivity.class);

                startActivity(intent);
            }
        });
        //过时招聘信息
        timeOutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TimeoutInfoActivity.class);

                startActivity(intent);
            }
        });


    }
    public void init(){
        releaseIB = (ImageButton) findViewById(R.id.releaseIB);
        chatIB = (ImageButton) findViewById(R.id.chatIB);
        personInfoIB = (ImageButton) findViewById(R.id.infoIB);
        releaseB = (Button) findViewById(R.id.releaseB);
        onRunB = (Button) findViewById(R.id.onRunB);
        timeOutB = (Button) findViewById(R.id.timeOutB);

        list=new ArrayList<>();
        listview=(ListView) findViewById(R.id.listView);

        adapter=new PlurInfoAdapter(ChooseActivity.this,list);
        listview.setAdapter(adapter);


        manager=getSupportFragmentManager();
        FragmentTransaction transition=manager.beginTransaction();

        ReleaseFragment fragment= (ReleaseFragment) manager.findFragmentById(R.id.frame);
        if (fragment==null){
            fragment=new ReleaseFragment();
        }
        transition.add(R.id.frame,fragment);

        transition.commit();
    }


}
