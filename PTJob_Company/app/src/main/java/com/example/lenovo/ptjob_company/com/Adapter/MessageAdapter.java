package com.example.lenovo.ptjob_company.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Model.Message;
import com.example.lenovo.ptjob_company.com.Util.Util;

import java.util.ArrayList;

/**好友列表数据适配器
 * Created by Administrator on 2016/8/29.
 */
public class MessageAdapter extends BaseAdapter {

    private ArrayList<Message>  mArrList;

    private LayoutInflater mInflater;

    private Context context;

    public MessageAdapter(ArrayList<Message> mArrList, Context context) {
        this.mArrList = mArrList;
        this.context=context;

        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mArrList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView==null){
            //将布局文件绘制到列表元素的视图上
            convertView=mInflater.inflate(R.layout.message_item,null);

            holder=new ViewHolder();
            holder.headImg= (ImageView) convertView.findViewById(R.id.id_contacts_message_head_img);
            holder.name= (TextView) convertView.findViewById(R.id.id_contacts_message_name);
            holder.lastMessage = (TextView) convertView.findViewById(R.id.id_contacts_message_last_message);
            holder.time= (TextView) convertView.findViewById(R.id.id_contacts_message_time);

            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }


        holder.headImg.setImageResource(R.drawable.person);
        Util.loadImageAsync(mArrList.get(position).getImageName(),holder.headImg,context);

        holder.name.setText(mArrList.get(position).getName());
        holder.lastMessage.setText(mArrList.get(position).getLastMessage());
        holder.time.setText(mArrList.get(position).getTime());

        return convertView;
    }



    class ViewHolder{

        public ImageView headImg;
        private TextView name;
        private TextView lastMessage;
        private TextView time;
    }
}
