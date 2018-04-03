package com.example.lenovo.ptjob_company.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Activty.OnRunInfoActivity;
import com.example.lenovo.ptjob_company.com.Activty.ReleaseActivity;
import com.example.lenovo.ptjob_company.com.Activty.TimeoutInfoActivity;
import com.example.lenovo.ptjob_company.com.Adapter.PlurInfoAdapter;
import com.example.lenovo.ptjob_company.com.Model.Information;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 2017/2/19.
 */
public class ManagerFragment extends Fragment {

    private Button releaseB;
    private Button onRunB;
    private Button timeOutB;

    private ListView listview;
    private ArrayList<Information> list;

    private FragmentManager manager;
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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.manager,null);
        init(view);
        return view;
    }

    public void init(View view){

        releaseB = (Button)view.findViewById(R.id.releaseB1);
        onRunB = (Button)view. findViewById(R.id.onRunB1);
        timeOutB = (Button) view.findViewById(R.id.timeOutB1);

        list=new ArrayList<>();
        listview=(ListView) view.findViewById(R.id.listView1);

        adapter=new PlurInfoAdapter(getActivity(),list);
        listview.setAdapter(adapter);


        //发布消息
        releaseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ReleaseActivity.class);

                startActivity(intent);
            }
        });
        //正在招聘
        onRunB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OnRunInfoActivity.class);

                startActivity(intent);
            }
        });
        //过时招聘信息
        timeOutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimeoutInfoActivity.class);

                startActivity(intent);
            }
        });

        list=new ArrayList<>();
        listview=(ListView) view.findViewById(R.id.listView1);

        adapter=new PlurInfoAdapter(getActivity(),list);
        listview.setAdapter(adapter);


        manager=getActivity().getSupportFragmentManager();
        FragmentTransaction transition=manager.beginTransaction();

        ReleaseFragment fragment= (ReleaseFragment) manager.findFragmentById(R.id.frame);
        if (fragment==null){
            fragment=new ReleaseFragment();
        }
        transition.add(R.id.frame,fragment);

        transition.commit();



    }
}
