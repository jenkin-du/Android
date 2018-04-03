package com.android.milkapp2.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.milkapp2.R;
import com.android.milkapp2.model.Datagram;
import com.android.milkapp2.model.Image;
import com.android.milkapp2.model.SharedStory;
import com.android.milkapp2.network.SendMessageTask;
import com.android.milkapp2.util.JSONParser;

import java.util.List;

public class StoryAdapter extends ArrayAdapter<SharedStory> {

    private int srcId;
    private List<SharedStory> objects;
    LayoutInflater inflater;


    public StoryAdapter(Context context, int textViewResourceId, List<SharedStory> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;

        srcId = textViewResourceId;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public SharedStory getItem(int position) {
        return objects.get(position);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        //文艺式的加载布局
        //通过创建viewHolder类，判断列表的布局是否绘制，如果没有绘制，
        //就新建一个ViewHolder类装载布局文件中实例化的对象
        //然后绘制布局，最后将已实例化的布局元素保存起来，下次绘制时直接拿来用，以减小系统开销
        if (convertView == null) {
            holder = new ViewHolder();

            //将布局文件绘制到列表元素的视图上
            convertView = inflater.inflate(srcId, null);

            //通过需要转换的视图找到布局文件中相应的布局元素实例
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            holder.textView = (TextView) convertView.findViewById(R.id.txt);
            convertView.setTag(holder);    //将已经实例化的布局元素保存起来
        } else {
            holder = (ViewHolder) convertView.getTag();    //获得已经实例化的布局元素实例
        }


        //将对应列表中的数据加载到布局文件中,显示默认图片
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.textView.setText(String.format("%s\n%s", objects.get(position).getMessage(), objects.get(position).getTime()));

        //异步加载图片
        String imageName = objects.get(position).getImageName();

        Datagram datagram = new Datagram();
        datagram.setJsonStream(imageName);
        datagram.setRequest("getImage");
        datagram.setType("image");

        String jsonDatagram = JSONParser.toJSONString(datagram);

        SendMessageTask task = new SendMessageTask(jsonDatagram, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String jsonDatagram = (String) msg.obj;

                Image image = JSONParser.toJavaBean(jsonDatagram, Image.class);
                String imageCode = image.getImageCode();

                byte[] buffer = Base64.decode(imageCode, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

                holder.imageView.setImageBitmap(bitmap);
            }
        });
        task.start();

        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }

}
