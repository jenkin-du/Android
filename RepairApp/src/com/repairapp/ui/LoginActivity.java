package com.repairapp.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.repairapp.tool.ExitAppUtils;
import com.repairapp.tool.HttpConfig;
import com.repairapp.tool.HttpURLHandler;
import com.repairapp.tool.HttpURLTask;

public class LoginActivity extends Activity {

	private EditText txUserName;
	private EditText txPassword;
	private Button btnRegister;
	private Button btnLogin;
	String result = null;
	private long exitTime = 0;

	private HttpURLHandler handler = new HttpURLHandler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 200) {
				String ok = (String) msg.obj;
				
				Log.i("LoginActivity", "handleMessage--reponse="+ok);
				if (ok.equals("true")) {
					Toast.makeText(LoginActivity.this, "登录成功！",
							Toast.LENGTH_SHORT).show();
					String username = txUserName.getText().toString();
					Intent intent = new Intent(LoginActivity.this,
							MenuActivity.class);
					intent.putExtra("username", username);
					startActivity(intent);
				}else {
					Toast.makeText(LoginActivity.this, "用户名或密码错误！",
							Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(LoginActivity.this, "网络错误！",
						Toast.LENGTH_SHORT).show();
			}

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitAppUtils.getInstance().addActivity(this);

		setContentView(R.layout.activity_login);

		initView();

		setListener();
	}

	/**
	 * 
	 */
	private void initView() {
		btnLogin = (Button) findViewById(R.id.btnLogin);
		txUserName = (EditText) findViewById(R.id.UserName);
		txPassword = (EditText) findViewById(R.id.textPasswd);
		btnRegister = (Button) findViewById(R.id.btnRegister);
	}

	/**
	 * 
	 */
	private void setListener() {
		//
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName = txUserName.getText().toString();
				String password = txPassword.getText().toString();
				//loginRemoteService(userName, password);
				Intent intent = new Intent(LoginActivity.this,
						MenuActivity.class);
				String username = txUserName.getText().toString();
				intent.putExtra("username", username);
				startActivity(intent);
				
			}
		});
		//
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 
	 * 
	 * @param userName
	 * @param password
	 */
	public void loginRemoteService(String userName, String password) {

		if(userName.length()<11){
			Toast.makeText(LoginActivity.this, "手机号太短！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("password", password);

		String url = HttpConfig.URL_HEADER + "LoginServlet";

		HttpURLTask task = new HttpURLTask(url, params, handler);
		task.start();

	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				ExitAppUtils.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
