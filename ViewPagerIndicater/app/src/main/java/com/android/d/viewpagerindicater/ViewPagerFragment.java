package com.android.d.viewpagerindicater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ViewPagerFragment extends Fragment {

    private String title;
    private static final String TITLE = "title";

    /**
     * 用静态方法为外界创建fragment的方法
     *
     * @param title fragment的名字
     */
    public static ViewPagerFragment newInstance(String title) {

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle=getArguments();//获得保存的数据
        if (bundle!=null){
            title=bundle.getString(TITLE);
        }

        TextView view=new TextView(getContext());
        view.setText(title);
        view.setGravity(Gravity.CENTER);

        return view;
    }
}
