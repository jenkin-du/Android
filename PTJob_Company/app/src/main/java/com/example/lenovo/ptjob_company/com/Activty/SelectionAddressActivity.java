package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Adapter.SpinnerAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetCityThread;
import com.example.lenovo.ptjob_company.com.Entry.GetProvinceThread;
import com.example.lenovo.ptjob_company.com.Entry.GetZoneThread;
import com.google.gson.Gson;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * 用于选择地址
 * Created by lenovo on 2016/11/22.
 */
public class SelectionAddressActivity extends Activity{
    private Spinner spinner_sheng;//省
    private Spinner spinner_shi;//市
    private Spinner spinner_qu;//区
    Bundle bundle;
    private EditText et_detailAddress;//输入详细地址
    private SpinnerAdapter adapter,adapter2,adapter3;
    private Button btn_confirm;//确认按钮
    ArrayList<String> provinces,cities,zones = new ArrayList<String>();
    Context context =this;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        Gson gson = new Gson();

        if (msg.what == 200) {

          String response = ((StringBuilder) msg.obj).toString();
            Log.i("response",response);
            provinces = gson.fromJson(response,ArrayList.class);

            int size = provinces.size();
            Log.i("1", "handleMessage: ");
            String[] strs = provinces.toArray(new String[size]);
            Log.i("2", "handleMessage: ");
            adapter=new SpinnerAdapter(SelectionAddressActivity.this, strs);
            spinner_sheng.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }


        }
    };
private Handler handler1 = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        Gson gson = new Gson();

        if (msg.what == 200) {
            String response = ((StringBuilder) msg.obj).toString();
            cities=gson.fromJson(response,ArrayList.class);
            int size = cities.size();
            String[] strs = cities.toArray(new String[size]);

            adapter2 = new SpinnerAdapter(SelectionAddressActivity.this, strs);
            spinner_shi.setAdapter(adapter2);
        }
    }
};
    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();

            if (msg.what == 200) {
                String response = ((StringBuilder) msg.obj).toString();
                zones=gson.fromJson(response,ArrayList.class);
                int size = zones.size();
                String[] strs = zones.toArray(new String[size]);

                adapter3 = new SpinnerAdapter(SelectionAddressActivity.this, strs);
                spinner_qu.setAdapter(adapter3);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        initView();
        String provinceURL= App.url+"GetProvinceServlet";
        new GetProvinceThread(provinceURL,handler).start();
        spinner_sheng.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String provinceName =  spinner_sheng.getSelectedItem().toString();
                Log.i("provinceName",provinceName);
                String cityURL=App.url+"GetCityServlet";
                new GetCityThread(cityURL,provinceName,handler1).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        spinner_shi.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cityName =  spinner_shi.getSelectedItem().toString();
                Log.i("cityName",cityName);
                String cityURL=App.url+"GetZoneServlet";
                new GetZoneThread(cityURL,cityName,handler2).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        btn_confirm.setOnClickListener(new View.OnClickListener(){

            @Override
             public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                String provinceName =  spinner_sheng.getSelectedItem().toString();
                String cityName =  spinner_shi.getSelectedItem().toString();
                String zoneName =  spinner_qu.getSelectedItem().toString();
                String detailAddress = et_detailAddress.getText().toString();
                String allAddress = sb.append(provinceName).append(cityName).
                        append(zoneName).append(detailAddress).toString();

                bundle = new Bundle();
                bundle.putSerializable("allAddress",allAddress);

                bundle.putSerializable("provinceName",provinceName);
                bundle.putSerializable("cityName",cityName);
                bundle.putSerializable("zoneName",zoneName);
                bundle.putSerializable("detailAddress",detailAddress);
                Intent intent = new Intent(context, ReleaseActivity.class);


               intent.putExtras(bundle);
               // startActivity(intent);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    //初始化控件
    private void initView() {
        spinner_sheng = (Spinner) findViewById(R.id.spinner_sheng);
        spinner_shi = (Spinner) findViewById(R.id.spinner_shi);
        spinner_qu = (Spinner) findViewById(R.id.spinner_qu);
        et_detailAddress = (EditText) findViewById(R.id.et_detailAddress);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

}
