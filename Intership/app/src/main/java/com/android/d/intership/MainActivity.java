package com.android.d.intership;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.d.intership.model.Datagram;
import com.android.d.intership.model.Image;
import com.android.d.intership.network.SendMessageTask;
import com.android.d.intership.util.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button sendBtn;
    private EditText edit;
    private ImageView image;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //初始化
        init();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edit.getText().toString();
                String jsonDatagram = "";
                if (!filePath.equals("")) {

                    Image image = new Image();
                    image.setImageCodeFromFile(filePath);
                    image.setName(filePath);
               //     image.setTime("now");
                    String imageJson = JSONParser.toJSONString(image);

                    Datagram datagram = new Datagram();
                    datagram.setRequest("sendImage");
                    datagram.setType("image");
                    datagram.setJsonStream(imageJson);
                    jsonDatagram = JSONParser.toJSONString(datagram);

                }
                //通过网络传输出去
                SendMessageTask task = new SendMessageTask(jsonDatagram, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        String message = (String) msg.obj;

                        Toast.makeText(MainActivity.this, "来自服务器的消息：\r\n" + message, Toast.LENGTH_SHORT).show();

                    }
                });
                task.start();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

        image.setImageBitmap(bitmap);


        String filePath = Environment.getExternalStorageDirectory().getPath();
        Toast.makeText(MainActivity.this,filePath,Toast.LENGTH_SHORT).show();
        File file = new File(filePath + "/myImage/");
        FileOutputStream bout = null;

        if(!file.exists()){
            boolean is=  file.mkdirs();
        }



        if (requestCode == 1) {

            this.filePath = Environment.getExternalStorageDirectory()
                    .getPath() + "/myImage/1.jpg";

            try {
                bout = new FileOutputStream(this.filePath);
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);// 把数据写入文件
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bout != null) {
                        bout.flush();
                    }
                    if (bout != null) {
                        bout.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            super.onActivityResult(requestCode,resultCode,data);
        }


    }

    private void init() {
        sendBtn = (Button) findViewById(R.id.sendBtn);
        edit = (EditText) findViewById(R.id.edit);
        image = (ImageView) findViewById(R.id.picture);
    }
}
