package com.example.lenovo.ptjob_company.com.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Model.Contact;
import com.example.lenovo.ptjob_company.com.Util.Util;

import java.util.ArrayList;

/**联系人
 * Created by Administrator on 2016/8/29.
 */
public class ContactsAdapter extends BaseAdapter {

    private ArrayList<Contact> mPersonList;

    private LayoutInflater mInflater;

    private Context context;

    public ContactsAdapter(ArrayList<Contact> mPersonList, Context context) {
        this.mPersonList = mPersonList;
        this.context=context;

        mInflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPersonList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView==null){

            convertView=mInflater.inflate(R.layout.contact_item,null);

            holder=new ViewHolder();
            holder.headImg= (ImageView) convertView.findViewById(R.id.id_contacts_person_head_img);
            holder.name= (TextView) convertView.findViewById(R.id.id_contacts_person_name);
//            holder.status= (TextView) convertView.findViewById(R.id.id_contacts_person_status);

            convertView.setTag(holder);

        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.name.setText(mPersonList.get(position).getName());

//        boolean isOnline=mPersonList.get(position).isOnline();
//        if (isOnline){
//
//            holder.status.setText("上线");
//        }else {
//            holder.status.setText("下线");
//        }

        holder.headImg.setImageResource(R.drawable.person);
        Util.loadImageAsync(mPersonList.get(position).getImageName(),holder.headImg,context);

        return convertView;
    }


    class ViewHolder{
        public ImageView headImg;
        private TextView name;
        private TextView status;
    }
}
