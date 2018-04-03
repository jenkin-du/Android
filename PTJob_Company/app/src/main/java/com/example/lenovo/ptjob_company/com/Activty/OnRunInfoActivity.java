package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.lenovo.ptjob_company.com.Entry.GetApplierInfoThread;
import com.example.lenovo.ptjob_company.com.Fragment.ReleaseFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Adapter.PlurInfoAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetAllPlurInfoThread;
import com.example.lenovo.ptjob_company.com.Fragment.ReleaseFragment;
import com.example.lenovo.ptjob_company.com.Model.Information;
import com.example.lenovo.ptjob_company.com.Model.Pluralist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 正在招聘的信息，实现功能同ChooseActivity
 * Created by lenovo on 2016/12/3.
 */
public class OnRunInfoActivity extends Activity {
    private ArrayList<Information> list;
    private ListView listview;
    Date startTime = null;
    private PlurInfoAdapter adapter;
    private Context context;
    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {

        if (msg.what == 200) {

            String response = ((StringBuilder) msg.obj).toString();
            Gson gson = new Gson();

            ArrayList<Information> iList = gson.fromJson(response, new TypeToken<ArrayList<Information>>(){}.getType());
            for (Information information:iList
                    ) {
                Calendar c = Calendar.getInstance();
                startTime = information.getStartWorkTime();
                c.setTime(startTime);

                c.add(Calendar.DAY_OF_WEEK, information.getWorkDays());
                Date now = new Date();
                if(!now.after(c.getTime())){
                    Information info=new Information();
                    info.setStatus("招聘中");
                    info.setCategory (information.getCategory());
                info.setTitle(information.getTitle());
                String address = information.getWorkAddress();

                info.setWorkAddress(address);


                info.setStartWorkTime(startTime);

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
                }else{

                }

            }


        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onrun_info);
        init();
        context = this;
        new GetAllPlurInfoThread(App.url+"GetParttimejobServlet",App.phoneNumber,handler).start();

        adapter=new PlurInfoAdapter(OnRunInfoActivity.this,list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ShowBriefPlurInfoActivity.class);
                Information information = (Information)adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("iID",information.getiId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void init(){
        list=new ArrayList<>();
        listview=(ListView) findViewById(R.id.listView2);



    }
}
