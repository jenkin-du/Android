package com.android.djs.animator;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ValueAnimator_ extends Activity {

    Button btn1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
        btn1= (Button) findViewById(R.id.btn1);

    }

    public void onClick(View view) {


        final Button button= (Button) view;
        ValueAnimator animator=ValueAnimator.ofInt(0,100);
        animator.setDuration(20000);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value= (Integer) animation.getAnimatedValue();
                button.setText(""+value);
            }
        });
        animator.start();
    }
}
