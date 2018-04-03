package com.example.lenovo.ptjob_company.com.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Model.Information;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 2016/12/4.
 */
public class PlurInfoAdapter extends BaseAdapter{

    private ArrayList<Information> mInfoList;
    private LayoutInflater mInflater;

   public PlurInfoAdapter(Context context,ArrayList<Information> infos){

       this.mInfoList=infos;
       mInflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView==null){

            convertView=mInflater.inflate(R.layout.pluralityinfo,null);

            holder=new ViewHolder();
            holder.category = (TextView) convertView.findViewById(R.id.category);

            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.status = (TextView) convertView.findViewById(R.id.status);

            convertView.setTag(holder);

        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.category.setText(mInfoList.get(position).getCategory());
        holder.title.setText(mInfoList.get(position).getTitle());

        String address = mInfoList.get(position).getWorkAddress();
        //holder.address.setText(address);

        String[] strs = address.split(",");
        holder.address.setText(strs[1]);

        Date startTime =mInfoList.get(position).getStartWorkTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String starttime = sdf.format(startTime);
        holder.date.setText(starttime);
        Log.i("ST",starttime);
        holder.status.setText(mInfoList.get(position).getStatus());



        return convertView;
    }

    class ViewHolder{
        private TextView category;
        private TextView title;
        private TextView address;
        private TextView date;
        private TextView status;
    }
}
