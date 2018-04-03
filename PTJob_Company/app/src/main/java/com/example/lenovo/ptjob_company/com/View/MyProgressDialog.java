package com.example.lenovo.ptjob_company.com.View;

import android.app.ProgressDialog;
import android.content.Context;

/**自定义加载精度条
 * Created by Administrator on 2016/8/30.
 */
public class MyProgressDialog extends ProgressDialog {

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public MyProgressDialog(Context context){
        this(context,0);
    }
}
