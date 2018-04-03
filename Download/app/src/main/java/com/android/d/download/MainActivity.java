package com.android.d.download;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.d.entry.FileInfo;
import com.android.d.services.DownloadService;

public class MainActivity extends Activity {

    private TextView tvFilename;
   private Button  btnStart,btnStop;
   private ProgressBar progressBar;

    boolean isFisrt=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tvFilename= (TextView) findViewById(R.id.tv_filename);
        btnStart= (Button) findViewById(R.id.btn_start);
        btnStop= (Button) findViewById(R.id.btn_stop);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        progressBar.setMax(100);

        final FileInfo fileInfo=new FileInfo(0,"http://download.moji001." +
                "com/download/mojiweather-P5.0902.02-0421-release-8888.apk"
                ,0,"mojiweather.apk",0);

        tvFilename.setText(fileInfo.getFileName().substring(0,fileInfo.getFileName().length()-4));

        //开始下载
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);

                if (isFisrt){
                    isFisrt=false;
                    btnStart.setText("继续");
                }
            }
        });

        //停止下载
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
            }
        });

        //注册广播接收器
        IntentFilter filter=new IntentFilter(DownloadService.ACTION_UPDATE);
        registerReceiver(receiver,filter);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);//注销广播接收器
    }


    /**
     * 定义一个广播接收器，接收来自线程中的下载进度，从而更新UI
     */
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //若接收到的intent是更新UI的操作，就更新进度值
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())){

                //获得线程中的进度值
                int progress=  intent.getIntExtra("progress",0);
                //将进度值设置给进度条
                progressBar.setProgress(progress);
                if (progressBar.getProgress()==100){
                    btnStart.setText("开始");
                }
            }
        }
    };
}
