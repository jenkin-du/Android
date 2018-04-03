package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Model.ChatRecord;
import com.example.lenovo.ptjob_company.com.Model.Contact;
import com.example.lenovo.ptjob_company.com.Model.Datagram;
import com.example.lenovo.ptjob_company.com.Model.Friend;
import com.example.lenovo.ptjob_company.com.Model.FriendRequest;
import com.example.lenovo.ptjob_company.com.Task.SendMessageTask;
import com.example.lenovo.ptjob_company.com.Util.Action;
import com.example.lenovo.ptjob_company.com.Util.DatagramParser;
import com.example.lenovo.ptjob_company.com.Util.HttpURLHandler;
import com.example.lenovo.ptjob_company.com.Util.HttpURLTask;
import com.example.lenovo.ptjob_company.com.Util.JSONParser;
import com.example.lenovo.ptjob_company.com.Util.Status;
import com.example.lenovo.ptjob_company.com.Util.Util;
import com.example.lenovo.ptjob_company.com.View.NavigationBar;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 新朋友
 * Created by Administrator on 2016/8/30.
 */
public class NewFriendActivity extends Activity {

    private NavigationBar mNavigationBar;

    private ImageView mHeadImage;
    private TextView mNameTV;
    private TextView mGenderTV;
    private TextView mBirthTV;
    private TextView mHeightTV;
    private TextView mEducationTV;
    private TextView mSchoolTV;
    private TextView mEmailTV;
    private TextView mPhoneTV;

    private Button mTalkImage;

    private String friendId;
    private String imageName;

    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friend);
        //初始化
        init();
        //设置监听器
        setListener();
        //从上一个activity中获取数据
        getData();
        //从网络获取数据
        loadNewFriendData();
    }

    /**
     * 设置监听器
     */
    private void setListener() {

        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                //退出
                NewFriendActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

                //添加好友页面
                Intent intent = new Intent();
                intent.putExtra("friendId", friendId);
                intent.setClass(NewFriendActivity.this, FriendRequestActivity.class);
                startActivity(intent);
            }
        });

        mTalkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewFriendActivity.this, ChatActivity.class);

                if(!App.mFriends.contains(friendId)){

                    App.mFriends.add(friendId);

                    Contact contact = new Contact();
                    contact.setName(friend.getName());
                    contact.setId(friend.getId());
                    contact.setChatting(true);
                    contact.setImageName(friend.getImageName());

                    App.mContacts.add(contact);

                    ArrayList<ChatRecord> records = new ArrayList<>();
                    if (!App.mChatRecordsMap.containsKey(friendId)) {
                        App.mChatRecordsMap.put(friendId, records);
                    }

                    addFriend();
                }


                intent.putExtra("friendId", friendId);
                startActivity(intent);

                NewFriendActivity.this.finish();
            }
        });
    }

    /**
     * 通过网络来添加好友
     */
    private void addFriend() {

        Datagram datagram=new Datagram();
        datagram.setRequest(Action.ADD_FRIEND);

        FriendRequest request=new FriendRequest();
        request.setTime(Util.getNowTime());
        request.setFriendId(friendId);

        request.setMyId(App.ID);
        request.setStatus(Action.REQUEST);
        datagram.setJsonStream(JSONParser.toJSONString(request));

        String json= JSONParser.toJSONString(datagram);

        SendMessageTask task=new SendMessageTask(App.IP,App.PORT,json,new Handler());
        task.start();
    }

    /**
     * 从网络获取数据
     */
    private void loadNewFriendData() {

        //异步加载头像
        Util.loadImageAsync(imageName, mHeadImage, this);

        String url = App.PREFIX + "Part-timeJob/PluralistServlet";

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "findFriend");
        params.put("friendId", friendId);

        HttpURLTask task = new HttpURLTask(url, params, new HttpURLHandler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 200) {
                    String jsonDatagram = (String) msg.obj;
                    String response = DatagramParser.getResponse(jsonDatagram);
                    Log.i("jsonDatagram", jsonDatagram);
                    if (response.equals(Status.SUCCESSFUL)) {

                        friend = DatagramParser.getEntity(jsonDatagram, new TypeToken<Friend>() {
                        }.getType());
                        //将数据表现在视图上
                        setData(friend);

                    } else {
                        Toast.makeText(NewFriendActivity.this, "出现错误", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(NewFriendActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.start();


    }

    /**
     * 将数据表现在视图上
     */
    private void setData(Friend friend) {


        mNameTV.setText(friend.getName());
        mGenderTV.setText(friend.getGender());
        mHeightTV.setText(friend.getHeight());
        mBirthTV.setText(friend.getAge());
        mPhoneTV.setText(friend.getPhone());
        mSchoolTV.setText(friend.getSchool());
        mEducationTV.setText(friend.getEducation());
        mEmailTV.setText(friend.getEmail());

    }

    /**
     * 初始化控件
     */
    private void init() {

        mNameTV = (TextView) findViewById(R.id.id_new_friend_name);
        mNavigationBar = (NavigationBar) findViewById(R.id.id_new_friend_navigation_bar);
        mHeadImage = (ImageView) findViewById(R.id.id_new_friend_head_img);
        mGenderTV = (TextView) findViewById(R.id.id_new_friend_gender);
        mBirthTV = (TextView) findViewById(R.id.id_new_friend_birth);
        mEducationTV = (TextView) findViewById(R.id.id_new_friend_education);
        mEmailTV = (TextView) findViewById(R.id.id_new_friend_email);
        mSchoolTV = (TextView) findViewById(R.id.id_new_friend_school);
        mPhoneTV = (TextView) findViewById(R.id.id_new_friend_phone);
        mHeightTV = (TextView) findViewById(R.id.id_new_friend_height);


        mTalkImage = (Button) findViewById(R.id.id_new_friend_talk);
    }

    /**
     * 获得上一个activity传过来的数据
     */
    public void getData() {
        friendId = getIntent().getStringExtra("friendId");
        imageName = getIntent().getStringExtra("imageName");
    }
}
