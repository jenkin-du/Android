package com.android.d.viewpagerindicater;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.android.d.view.Indicater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private Indicater indicater;

    private List<String> titles= Arrays.asList("短信1","收藏2","推荐3",
            "短信4","收藏5","推荐6","短信7","收藏8","推荐9");
    private List<ViewPagerFragment> contents=new ArrayList<>();
    private FragmentPagerAdapter adapter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        setContentView(R.layout.main);

        viewPager= (ViewPager) findViewById(R.id.id_viewpager);
        indicater= (Indicater) findViewById(R.id.id_indicater);


        for (String title :titles) {
            ViewPagerFragment fragment=ViewPagerFragment.newInstance(title);
            contents.add(fragment);
        }
        //新建一个fragment适配器
        adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return contents.get(position);
            }

            @Override
            public int getCount() {
                return contents.size();
            }
        };
        //添加适配器
        viewPager.setAdapter(adapter);
        //添加页面变化监听器
        indicater.setOnPagerLinstener(viewPager,0);

    }
}
