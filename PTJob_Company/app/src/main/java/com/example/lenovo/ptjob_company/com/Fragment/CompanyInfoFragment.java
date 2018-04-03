package com.example.lenovo.ptjob_company.com.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Activty.ChangeDescriptionActivity;
import com.example.lenovo.ptjob_company.com.Activty.ChangePasswordActivity;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetPersonInfoThread;

/**
 * Created by lenovo on 2017/2/20.
 */
public class CompanyInfoFragment extends Fragment {
    private TextView companyName;
    private TextView personName;
    private TextView personPhone;
    private TextView companyDescription;
    private LinearLayout changePasswordLL;
    private LinearLayout changeDescriptionLL;

    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 200) {

                String response = ((StringBuilder) msg.obj).toString();
                String str[] = response.split(",");
                String pName = str[0];//联系人姓名
                String cName = str[1];//公司名
                String companyDes = str[2];//公司简介

                if(!pName.equals("")) {
                    personName.setText(pName);
                }
                if(!cName.equals("")) {
                    companyName.setText(cName);
                }if(!companyDescription.equals("")){
                    companyDescription.setText(companyDes);
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.personinfo,null);
        init(view);
        return view;
    }

    public void init(View view){
        companyName= (TextView) view.findViewById(R.id.companyName);
        personName= (TextView) view.findViewById(R.id.personName);
        personPhone= (TextView) view.findViewById(R.id.personPhone);
        companyDescription= (TextView) view.findViewById(R.id.companyDescription);
        changePasswordLL = (LinearLayout) view.findViewById(R.id.changepasswordLL);
        changeDescriptionLL =  (LinearLayout) view.findViewById(R.id.changedescriptionLL);
        String url = App.url+"GetPersonInfoServlet";
        new GetPersonInfoThread(url, App.phoneNumber,handler).start();
        personPhone.setText(App.phoneNumber);
        changePasswordLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChangePasswordActivity.class);
                startActivity(intent);


            }
        });
        changeDescriptionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChangeDescriptionActivity.class);
                startActivity(intent);

            }
        });


    }
}
