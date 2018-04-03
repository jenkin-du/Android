package com.example.lenovo.ptjob_company.com.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Activty.AddFriendActivity;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.DataBase.DAO;
import com.example.lenovo.ptjob_company.com.Model.ChatRecord;
import com.example.lenovo.ptjob_company.com.Model.Contact;
import com.example.lenovo.ptjob_company.com.Model.Datagram;
import com.example.lenovo.ptjob_company.com.Model.FriendRequest;
import com.example.lenovo.ptjob_company.com.Model.OnlineRecord;
import com.example.lenovo.ptjob_company.com.Server.CommunicationServer;
import com.example.lenovo.ptjob_company.com.Task.SendMessageTask;
import com.example.lenovo.ptjob_company.com.Util.Action;
import com.example.lenovo.ptjob_company.com.Util.DatagramParser;
import com.example.lenovo.ptjob_company.com.Util.JSONParser;
import com.example.lenovo.ptjob_company.com.Util.Status;
import com.example.lenovo.ptjob_company.com.Util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ImageView mAddImg;
    private ImageView mNewsIndicator;
    private ImageView mContactsIndicator;

    private TextView mNewsTV;
    private TextView mContactsTV;

    private MessageReceiver receiver;

    //viewpager
    private ViewPager mPager;
    private List<Fragment> mFragmentList;

    private Bundle bundle;

    private static String TAG = "ChatMainActivity";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.communication,null);
        //初始化
        init(view);
        //开启后台服务
        setService();
        //设置点击事件监听器
        setClickListener();
        //添加fragment到此布局中
        addFragment();
        //接受添加好友消息
        setBroadcastReceiver();
        //上线
        setOnline();
        return view;
    }

    private void setService() {

        Intent intent = new Intent();
        intent.setClass(getActivity(), CommunicationServer.class);
        getActivity().startService(intent);
    }

    /**
     * 上线
     */
    private void setOnline() {

        OnlineRecord onlineRecord = new OnlineRecord();
        onlineRecord.setId(App.ID);
        onlineRecord.setIp(Util.getIpAdd(getActivity().getApplicationContext()));

        String jsonDatagram = DatagramParser.toJsonDatagram(Action.ON_LINE, null, onlineRecord);
        final SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String json = (String) msg.obj;
                ArrayList<String> onlineFriendIds = DatagramParser.getEntity(json, new TypeToken<ArrayList<String>>() {
                }.getType());

                if (onlineFriendIds != null) {
                    for (String friendId : onlineFriendIds)
                        ContactsFragment.notifyContactOnLine(friendId,true);
                }

            }
        });
        task.start();
    }

    /**
     * 接受添加好友消息
     */
    private void setBroadcastReceiver() {
        receiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter(Action.NEW_MESSAGE);
        filter.setPriority(200);
        getActivity().registerReceiver(receiver, filter);

    }


    /**
     * 设置监听事件
     */
    private void setClickListener() {

        mAddImg.setOnClickListener(this);
        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(0);

        mNewsTV.setOnClickListener(this);
        mContactsTV.setOnClickListener(this);
        mNewsTV.setTextColor(getResources().getColor(R.color.text_color));
    }


    /**
     * 添加消息和通讯录fragment
     */
    private void addFragment() {

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };

        mPager.setAdapter(adapter);
    }


    /**
     * 初始化
     */
    private void init(View view) {
        mAddImg= (ImageView) view.findViewById(R.id.id_communication_plus);
        mPager = (ViewPager) view.findViewById(R.id.id_communication_pager);
        mContactsTV = (TextView) view.findViewById(R.id.id_communication_contacts);
        mNewsTV = (TextView) view.findViewById(R.id.id_communication_news);
        mContactsIndicator = (ImageView) view.findViewById(R.id.id_bar_indicator_blow_contacts);
        mNewsIndicator = (ImageView) view.findViewById(R.id.id_bar_indicator_blow_news);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new ContactsFragment());

        ((App) getActivity().getApplication()).restoreData();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            //根据当前选项卡设置当前显示tab文字高亮
            mNewsTV.setTextColor(getResources().getColor(R.color.text_color));
            mContactsTV.setTextColor(getResources().getColor(R.color.onclick));
            //根据当前选项卡设置指示器显示位置
            mNewsIndicator.setVisibility(ImageView.VISIBLE);
            mContactsIndicator.setVisibility(ImageView.INVISIBLE);
        }
        if (position == 1) {
            //根据当前选项卡设置当前显示tab文字高亮
            mNewsTV.setTextColor(getResources().getColor(R.color.onclick));
            mContactsTV.setTextColor(getResources().getColor(R.color.text_color));
            //根据当前选项卡设置指示器显示位置
            mNewsIndicator.setVisibility(ImageView.INVISIBLE);
            mContactsIndicator.setVisibility(ImageView.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_communication_contacts:
                mPager.setCurrentItem(1);
                break;

            case R.id.id_communication_news:

                mPager.setCurrentItem(0);
                break;

            case R.id.id_communication_plus:

                //添加好友
                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent);

                break;

//            //弹出侧栏
//            case R.id.id_usr_head_image_com:
//                MainActivity.slidingMenu.toggle(true);
//                Log.i("tag", "onClick: communication");
            default:
                break;
        }
    }


    /**
     * 广播接收器
     */
    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            bundle = intent.getBundleExtra(Status.MESSAGE);
            if (bundle != null) {

                FriendRequest request = bundle.getParcelable(Status.FRIEND_REQUEST);
                ChatRecord record = bundle.getParcelable(Status.CHAT_RECORD);
                OnlineRecord online = bundle.getParcelable(Action.ON_LINE);
                OnlineRecord offline = bundle.getParcelable(Action.OFF_LINE);

                if (request != null) {

                    String friendId;
                    String status = request.getStatus();

                    Log.i(TAG, "onReceive: request: " + request.toString());
                    switch (status) {
                        case Action.REQUEST:
                            friendId = request.getMyId();
                            //是否已经存在
                            if (!App.mFriends.contains(friendId) && !App.ID.equals(friendId)) {
                                showRequestDialog(request);
                            }
                            break;
                        case Action.AGREE:
                            friendId = request.getFriendId();
                            //是否已经存在
                            if (!App.mFriends.contains(friendId) && !App.ID.equals(friendId)) {
                                //将朋友的详细信息获取
                                obtainFriendDetail(request);

                            }

                            break;
                        case Action.DELETE:
                            friendId = request.getMyId();
                            //删除好友
                            MessageFragment.deleteMessage(friendId);
                            ContactsFragment.deleteContact(friendId);
                            App.mChatRecordsMap.remove(friendId);
                            if (App.mFriends.contains(friendId)) {
                                App.mFriends.remove(friendId);
                            }
                            //删除好友聊天记录
                            DAO.deleteFriends(getActivity(), App.ID, friendId);
                            Toast.makeText(getActivity(),"好友将你删除",Toast.LENGTH_SHORT).show();
                            break;
                    }


                }

                //更新朋友消息
                if (record != null) {
                    Log.i(TAG, "onReceive: record----------------  " + record.toString());
                    //更新消息
                    ((MessageFragment) mFragmentList.get(0)).updateMessage(record);

                    String friendId = record.getMyId();
                    for (int i = 0; i < App.mContacts.size(); i++) {
                        if (friendId.equals(App.mContacts.get(i).getId())) {

                            if (!App.mContacts.get(i).isChatting()) {
                                ArrayList<ChatRecord> records = App.mChatRecordsMap.get(friendId);
                                if (records == null) {
                                    records = new ArrayList<>();
                                    records.add(record);

                                    App.mChatRecordsMap.put(friendId, records);
                                }
                                App.mChatRecordsMap.get(friendId).add(record);
                            }
                            break;
                        }
                    }

                }
                //上线
                if (online != null) {

                    Log.i(TAG, "onReceive: online   " + online.toString());
                    String friendId = online.getId();
                    ContactsFragment.notifyContactOnLine(friendId,true);
                }
                //下线
                if (offline != null) {

                    Log.i(TAG, "onReceive: offline   " + offline.toString());
                    String friendId = offline.getId();
                    ContactsFragment.notifyContactOnLine(friendId,false);
                }
            }


        }
    }


    /**
     * 弹出dialog提示好友请求加好友
     */
    private void showRequestDialog(final FriendRequest request) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("好友申请");

        String message = request.getFriendName() + "申请加你为好友";
        builder.setMessage(message);

        //同意按钮
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //同意添加好友
                agreeAddFriend(request);

            }
        });

        //拒绝按钮
        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //拒绝添加好友
                refuseAddFriend();
            }
        });

        //创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /**
     * 拒绝添加好友
     */
    private void refuseAddFriend() {


    }

    /**
     * 同意添加好友
     */
    private void agreeAddFriend(final FriendRequest request) {

        //通过网络将同意添加好友的消息传递出去
        Datagram datagram = new Datagram();
        datagram.setRequest(Action.ADD_FRIEND);
        request.setStatus(Action.AGREE);

        datagram.setJsonStream(JSONParser.toJSONString(request));
        String jsonDatagram = JSONParser.toJSONString(datagram);

        SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler() {

            @Override
            public void handleMessage(Message msg) {

                String jsonData = (String) msg.obj;
                if (jsonData != null && !jsonData.equals("")) {

                    Contact contact = DatagramParser.getEntity(jsonData, new TypeToken<Contact>() {
                    }.getType());

                    Log.i(TAG, "handleMessage: contact " + contact.toString());
                    //更新UI
                    addFriends(contact, request);
                }
            }
        });
        task.start();

    }

    /**
     * 通过网络将刚添加的朋友的信息查找出来
     */
    private void obtainFriendDetail(final FriendRequest request) {

        String friendId = request.getFriendId();

        Datagram datagram = new Datagram();
        datagram.setRequest(Action.QUERY_FRIEND);

        datagram.setJsonStream(friendId);
        String jsonData = JSONParser.toJSONString(datagram);

        SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonData, new Handler() {
            @Override
            public void handleMessage(Message msg) {

                String jsonData = (String) msg.obj;
                if (jsonData != null && !jsonData.equals("")) {

                    Contact contact = DatagramParser.getEntity(jsonData, new TypeToken<Contact>() {
                    }.getType());

                    //更新UI
                    addFriends(contact, request);
                }
            }
        });
        task.start();
    }

    /**
     * 更新UI新信息
     */
    private void addFriends(Contact contact, FriendRequest request) {

        String friendId = contact.getId();
        App.mFriends.add(friendId);

        Log.i(TAG, "addFriends: mFriends " + App.mFriends.toString());
        Log.i(TAG, "addFriends: mContacts " + App.mContacts.toString());

        //将聊天记录放在全局变量中
        ChatRecord record = new ChatRecord();

        String status=request.getStatus();
        if(status.equals(Action.REQUEST)){
            record.setMyId(App.ID);
            record.setFriendId(friendId);

        }else if (status.equals(Action.AGREE)){
            record.setMyId(friendId);
            record.setFriendId(App.ID);
        }

        record.setTime(request.getTime());
        record.setMessage(request.getRequestReason());

        ArrayList<ChatRecord> records = new ArrayList<>();
        records.add(record);

        App.mChatRecordsMap.put(friendId, records);
        //添加朋友
        ((ContactsFragment) mFragmentList.get(1)).addFriend(contact);
        //将消息传到MessageFragment中更新UI
        ((MessageFragment) mFragmentList.get(0)).addMessage(request, contact);
    }


    /**
     * 下线
     */
    private void setOffline() {

        OnlineRecord onlineRecord = new OnlineRecord();
        onlineRecord.setId(App.ID);

        String jsonDatagram = DatagramParser.toJsonDatagram(Action.OFF_LINE, null, onlineRecord);
        SendMessageTask task = new SendMessageTask(App.IP, App.PORT, jsonDatagram, new Handler());
        task.start();
    }




    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       getActivity().unregisterReceiver(receiver);

        //下线
        setOffline();
        //保存数据
        DAO.saveContacts( getActivity(), App.mContacts);
        DAO.saveChatRecords( getActivity(), App.mChatRecordsMap);

        App.mFriends.clear();
        App.mMessages.clear();
        App.mChatRecordsMap.clear();
        App.mContacts.clear();
    }


}
