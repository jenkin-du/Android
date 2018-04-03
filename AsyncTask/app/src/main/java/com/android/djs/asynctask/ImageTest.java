package com.android.djs.asynctask;

        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.ProgressBar;

        import java.io.BufferedInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URL;
        import java.net.URLConnection;

/**显示一张图片
 * Created by Administrator on 2016/3/31.
 */
public class ImageTest extends Activity{


    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.image);
        imageView= (ImageView) findViewById(R.id.image);
        progressBar= (ProgressBar) findViewById(R.id.bar);

        String imageURL = "http://pic3.zhongsou.com/image/38063b6d7defc892894.jpg";
        MyAsyncTask task=new MyAsyncTask();        //新建一个异步任务下载图片的类

        //通过传入网络图片的网址URL,并在背后线程下载图片，执行execute方法，开启程线

        task.execute(imageURL);

    }


    /**
     * 定义一个asyncTask类，用于网络图片的异步下载
     */
    class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {


        /**
         * 背后线程执行耗时操作
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) {    //params参数可变，是一个数组

            String url=params[0];  //取出url地址
            Bitmap bitmap=null;   //定义一个图片资源
            URLConnection connection; //定义一个url连接对象
            InputStream is=null; //定义输入流，用于接收图片资源

            try {
                connection=new URL(url).openConnection(); //新建一个Url对象，
                // 通过其openConnection方法打开网络连接

                is=connection.getInputStream();                     //获取网络输入流
                BufferedInputStream bis=new BufferedInputStream(is); //将输入流包装成带缓冲区的输入流，减小系统开销

                bitmap= BitmapFactory.decodeStream(bis);              //通过BitmapFactory类的静态方法将输入流解析成图片

                Thread.sleep(3000);
                bis.close(); //关闭输入流
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is!=null){
                        is.close(); //关闭输入流
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return bitmap;  //将获得的图片返回给主线程，用于相关的操作
        }

        /**
         * 在线程开始之前预先执行，进行一些初始化操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE); //在获取网络图片之前，让进度条可见，提示用户正在加载图片
        }


        /**
         * 在线程结束时调用执行，返回在线程中执行代码返回的数据数据,此方法在UI线程张调用
         * @param bitmap  线程中执行代码返回的bitmap数据
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) { //此函数的bitmap参数为doInBackground方法返回的bitmap，
                                                                                      //用于与主线程通信
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);    //在获取网络图片之后，让进度条不可见
            imageView.setImageBitmap(bitmap);         //将获取的网络图片在主UI线程中显示出来
        }
    }
}
