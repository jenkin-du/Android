package com.android.djs.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        MyAsyncTask asyncTask=new MyAsyncTask();
//        asyncTask.execute();
    }


    public void loadImage(View view) {

        Intent intent=new Intent(MainActivity.this,ImageTest.class);
        this.startActivity(intent);

    }

    public void showProgress(View view) {
        Intent intent =new Intent(this,ShowProgressBar.class);
        this.startActivity(intent);
    }
}
