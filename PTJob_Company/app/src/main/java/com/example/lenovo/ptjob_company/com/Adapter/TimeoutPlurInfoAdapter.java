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
import com.example.lenovo.ptjob_company.com.Model.Pluralist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 2016/12/4.
 */
public class TimeoutPlurInfoAdapter extends BaseAdapter{

    private ArrayList<Pluralist> mInfoList;
    private LayoutInflater mInflater;

   public TimeoutPlurInfoAdapter(Context context, ArrayList<Pluralist> infos){

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

            convertView=mInflater.inflate(R.layout.timeout_item,null);

            holder=new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.t_nameTV);

           holder.isEmployed = (TextView) convertView.findViewById(R.id.t_isemployedTV);

            convertView.setTag(holder);

        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.name.setText(mInfoList.get(position).getName());
        holder.isEmployed.setText("成功录用");

        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView isEmployed;
    }
}
