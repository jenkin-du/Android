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
 * Created by lenovo on 2017/1/17.
 */
public class BriefPlurAdapter extends BaseAdapter {


    private ArrayList<Pluralist> mInfoList;
    private LayoutInflater mInflater;

    public BriefPlurAdapter(Context context, ArrayList<Pluralist> infos){

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


        ViewHolder2 holder;

        if (convertView==null){

            convertView=mInflater.inflate(R.layout.plurinfo,null);

            holder=new ViewHolder2();
           holder.count = (TextView) convertView.findViewById(R.id.countTV);

            holder.name = (TextView) convertView.findViewById(R.id.nameTV);
            holder.phone = (TextView) convertView.findViewById(R.id.numberTV);


            convertView.setTag(holder);

            holder.count.setText((position+1)+"");

        }else {
            holder= (ViewHolder2) convertView.getTag();
        }


        holder.name.setText(mInfoList.get(position).getName());
//
        holder.phone.setText(mInfoList.get(position).getPhone());




        return convertView;
    }

    class ViewHolder2{
        private TextView count;
        private TextView name;
        private TextView phone;

    }
}
