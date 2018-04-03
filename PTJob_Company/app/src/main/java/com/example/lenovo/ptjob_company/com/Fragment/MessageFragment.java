package com.example.lenovo.ptjob_company.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Activty.ChatActivity;
import com.example.lenovo.ptjob_company.com.Adapter.MessageAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Model.ChatRecord;
import com.example.lenovo.ptjob_company.com.Model.Contact;
import com.example.lenovo.ptjob_company.com.Model.FriendRequest;
import com.example.lenovo.ptjob_company.com.Model.Message;
import com.example.lenovo.ptjob_company.com.Util.Util;

import java.util.ArrayList;


/**
 * 消息列表
 * Created by D on 2016/5/31.
 */
public class MessageFragment extends Fragment {


    private ListView mNewsList;

    private static ArrayList<Message> mMessageList;
    private static MessageAdapter mMessageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, container, false);

        //初始化
        init(view);
        //设置点击事件
        setListener();


        return view;
    }


    /**
     * 设置点击事件
     */
    private void setListener() {

        mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("messageFragment", "onItemClick: message" + mMessageList.toString());
                Message message = mMessageList.get(position);
                String friendId = message.getId();

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("friendId", friendId);
                startActivity(intent);

            }
        });
    }


    /**
     * 初始化
     */
    private void init(View view) {
        //得到新闻列表的引用
        mNewsList = (ListView) view.findViewById(R.id.id_communication_message_list);


        mMessageList = App.mMessages;
        mMessageAdapter = new MessageAdapter(mMessageList, getActivity());
        mNewsList.setAdapter(mMessageAdapter);

    }


    /**
     * 将新添加的朋友消息添加到消息列表
     */
    public void addMessage(FriendRequest request, Contact contact) {

        if (request != null && contact != null) {

            Message message = new Message();
            message.setId(contact.getId());
            message.setName(contact.getName());
            message.setImageName(contact.getImageName());
            message.setLastMessage(request.getRequestReason());
            message.setTime(Util.getRightTime(request.getTime()));

            //将消息保存在全局变量中
            App.mMessages.add(message);
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 将新消息更新消息列表
     */
    public void updateMessage(ChatRecord record) {

        if (record != null) {
            String friendId = record.getMyId();

            for (int i = 0; i < mMessageList.size(); i++) {

                if (mMessageList.get(i).getId().equals(friendId)) {

                    mMessageList.get(i).setTime(Util.getRightTime(record.getTime()));
                    mMessageList.get(i).setLastMessage(record.getMessage());
                    mMessageAdapter.notifyDataSetChanged();
                    break;
                }
            }

        }
    }


    /**
     * 根据我的聊天更新消息列表
     *
     * @param record 小消息
     */
    public static void updateMessageOnChatting(ChatRecord record) {

        boolean hasMessage = false;

        String friendId = record.getFriendId();

        for (int i = 0; i < mMessageList.size(); i++) {

            if (mMessageList.get(i).getId().equals(friendId)) {
                mMessageList.get(i).setTime(Util.getRightTime(record.getTime()));
                mMessageList.get(i).setLastMessage(record.getMessage());

                mMessageAdapter.notifyDataSetChanged();

                hasMessage = true;
                break;
            }
        }

        if (!hasMessage) {

            Message message=new Message();
            message.setId(friendId);
            message.setLastMessage(record.getMessage());
            message.setTime(Util.getRightTime(record.getTime()));
            for (int i=0;i<App.mContacts.size();i++){
                if (App.mContacts.get(i).getId().equals(friendId)){
                    message.setName(App.mContacts.get(i).getName());
                    message.setImageName(App.mContacts.get(i).getImageName());
                }
            }

            mMessageList.add(message);
            mMessageAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 删除好友信息
     */
    public static void deleteMessage(String friendId) {

        for (int i = 0; i < App.mMessages.size(); i++) {
            if (App.mMessages.get(i).getId().equals(friendId)) {

                App.mMessages.remove(i);
                mMessageAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mMessageList = App.mMessages;
        mMessageAdapter.notifyDataSetChanged();
    }
}
