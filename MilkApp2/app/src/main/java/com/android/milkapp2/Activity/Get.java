package com.android.milkapp2.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ListView;

import com.android.milkapp2.Adapter.StoryAdapter;
import com.android.milkapp2.R;
import com.android.milkapp2.model.SharedStory;
import com.android.milkapp2.network.SendMessageTask;
import com.android.milkapp2.util.DatagramParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class Get extends Activity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get);

        listView = (ListView) findViewById(R.id.listView);
        getStories();


    }

    //接收信息
    private void getStories() {

        SharedStory story = new SharedStory();
        Intent data = getIntent();
        story.setLatitude(data.getStringExtra("latitude"));
        story.setLongitude(data.getStringExtra("longitude"));

        String jsonDatagram = DatagramParser.toJsonDatagram("getStory", "story", story);

        SendMessageTask task = new SendMessageTask(jsonDatagram, new Handler() {

            @Override
            public void handleMessage(Message msg) {



                try{
                ArrayList<SharedStory> stories = DatagramParser.getEntity((String) msg.obj, new TypeToken<ArrayList<SharedStory>>() {
                }.getType());

                if (stories != null) {
                    StoryAdapter sa = new StoryAdapter(Get.this, R.layout.story_listview, stories);

                    listView.setAdapter(sa);
                }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        task.start();

    }
}
