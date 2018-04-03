package com.android.djs.asynctask;

import android.util.Log;


public class MyAsyncTask extends android.os.AsyncTask<Void,Void,Void>{


    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("asyncTask","cancelled");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("asyncTask", "preExecute");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.i("asyncTask", "postExecute");
    }




    @Override
    protected Void doInBackground(Void... params) {
        Log.i("asyncTask","doInBackground");
        return null;
    }

    public MyAsyncTask() {
        super();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Log.i("asyncTask", "progressUpdate");
    }
}
