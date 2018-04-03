package com.android.djs.useimooc_asyncloading;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {


    private ListView list;

    private static  String url="http://www.imooc.com/api/teacher?type=4&num=30";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        list= (ListView) findViewById(R.id.list);
        newsAsyncTask task=new newsAsyncTask();
        task.execute(url);
    }


    /**
     * 使用AsyncTask实现异步加载
     * 创建一个内部类，用于imooc新闻的异步加载
     * 返回一个新闻类的集合
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    class newsAsyncTask extends AsyncTask<String,Void,List<NewsBean>>{

        /**
         * 此方法用于imooc新闻的在另外线程的异步加载，返回一个新闻对象的列表
         * @param params 请求网址
         * @return  包含imooc新闻类的集合
         */
        @Override
        protected List<NewsBean> doInBackground(String... params) {

            return getJsonData(params[0]);
        }


        /**
         * 将在异步线程中获得的newsBean对象列表加载到list列表中
         * @param newsBeans   doInBackground方法中返回的newsBean对象列表集合
         */
        @Override
        protected void onPostExecute(List<NewsBean> newsBeans) {
            super.onPostExecute(newsBeans);

            newsAdapter adapter=new newsAdapter(newsBeans,MainActivity.this,list);
            list.setAdapter(adapter);

        }
    }




    /**
     *
     * 该方法在线程中获取网络新闻的json格式的数据，并把Json格式的数据转化成新闻类的列表
     * @param urlString   请求数据的网址
     * @return  返回新闻类的列表
     */
    private List<NewsBean> getJsonData(String urlString){
        List<NewsBean> newsBeanList = new ArrayList<>();       //创建一个newsBean对象的集合
        String jsonString;
        JSONObject jsonObject;           //创建json对象
        NewsBean newsBean;               //创建一个newsBean对象



        try {
            URL url=new URL(urlString);
            InputStream is=url.openConnection().getInputStream();    //根据url网址获取网络输入流

            jsonString=readStream(is);      //将网络输入流转换成json格式的字符串

            jsonObject=new JSONObject(jsonString);         //通过获取的json格式的字符串数据转化成json对象
          JSONArray jsonArray= jsonObject.getJSONArray("data"); //从json格式中获取json格式的数组

            for (int i=0;i<jsonArray.length();i++){     //通过for循环将json格式的数据添加到newsBean的列表中
                jsonObject=jsonArray.getJSONObject(i);       //从json数组中获得每个json对象
                newsBean =new NewsBean();

                //将json格式的对象转换成newsBean对象
                newsBean.setImageURL(jsonObject.getString("picSmall"));
                newsBean.setTitle(jsonObject.getString("name"));
                newsBean.setContent(jsonObject.getString("description"));

                //将每个获取的newsBean对象添加到列表中
                newsBeanList.add(newsBean);
            }
            is.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return newsBeanList;
    }



    /**
     * 该方法从输入流中获取json格式的字符串
     * @param is      网络输入流
     * @return          json格式的字符串
     */
    private String readStream(InputStream is){

        String result="";
        InputStreamReader isr;

        long start=System.currentTimeMillis();
        try {
            String line;
            isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);

            line=br.readLine();
            while(line!=null){
                result+=line;
                line=br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end=System.currentTimeMillis();

        Log.i("readStream",String.valueOf(end-start));
        return result;
    }
}
