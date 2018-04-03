package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Adapter.ContactsSearchedAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Model.SearchedPerson;
import com.example.lenovo.ptjob_company.com.Util.HttpURLTask;
import com.example.lenovo.ptjob_company.com.Util.JSONParser;
import com.example.lenovo.ptjob_company.com.View.MyProgressDialog;
import com.example.lenovo.ptjob_company.com.View.NavigationBar;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 添加好友页面
 * Created by Administrator on 2016/8/29.
 */
public class AddFriendActivity extends Activity implements AdapterView.OnItemClickListener {

    private NavigationBar mNavigationBar;//导航栏


    private ListView mResultListView;//结果列表

    private ArrayList<SearchedPerson> mPersonList;
    private ContactsSearchedAdapter mAdapter;

    private MyProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        //初始化
        init();
        //注册监听器
        setOnClickListener();

    }

    /**
     * 注册监听器
     */
    private void setOnClickListener() {
        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                AddFriendActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {
            }
        });

        mResultListView.setOnItemClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {

        mNavigationBar = (NavigationBar) findViewById(R.id.id_add_friend_navi_bar);
        mResultListView = (ListView) findViewById(R.id.id_add_friend_result_listView);

        mPersonList = new ArrayList<>();
        mAdapter = new ContactsSearchedAdapter(mPersonList, this);
        mResultListView.setAdapter(mAdapter);

        progressDialog = new MyProgressDialog(this);


        loadAllPluralist(App.ID);
    }


    /**
     * 根据搜索条件加载数据
     */
    private void loadAllPluralist(String condition) {

        String url = App.PREFIX + "Part-timeJob/PluralistServlet";

        final HashMap<String, String> params = new HashMap<>();
        params.put("action", "getPluralist");
        params.put("condition", condition);

        HttpURLTask task = new HttpURLTask(url, params, new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 200) {

                    String json = (String) msg.obj;
                    ArrayList<SearchedPerson> persons = JSONParser.toJavaBean(json, new TypeToken<ArrayList<SearchedPerson>>() {
                    }.getType());

                    if (persons != null) {

                        if(persons.size()!=0){
                            mPersonList.addAll(persons);
                            mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(AddFriendActivity.this, "没有应聘者", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AddFriendActivity.this, "没有应聘者", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    progressDialog.dismiss();
                    Toast.makeText(AddFriendActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.start();


    }

    /**
     * 点击item
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SearchedPerson person = mPersonList.get(position);
        String friendId = person.getpId();
        String imageName=person.getImageName();

        Intent intent = new Intent(AddFriendActivity.this, NewFriendActivity.class);
        intent.putExtra("friendId", friendId);
        intent.putExtra("imageName",imageName);
        startActivity(intent);

        AddFriendActivity.this.finish();

    }
}
