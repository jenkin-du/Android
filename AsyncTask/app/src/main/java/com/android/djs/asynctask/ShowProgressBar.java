package com.android.djs.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;


public class ShowProgressBar extends Activity {

    ProgressBar progressBar;
    ProgressAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showprogress);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setMax(100);
        task = new ProgressAsyncTask(progressBar); //新建一个跟新进度条的异步任务类

        task.execute();          //执行异步任务
    }

    /**
     * 主界面的停止，当主界面被不活动时调用
     */
    @Override
    protected void onStop() {
        super.onStop();
        //当异步任务不为空，且正在运行时
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);           //将异步任务取消掉，其只是将task标记为cancel状态，并不是真正将异步线程取消掉
            //其只是向异步线程发送了cancel请求

        }
    }



}