package com.android.djs.animator;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{

    private int [] res={R.id.v1,R.id.v2,R.id.v3,R.id.v4,R.id.v5,R.id.v6,R.id.v7,R.id.v8};
    private ArrayList<ImageView> images;
    private  boolean isOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        images=new ArrayList<>();
        for (int re : res) {
            ImageView image = (ImageView) findViewById(re);
            image.setOnClickListener(this);
            images.add(image);
        }



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.v1:

                startAnim();

                break;
            case R.id.v8:
                Intent intent=new Intent(MainActivity.this,ValueAnimator_.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void startAnim() {

        if (!isOpen) {
            for (int i = 0; i < res.length; i++) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(images.get(i), "translationY",
                        0f, i * 150f);
                animator.setDuration(500);
                animator.setStartDelay(i * 300);
                animator.setInterpolator(new BounceInterpolator());//设置插值器，让图形加速变化
                animator.start();
            }
            isOpen = true;
        } else {
            for (int i = res.length-1; i >=0; i--) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(images.get(i), "translationY",
                        i * 150f,0f );
                animator.setDuration(500);
                animator.setStartDelay(i * 300);
                animator.setInterpolator(new BounceInterpolator());//设置插值器，让图形加速变化
                animator.start();
                isOpen=false;
            }

        }
    }
}
