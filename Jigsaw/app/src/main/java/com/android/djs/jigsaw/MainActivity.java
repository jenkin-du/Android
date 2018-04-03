package com.android.djs.jigsaw;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    public static int BIAS;
    JigsawSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//全屏
        setContentView(R.layout.main);

        init();

    }

    /**
     * 初始化
     */
    private void init() {

        RelativeLayout mRLayout = (RelativeLayout) findViewById(R.id.rl);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        BIAS = Util.dip2px(this, 16);
        width -= BIAS * 2;
        height -= BIAS * 2;

         view = new JigsawSurfaceView(this, width, height);
        mRLayout.addView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
