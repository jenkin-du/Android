package com.repairapp.ui;

import java.util.HashMap;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.repairapp.tool.ExitAppUtils;
import com.repairapp.tool.HttpConfig;
import com.repairapp.tool.HttpURLHandler;
import com.repairapp.tool.HttpURLTask;

public class RegisterActivity extends Activity {

	/** 账号 */
	private EditText newUserName;
	/** 新密码 */
	private EditText newPassWord;
	/** 确认密码 */
	private EditText confirmPassWord;
	private String userName;
	private String password;
	private Button btnRegister;
	String result = null;

	private HttpURLHandler handler = new HttpURLHandler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 200) {
				if(((String)msg.obj).equals("true")){
					Toast.makeText(RegisterActivity.this, "注册成功！请登录",
							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}else {
					Toast.makeText(RegisterActivity.this, "用户名已注册！请直接登录",
							Toast.LENGTH_SHORT).show();
				}
				

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ExitAppUtils.getInstance().addActivity(this);
		// 设置初始化视图
		initViews();
		// 设置事件监听器方法
		setListener();

	}

	private void initViews() {
		newUserName = (EditText) findViewById(R.id.activity_register_username);
		newPassWord = (EditText) findViewById(R.id.activity_register_newpassword);
		confirmPassWord = (EditText) findViewById(R.id.activity_register_confirpassword);
		btnRegister = (Button) findViewById(R.id.activity_register_confirm);

	}

	/**
	 * 设置事件的监听器的方法
	 */
	private void setListener() {
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				userName = newUserName.getText().toString();
				password = newPassWord.getText().toString();

				if (userName.length() < 11) {
					Toast.makeText(RegisterActivity.this, "手机格式错误",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// 注册
				doRegister(userName, password);

			}
		});
	}

	/***
	 * 注册
	 * 
	 * @param userName
	 * @param password
	 */
	public void doRegister(String userName, String password) {

		// 创建提示框提醒是否注册成功
		if (newUserName.getText().toString().equals("")
				|| newPassWord.getText().toString().equals("")
				|| confirmPassWord.getText().toString().equals("")) {

			Toast.makeText(RegisterActivity.this, "账号或者密码不能为空!",
					Toast.LENGTH_SHORT).show();

		} else if (!newPassWord.getText().toString()
				.equals(confirmPassWord.getText().toString())) {

			Toast.makeText(RegisterActivity.this, "两次密码输入不一致，请重新输入！",
					Toast.LENGTH_SHORT).show();
		} else {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userName", userName);
			params.put("password", password);

			String url = HttpConfig.URL_HEADER + "/RegisterServlet";

			HttpURLTask task = new HttpURLTask(url, params, handler);
			task.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
						.addNextIntentWithParentStack(upIntent)
						.startActivities();
			} else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
