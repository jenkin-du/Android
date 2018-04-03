package com.example.lenovo.ptjob_company.com.Activty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Entry.Recruit;
import com.example.lenovo.ptjob_company.com.Fragment.ChatFragment;
import com.example.lenovo.ptjob_company.com.Fragment.CompanyInfoFragment;
import com.example.lenovo.ptjob_company.com.Fragment.ManagerFragment;
import com.example.lenovo.ptjob_company.com.Util.Action;
import com.example.lenovo.ptjob_company.com.Util.Status;

public class MActivity extends FragmentActivity {
    //    //与fragment有关
    private ManagerFragment mManagerFragment;
    private ChatFragment chatFragment;
    private CompanyInfoFragment companyInfoFragment;

    private FragmentManager mManager;
    private FragmentTransaction transaction;

    private ImageButton releaseIB;
    private ImageButton chatIB1;
    private ImageButton personInfoIB;

    private MAReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_main);

        init();

    }

    private void init() {

        releaseIB = (ImageButton) findViewById(R.id.releaseIB);
        chatIB1 = (ImageButton) findViewById(R.id.chatIB1);
        personInfoIB = (ImageButton) findViewById(R.id.infoIB);


        mManager = getSupportFragmentManager();

        transaction = mManager.beginTransaction();
        //兼职管理
        mManagerFragment = (ManagerFragment) mManager.findFragmentById(R.id.frame1);

        if (mManagerFragment == null) {
            mManagerFragment = new ManagerFragment();
        }

        transaction.add(R.id.frame1, mManagerFragment);
        //聊天
        chatFragment = (ChatFragment) mManager.findFragmentById(R.id.frame1);

        if (chatFragment == null) {
            chatFragment = new ChatFragment();
        }


        transaction.add(R.id.frame1, chatFragment);
        transaction.hide(chatFragment);

        //企业中心

        companyInfoFragment = (CompanyInfoFragment) mManager.findFragmentById(R.id.frame1);

        if (companyInfoFragment == null) {
            companyInfoFragment = new CompanyInfoFragment();
        }


        transaction.add(R.id.frame1, companyInfoFragment);
        transaction.hide(companyInfoFragment);


        transaction.commit();

        //聊天
        chatIB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transaction = mManager.beginTransaction();

                transaction.hide(mManagerFragment);
                transaction.hide(chatFragment);
                transaction.hide(companyInfoFragment);

                transaction.show(chatFragment);

                transaction.commit();
            }
        });

        //兼职管理
        releaseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transaction = mManager.beginTransaction();

                transaction.hide(mManagerFragment);
                transaction.hide(chatFragment);
                transaction.hide(companyInfoFragment);

                transaction.show(mManagerFragment);

                transaction.commit();
            }
        });

        personInfoIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transaction = mManager.beginTransaction();
                transaction.hide(mManagerFragment);
                transaction.hide(chatFragment);
                transaction.hide(companyInfoFragment);

                transaction.show(companyInfoFragment);


                transaction.commit();
            }
        });

        receiver = new MAReceiver();

        IntentFilter filter = new IntentFilter(Action.NEW_MESSAGE);
        registerReceiver(receiver, filter);
    }


    /**
     *
     */
    class MAReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getBundleExtra(Status.MESSAGE);
            if (bundle != null) {

                Recruit r=bundle.getParcelable("APPLY");
                if (r!=null){

                    Toast.makeText(MActivity.this,"收到申请",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
