package com.android.djs.asynctask;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.Date;

/**
 * Integer参数，用于返回线程任务完成的进度
 */
public class ProgressAsyncTask extends AsyncTask<Void,Integer,Void>{

    private ProgressBar progressBar;

    public ProgressAsyncTask(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }


    /**
     * 该方法在子线程中调用
     * @param params 没有参数
     * @return  没有返回值
     */
    @Override
    protected Void doInBackground(Void... params) {

        for (int i=0;i<=100;i++){


            if (isCancelled()){         //判断该线程时候被请求取消，如果在主线程中发出cancel请求，该值为真

                break;                        //java中无法直接强制地杀掉一个线程，
                // 只有通过这种在主线程中发出cancel请求，在子线程中就收请求，从而判断是否继续让子线程执行，
                //通过这种方式，可以在主线程中间接取消掉一个线程的执行
            }
            publishProgress(i);     //更新任务的执行进度

            try {
                Thread.sleep(300);       //模拟加载任务
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 该方法在主线程调用
     * 该方法用于处理 publishProgress方法所更新的任务进度
     * @param values  此参数为线程中更新进度方法publishProgress所更新的进度值数组
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (isCancelled()){
            return;          //判断是否被取消，若是，则直接返回
        }
        //处理异步线程中更新的进度值
        progressBar.setProgress(values[0]);          //将异步线程中更新的进度值，实时通过ProgressBar显示出来
    }
}
