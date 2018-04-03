package com.android.djs.imageswither;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;


public class ImageAdapter extends BaseAdapter {

    private  int[] resID;
    private Context context;

    public ImageAdapter(Context context, int[] resID) {
        this.context = context;
        this.resID = resID;
    }

    @Override
    public int getCount() {

        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {

        return resID[position];
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image=new ImageView(context);
        //设置对应位置上的图片
        image.setBackgroundResource(resID[position%resID.length]);
        //设置图片的缩略形式
        image.setLayoutParams(new Gallery.LayoutParams(200,150));
        //设置缩放形式
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        return image;
    }
}
