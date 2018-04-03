package com.android.d.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.d.entry.FileInfo;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**后台下载
 * Created by D on 2016/4/26.
 */
public class DownloadService extends Service {

    public static final String ACTION_START="ACTION_START";
    public static final String ACTION_STOP="ACTION_STOP";
    public static final String ACTION_UPDATE="UPDATE";
    public static  final String DOWNLOAD_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/download_demo/";
    public  static  final  int MSG_INIT=0;

    private Downloader downloader=null;




    /**
     * 初始化后台服务
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //获得activity中传来的参数，如果是下载请求，则打开初始化线程开始下载
        if (ACTION_START.equals(intent.getAction())){
            //获得传过来的fileInfo
            FileInfo fileInfo= (FileInfo) intent.getSerializableExtra("fileInfo");
         //启动初始化线程
            new InitThread(fileInfo).start();
        }
        //如果是停止下载的请求，则停止下载文件的线程
        if (ACTION_STOP.equals(intent.getAction())){

            Log.i(ACTION_STOP, "stop");
            if (downloader!=null){
                //停止下载，
                downloader.isPause=true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    /**
     * 初始化文件下载线程
     */
    private class InitThread extends  Thread{

        private  FileInfo fileInfo=null;

        public InitThread(FileInfo fileInfo) {
            this.fileInfo = fileInfo;
        }

        /**
         * 重写run方法，在线程中打开网络连接，并获取文件的长度
         */
        @Override
        public void run() {
            HttpURLConnection conn=null;
            RandomAccessFile randomFile=null;
            try {
                //获取网络文件，读取其长度，在本地创建文件，
                URL url=new URL(fileInfo.getUrl());
                //打开连接
                conn= (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);//超时时间
                conn.setRequestMethod("GET");//请求方式

                int length=-1;
                //如果连接返回码正确，则获得下载文件信息
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    //获取文件长度
                    length=conn.getContentLength();
                    //如果获取长度失败，则直接返回
                    if (length<=0){
                        return ;
                    }

                    //创建文件夹
                    File dir=new File(DOWNLOAD_PATH);
                    if (!dir.exists()){
                        dir.mkdir();
                    }
                   //创建文件
                    File file=new File(dir,fileInfo.getFileName());
                    //随机访问文件对象，用于多点续传文件的写入
                   randomFile=new RandomAccessFile(file,"rwd");
                    //设置文件长度
                    randomFile.setLength(length);
                    //向service发送消息，
                    fileInfo.setLength(length);
                    handler.obtainMessage(MSG_INIT,fileInfo).sendToTarget();
                }


            }catch (Exception e){
                e.printStackTrace();
            }finally {//关闭文件输入流及网络连接
                try {

                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (randomFile != null) {
                        randomFile.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    //定义一个handler，用于处理来自另外线程的消息
     private Handler handler=new Handler(){

        //处理线程中传来的消息，
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INIT:
                    FileInfo fileInfo= (FileInfo) msg.obj;
                    Log.i("fileInfo", ":"+fileInfo.toString());
                    //创建下载类，
                    downloader=new Downloader(DownloadService.this,fileInfo);
                    //开始下载
                    downloader.download();
                    break;
            }
        }
    };
}
