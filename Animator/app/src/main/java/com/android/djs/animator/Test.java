package com.android.djs.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Test extends AppCompatActivity {

    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void click(View view) {
        Toast.makeText(this,"clicked",Toast.LENGTH_LONG).show();
    }

    public void move(View view) {
        imageview= (ImageView) findViewById(R.id.imageView);

        //使用属性动画绘制动画
        //平移
//        ObjectAnimator.ofFloat(imageview,"translationX",0f,200f).setDuration(2000).start();
        //旋转,并组合动画，几种动画同时作用，异步操作
//        ObjectAnimator.ofFloat(imageview,"rotation",0f,360f).setDuration(2000).start();
//        ObjectAnimator.ofFloat(imageview,"translationX",0f,300f).setDuration(2000).start();
//        ObjectAnimator.ofFloat(imageview,"translationY",0f,360f).setDuration(2000).start();


        //另一种组合动画，几种动画同时作用，有优化操作
//        PropertyValuesHolder p1=PropertyValuesHolder.ofFloat("rotation",0f,360f);
//        PropertyValuesHolder p2=PropertyValuesHolder.ofFloat("translationX",0f,360f);
//        PropertyValuesHolder p3=PropertyValuesHolder.ofFloat("translationY",0f,360f);
//        ObjectAnimator.ofPropertyValuesHolder(imageview,p1,p2,p3).setDuration(2000).start();

        //动画集合,
//        AnimatorSet set=new AnimatorSet();
//
//        ObjectAnimator animator1=ObjectAnimator.ofFloat(imageview,"rotation",0f,360f);
//        ObjectAnimator animator2=ObjectAnimator.ofFloat(imageview,"translationX",0f,360f);
//        ObjectAnimator animator3=ObjectAnimator.ofFloat(imageview,"translationY",0f,360f);
//         //几种动画同时作用
////        set.playTogether(animator1,animator2,animator3);
//        //几种动画先后执行
////        set.playSequentially(animator1,animator2,animator3);
////        set.play(animator1).with(animator2);//一起执行
//        set.play(animator2).after(animator3);//先后执行
////        set.play(animator2).with(animator3).with(animator1);
//        set.setDuration(2000);
//
//        set.start();

        ObjectAnimator animator1=ObjectAnimator.ofFloat(imageview,"rotation",0f,360f);
//        animator1.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//                Toast.makeText(Test.this,"end",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Toast.makeText(Test.this,"end",Toast.LENGTH_SHORT).show();
            }
        });
        animator1.start();
    }
}
