package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;

import com.example.lenovo.ptjob_company.com.Adapter.BriefPlurAdapter;
import com.example.lenovo.ptjob_company.com.Adapter.PlurInfoAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetApplierInfoThread;
import com.example.lenovo.ptjob_company.com.Model.Information;
import com.example.lenovo.ptjob_company.com.Model.Pluralist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


/**
 *
 * 显示兼职申请者的粗略消息
 * Created by lenovo on 2017/1/17.
 */
public class ShowBriefPlurInfoActivity extends Activity{
    private ArrayList<Pluralist> list;
    private ListView listview;
    private BriefPlurAdapter adapter;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {
                String response = ((StringBuilder) msg.obj).toString();

                Gson gson = new Gson();

                ArrayList<Pluralist> iList = gson.fromJson(response, new TypeToken<ArrayList<Pluralist>>(){}.getType());

                for (Pluralist pluralist:iList) {
                    Pluralist plur=new Pluralist();
                    plur.setName (pluralist.getName());
                    plur.setPhone(pluralist.getPhone());
                    plur.setAge(pluralist.getAge());
                    plur.setEducationBackground(pluralist.getEducationBackground());
                    plur.setGender(pluralist.getGender());
                    plur.setpId(pluralist.getpId());
                    list.add(plur);
                    adapter.notifyDataSetChanged();
                }
                Log.i("brief_iList",list.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pluralistinfo);
        init();
        context = this;
        String url = App.url+"GetPluralistInfoServlet";
        Intent intent1 =getIntent();
        String iID = (String) intent1.getSerializableExtra("iID");
        new GetApplierInfoThread(url,iID, handler).start();
        adapter=new BriefPlurAdapter(ShowBriefPlurInfoActivity.this,list);
        listview.setAdapter(adapter);
        App.iID=iID;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ShowPlurDetailActivity.class);
                Pluralist pluralist = (Pluralist) adapter.getItem(position);
                Log.i("plur",pluralist.toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("plur",pluralist);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void init(){
        list=new ArrayList<>();
        listview=(ListView) findViewById(R.id.listView3);

    }
}
