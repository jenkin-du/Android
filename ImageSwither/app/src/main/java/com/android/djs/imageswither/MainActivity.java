package com.android.djs.imageswither;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;


public class MainActivity extends Activity implements ViewFactory {

    private int[] resID;
    private Gallery gallery;
    private ImageAdapter adapter;
    private ImageSwitcher switcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gallery= (Gallery) findViewById(R.id.gallery);
        switcher= (ImageSwitcher) findViewById(R.id.swither);

        resID=new int[]{R.drawable.item1,R.drawable.item2,R.drawable.item3,R.drawable.item4,
                R.drawable.item5,R.drawable.item6,R.drawable.item7,R.drawable.item8,R.drawable.item9,
                R.drawable.item10,R.drawable.item11,R.drawable.item12,};

        adapter=new ImageAdapter(this,resID);
        gallery.setAdapter(adapter);

        switcher.setFactory(this);
        switcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        switcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
        //添加监听器，让选中的图片显示出来
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                resID[position%resID.length]
                switcher.setBackgroundResource(resID[position % resID.length]);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public View makeView() {

        ImageView image=new ImageView(this);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return image;
    }
}
