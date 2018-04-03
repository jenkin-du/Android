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
import com.example.lenovo.ptjob_company.com.Adapter.ContactsAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Model.Contact;

import java.util.ArrayList;


/**
 * 通讯录列表
 * Created by D on 2016/5/31.
 */
public class ContactsFragment extends Fragment {

    private ListView mContactsListView;

    private static ContactsAdapter mContactsAdapter;
    private static ArrayList<Contact> mContactsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contacts, container, false);

        //初始化
        init(view);
        //设置数据
        setData();
        //设置监听器
        setListener();

        return view;
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        mContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = mContactsList.get(position);
                String friendId = contact.getId();

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
        mContactsListView = (ListView) view.findViewById(R.id.id_communication_contacts_list);

    }

    /**
     * 将列表中设置数据
     */
    private void setData() {

//        // TODO: 2016/9/8 排序
        mContactsList = App.mContacts;
        mContactsAdapter = new ContactsAdapter(mContactsList, getActivity());
//        设置适配器
        mContactsListView.setAdapter(mContactsAdapter);
        mContactsAdapter.notifyDataSetChanged();
    }


//    /**
//     * 设置数据适配器
//     */
//    private void setContactsAdapter() {
//
//        mContactsList = new ArrayList<>();
//
//    }


    /**
     * 添加朋友
     */
    public void addFriend(Contact contact) {

        contact.setOnline(true);
        mContactsList.add(contact);

        Log.i("contactsFragment", "addFriend: mContactsList " + mContactsList.toString());
        mContactsAdapter.notifyDataSetChanged();
    }


//    /**
//     * 恢复数据
//     */
//    public void restoreData() {
//
//
//
//        if (mContactsList != null && mContactsAdapter != null) {
//
//            mContactsList.clear();
//
//            mContactsList.addAll(App.mContacts);
//            mContactsAdapter.notifyDataSetChanged();
//
//        }
//
//    }

    /**
     * 删除好友
     */
    public static void deleteContact(String friendId) {

        for (int i = 0; i < App.mContacts.size(); i++) {
            if (App.mContacts.get(i).getId().equals(friendId)) {
                App.mContacts.remove(i);
                mContactsAdapter.notifyDataSetChanged();
                break;
            }

        }
    }

    /**
     * 通知联系人列表以改变
     */
    public static void notifyContactOnLine(String friendId, boolean online) {

        if (mContactsList!=null){

            for (int i = 0; i < mContactsList.size(); i++) {
                if (friendId.equals(mContactsList.get(i).getId())) {
                    mContactsList.get(i).setOnline(online);
                }
            }
            mContactsAdapter.notifyDataSetChanged();
        }

    }
}

