package com.android.djs.useimooc_asyncloading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


/**
 * 使用多线程的方式实现异步加载
 * Created by Administrator on 2016/4/2.
 */
public class ImageLoader {

    //创建缓存
    private LruCache<String,Bitmap> cache;

    private ListView listView;
    private Set<MyThread> threads;


    /**
     * 通过开启线程加载图片
     * 通过传入加载图片的url和图片视图，在线程中根据url解析图片数据，再将解析的数据加载到图片视图上
     *
     * @param imageView     需要加载的图片容器的实例
     * @param urlString 获取图片的URL
     */
    public void showImageByThread(ImageView imageView, String urlString) {

        //从内存中获取已缓存的图片
        Bitmap bitmap=getBitmapFromCache(urlString);

        //若没有被缓存，则从网络中下载
        if (bitmap==null){
            //开启一个线程，通过重写run方法实现图片的加载
//            MyThread thread = new MyThread(urlString);
//            thread.start();
            imageView.setImageResource(R.mipmap.ic_launcher);
        }else {

            imageView.setImageBitmap(bitmap);
        }

    }

    class MyThread extends Thread {

        private String urlString;

        private int isCancelled=1;
        private MyHandler handler;

        public MyThread(String urlString){
            this.urlString=urlString;

            handler=new MyHandler(urlString);
        }

        /**
         * 通过重写run方法实现图片的异步加载
         */
        @Override
        public void run() {
            super.run();

            if (isCancelled==1){

                //从url中获取bitmap图片并将其加载到imageView视图上
                Bitmap bitmap = getBitmapFromURL(urlString);

                //将获得的图片放在缓存中
                if(bitmap!=null){

                    addBitmapToCache(urlString,bitmap);

                }

                //非主线程无法直接更新UI线程，下一句不可用，会报错
//            imageView.setImageBitmap(getBitmapFromURL(urlString));

                //通过handler对象可以实现主线程如其他线程通信，可以再其他线程中间接更新UI线程

                //获得现有的消息类型实例，减小系统开销
                Message message = Message.obtain();
                //将获取的bitmap实例封装到message中
                message.obj = bitmap;

                //将封装好的消息发送出去
                handler.sendMessage(message);
            }

        }


        /**
         * 从url中解析出图片
         *
         * @param urlString 对应图片的url
         * @return 返回解析的bitmap
         */
        private Bitmap getBitmapFromURL(String urlString) {

            Bitmap bitmap = null;
            try {
                URL url = new URL(urlString);
                //从url中获取图片输入流
                InputStream is = url.openConnection().getInputStream();
                //将图片输入流通过bitmap工厂类解析成bitmap
                bitmap = BitmapFactory.decodeStream(is);

                if (is != null) {       //关闭输入流
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }

    }

    /**
     * 创建一个handler对象，实现在UI线程中和子线程中进行通信，以实现子线程更新UI线程
     */
    class MyHandler extends Handler {


        private  String urlString;

        public MyHandler(String urlString){
            this.urlString=urlString;
        }

        /**
         * 重写其handlemessage方法，处理主线程传来的消息
         *
         * @param msg 传来的消息
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ImageView imageView= (ImageView) listView.findViewWithTag(urlString);
            if (imageView!=null&&msg.obj!=null){
                imageView.setImageBitmap((Bitmap) msg.obj);
            }


        }

    }


    /**
     * 构造方法
     */
    public ImageLoader(ListView list){

        this.listView=list;

        threads =new HashSet<>();

        //获得最大的内存
        int maxMemory= (int) Runtime.getRuntime().maxMemory();
        int cacheSize=maxMemory/4;

        cache=new LruCache<String,Bitmap>(cacheSize){

            /**
             * 重写sizeOf方法，在每次调用缓存是使用
             * @param key        与图片绑定的url
             * @param value   加载的图片缓存资源
             * @return       返回缓存的大小
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {

                //将图片的大小作为缓存的大小,在每次缓存是调用
                return value.getByteCount();
            }


        };

    }


    /**
     *将图片添加到缓存中
     */
    private void addBitmapToCache(String urlString,Bitmap bitmap){
        //判断cache中是否已保存图片
        if (cache.get(urlString)==null){
            cache.put(urlString,bitmap);
        }

    }


    /**
     * cache可暂时看做Map类
     * 从缓存中获得对应图片
     * @param urlString       图片的url
     * @return   图片
     */
    private Bitmap getBitmapFromCache(String urlString){
        return  cache.get(urlString);
    }



    /**
     * 从自指定向加载图片
     * @param start  开始项
     * @param end   结束项
     */
    public void loadImages(int start,int end){

        for (int i = start; i < end; i++) {

            String url=newsAdapter.urls[i];

            //从内存中获取已缓存的图片
            Bitmap bitmap=getBitmapFromCache(url);

            //若没有被缓存，则从网络中下载

            if (bitmap==null){
                //开启一个线程，通过重写run方法实现图片的加载
                MyThread thread = new MyThread(url);
                thread.start();
                threads.add(thread);
            }else {

                ImageView imageView= (ImageView) listView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 取消图片加载
     */
    public void cancellAllTask(){

        if (threads!=null){

            for(MyThread thread:threads){

                thread.isCancelled=0;

            }

        }
    }
}
