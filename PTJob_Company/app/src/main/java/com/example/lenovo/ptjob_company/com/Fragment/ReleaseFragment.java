package com.example.lenovo.ptjob_company.com.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Activty.PluralityDetailActivity;
import com.example.lenovo.ptjob_company.com.Adapter.PlurInfoAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetAllPlurInfoThread;
import com.example.lenovo.ptjob_company.com.Model.Information;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenovo on 2016/12/7.
 */
public class ReleaseFragment extends Fragment {

    Context context;
    private ListView listview;
    private ArrayList<Information> list;
    private PlurInfoAdapter adapter;
    Date startTime = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {

                Log.i("dhfr", "handleMessage: ");
                String response = ((StringBuilder) msg.obj).toString();
                Gson gson = new Gson();
                ArrayList<Information> iList = gson.fromJson(response, new TypeToken<ArrayList<Information>>() {
                }.getType());
                for (Information information : iList
                        ) {
                    Information info = new Information();
                    info.setCategory(information.getCategory());
                    info.setTitle(information.getTitle());
                    String address = information.getWorkAddress();

                    info.setWorkAddress(address);
                    startTime = information.getStartWorkTime();

                    info.setStartWorkTime(startTime);

                    Calendar c = Calendar.getInstance();
                    c.setTime(startTime);

                    c.add(Calendar.DAY_OF_WEEK, information.getWorkDays());
                    Date now = new Date();
                    if (now.after(c.getTime())) {
                        info.setStatus("已结束");
                    } else {
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
                    Log.i("id", information.getId());

                }


            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.release_frag, container, false);

        context = getActivity();
        list = new ArrayList<>();
        listview = (ListView) view.findViewById(R.id.listView);


        adapter = new PlurInfoAdapter(getActivity(), list);
        listview.setAdapter(adapter);

        String url = "http://192.168.191.1:8080/Part-timeJob/GetParttimejobServlet";
        new GetAllPlurInfoThread(url, App.phoneNumber, handler).start();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.i("tag", "onItemClick: " + list.get(position).toString());
                Intent intent = new Intent(context, PluralityDetailActivity.class);

                Information info = list.get(position);

                Log.i("info!!!!", info.toString());
                Bundle bundle = new Bundle();
                bundle.putParcelable("11", info);

                bundle.putSerializable("Startworktime", list.get(position).getStartWorkTime());
                intent.putExtra("1", bundle);

                startActivity(intent);

            }
        });

        return view;
    }
}
