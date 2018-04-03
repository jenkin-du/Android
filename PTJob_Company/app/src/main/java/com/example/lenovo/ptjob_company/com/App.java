package com.example.lenovo.ptjob_company.com;

import android.app.Application;


import com.example.lenovo.ptjob_company.com.DataBase.DAO;
import com.example.lenovo.ptjob_company.com.Model.ChatRecord;
import com.example.lenovo.ptjob_company.com.Model.Contact;
import com.example.lenovo.ptjob_company.com.Model.Message;
import com.example.lenovo.ptjob_company.com.Util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class App extends Application{
   public  static String phoneNumber ;

   public  static String url="http://115.159.6.81:8080/Part-timeJob/" ;

   public  static String iID ;

   public  static String pID ;

   public static String ID = "P1";

   public static String PREFIX = "http://115.159.6.81:8080/";
   public static String IP = "115.159.6.81";
   public static int PORT = 2345;


   //全局消息
   public static HashMap<String, ArrayList<ChatRecord>> mChatRecordsMap;


   //全局消息列表
   public static ArrayList<Message> mMessages;
   //全局联系人列表
   public static ArrayList<Contact> mContacts;
   //全局朋友id
   public static ArrayList<String> mFriends;

   @Override
   public void onCreate() {
      super.onCreate();

      //初始化
      init();

   }

   /**
    * 加载全局朋友数据数据
    */
   public void restoreData() {

      mContacts.addAll(DAO.getContexts(getApplicationContext(),ID));
      for (int i = 0; i < mContacts.size(); i++) {
         mFriends.add(mContacts.get(i).getId());
      }


      HashMap<String, ArrayList<ChatRecord>> recordsMap = DAO.getChatRecordMap(getApplicationContext(), mFriends);

      if (recordsMap != null) {
         mChatRecordsMap = recordsMap;
      }


      Contact contact;
      String friendId;
      ChatRecord record;
      ArrayList<ChatRecord> records;
      String name = "";
      String message = "";
      String time = "";
      String imageName="";
      for (int i = 0; i < mContacts.size(); i++) {

         contact = mContacts.get(i);
         friendId = contact.getId();
         name = contact.getName();
         imageName=contact.getImageName();

         records = mChatRecordsMap.get(friendId);
         if (records != null) {
            record = records.get(records.size() - 1);

            message = record.getMessage();
            time = record.getTime();
         }

         Message msg = new Message();
         msg.setLastMessage(message);
         msg.setTime(Util.getRightTime(time));
         msg.setId(friendId);
         msg.setName(name);
         msg.setImageName(imageName);

         mMessages.add(msg);
      }
   }

   private void init() {

      mChatRecordsMap = new HashMap<>();
      mMessages = new ArrayList<>();
      mContacts = new ArrayList<>();
      mFriends = new ArrayList<>();
   }
}
