package com.example.lenovo.ptjob_company.com.Activty;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lenovo.ptjob_company.R;
import com.example.lenovo.ptjob_company.com.Adapter.SpinnerAdapter;
import com.example.lenovo.ptjob_company.com.App;
import com.example.lenovo.ptjob_company.com.Entry.GetDefaultInfoThread;
import com.example.lenovo.ptjob_company.com.Entry.ReleaseThread;

import java.util.Calendar;

/**
 * 发布兼职消息
 * Created by lenovo on 2016/10/23.
 */
public class ReleaseActivity extends Activity {
    private EditText releaseName;
    private EditText datePickerET;
    private EditText timePickerET;
    private Spinner spinner, spinner2, spinner3, spinner4;
    private final String[] types = {"其他", "家教", "服务", "促销", "实习", "派单"};
    private final String[] salaries = {"元/小时", "元/天", "元/月"};
    private final String[] genders = {"男", "女", "不限"};
    private final String[] experiences = {"有经验者优先",  "不限"};
    private SpinnerAdapter adapter, adapter2, adapter3, adapter4;
    private EditText endTimePickerET;
    private Button submitButton;
    private EditText lastTimeET;
    private EditText numberOfEmployeeET;
    private EditText salaryET;
    private EditText heightET;
    private EditText descriptionET;
    private EditText detailET;
    private EditText contactNameET;
    private EditText contactPhoneET;
    private Context context;
    private EditText addressET;

    String province="";
    String city="";
    String zone = "";
    String detailAddress="";


    private Handler getDefaultHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {

        if (msg.what == 200) {

            String response = ((StringBuilder) msg.obj).toString();
            String str[] = response.split(",");
            String name = str[0];
            String address = str[1];

            if(!name.equals("")) {
                contactNameET.setText(name);
            }
            if(!address.equals(""))
            addressET.setText(address);
        }
        }
    };
    private Handler releaseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        if (msg.what == 200) {

            Log.i("djkds", "handleMessage: ");
            String response = ((StringBuilder) msg.obj).toString();
            if (response.equals("success")) {
                Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,ChooseActivity.class);
                startActivity(intent);
            }else if(response.equals("failed")){
                Toast.makeText(getApplicationContext(), "发布失败！", Toast.LENGTH_SHORT).show();
            }
        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release);
        init();
        context = this;
        adapter = new SpinnerAdapter(this, types);
        adapter2 = new SpinnerAdapter(this, salaries);
        adapter3 = new SpinnerAdapter(this, genders);
        adapter4 = new SpinnerAdapter(this, experiences);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);
        datePickerET.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(ReleaseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        datePickerET.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        timePickerET.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(ReleaseActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        timePickerET.setText(DateFormat.format("kk:mm", c));
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                dialog.show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        endTimePickerET.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(ReleaseActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        endTimePickerET.setText(DateFormat.format("kk:mm", c));
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                dialog.show();
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addressET.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectionAddressActivity.class);

                startActivityForResult(intent,1);

            }
        });

        contactPhoneET.setText(App.phoneNumber);

        String url = App.url+"GetReleaseDefaultServlet";
        new GetDefaultInfoThread(url,App.phoneNumber,getDefaultHandler).start();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = releaseName.getText().toString();//兼职标题
                 String type = spinner.getSelectedItem().toString();//兼职类型
                String datePicker = datePickerET.getText().toString();//选择的日期
                String startTime  = timePickerET.getText().toString();//开始时间
                String endTime = endTimePickerET.getText().toString();//结束时间
                String lastTime =lastTimeET.getText().toString();//持续时间
               String numberOfEmployee = numberOfEmployeeET.getText().toString();//招工人数
                String salary = salaryET.getText().toString();//薪资
                String salaryUnit =  spinner2.getSelectedItem().toString();//薪资单位
                String gender = spinner3 .getSelectedItem().toString();//性别要求
                String experience = spinner4.getSelectedItem().toString();//经验要求
                String height = heightET.getText().toString();//身高要求
                String description =  descriptionET.getText().toString();//工作描述
                String detail = detailET.getText().toString();//细节描述
                String contactNameOfRelease = contactNameET.getText().toString();//联系人姓名
                String contactPhoneOfRelease  = contactPhoneET.getText().toString();//联系人号码
                String detailAddress1 = detailAddress;
                String province1 = province;
                String city1 = city;
                String zone1 = zone;
                String phone = App.phoneNumber;
                String releaseURL =App.url+"ReleaseServlet";

                if (title.equals("")){
                    Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();

                }else if(type.equals("")){
                    Toast.makeText(getApplicationContext(), "兼职类型未选择", Toast.LENGTH_SHORT).show();

                }else if(datePicker.equals("")){
                    Toast.makeText(getApplicationContext(), "兼职日期未选择", Toast.LENGTH_SHORT).show();

                }else if(startTime.equals("")){
                    Toast.makeText(getApplicationContext(), "开始时间未选择", Toast.LENGTH_SHORT).show();

                }else if(endTime.equals("")){
                    Toast.makeText(getApplicationContext(), "结束时间未选择", Toast.LENGTH_SHORT).show();

                }else if(lastTime.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入兼职持续时间", Toast.LENGTH_SHORT).show();

                }else if(numberOfEmployee.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入招工人数", Toast.LENGTH_SHORT).show();

                }else if(salary.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入薪资", Toast.LENGTH_SHORT).show();

                }else if(salaryUnit.equals("")){
                    Toast.makeText(getApplicationContext(), "未选择薪资单位", Toast.LENGTH_SHORT).show();

                }else if(gender.equals("")){
                    Toast.makeText(getApplicationContext(), "未选择性别要求", Toast.LENGTH_SHORT).show();

                }else if(experience.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入经验要求", Toast.LENGTH_SHORT).show();

                }else if(height.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入身高要求", Toast.LENGTH_SHORT).show();

                }else if(description.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入工作描述", Toast.LENGTH_SHORT).show();

                }else if(detail.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入细节描述", Toast.LENGTH_SHORT).show();

                }else if(contactNameOfRelease.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入联系人姓名", Toast.LENGTH_SHORT).show();

                }else if(contactPhoneOfRelease.equals("")){
                    Toast.makeText(getApplicationContext(), "未输入联系人号码", Toast.LENGTH_SHORT).show();

                }else if(addressET.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "未选择联系地址", Toast.LENGTH_SHORT).show();

                }else {
                    new ReleaseThread(detailAddress1, province1, city1, zone1, contactNameOfRelease, contactPhoneOfRelease,
                            datePicker, description, detail, endTime, experience, gender,
                            height, lastTime, numberOfEmployee, salary, salaryUnit, startTime,
                            title, type, releaseURL, releaseHandler, phone).start();
                }
            }
        });

    }


    public void init() {
        datePickerET = (EditText) findViewById(R.id.datePickerET);
        timePickerET = (EditText) findViewById(R.id.TimePickerET);
        spinner = (Spinner) findViewById(R.id.spinner);
        endTimePickerET = (EditText) findViewById(R.id.EndTimePickerET);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        submitButton = (Button) findViewById(R.id.submitButton);
        releaseName = (EditText) findViewById(R.id.releaseNameET);
        lastTimeET = (EditText) findViewById(R.id.TimeLastET);
        numberOfEmployeeET = (EditText) findViewById(R.id.numberOfEmployET);
        salaryET = (EditText) findViewById(R.id.salaryET);
        heightET = (EditText) findViewById(R.id.heightET);
        descriptionET = (EditText) findViewById(R.id.descriptionET);
        detailET =  (EditText) findViewById(R.id.detailET);
        contactNameET =  (EditText) findViewById(R.id.contactNameOfRealeaseET);
        contactPhoneET = (EditText) findViewById(R.id. phoneET);
       addressET = (EditText) findViewById(R.id. contactAddressOfRealeaseET);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode== RESULT_OK){
            if(requestCode==1){

                String addr= (String) data.getSerializableExtra("allAddress");
                addressET.setText(addr);
                province = (String) data.getSerializableExtra("provinceName");

                 city = (String) data.getSerializableExtra("cityName");
                 zone = (String) data.getSerializableExtra("zoneName");
                detailAddress = (String) data.getSerializableExtra("detailAddress");
            }
        }
    }
}
