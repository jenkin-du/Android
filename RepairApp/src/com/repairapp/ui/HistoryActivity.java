package com.repairapp.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.repairapp.tool.HttpConfig;
import com.repairapp.tool.HttpURLHandler;
import com.repairapp.tool.HttpURLTask;
import com.repairapp.tool.JSONParser;

public class HistoryActivity extends Activity {

	private ListView mListView;
	SimpleAdapter _Adapter = null;
	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map = new HashMap<String, String>();
	private String username;

	private static final String TAG = "MainActivity";

	private HttpURLHandler handler = new HttpURLHandler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			// 此处更新主UI
			if (msg.what == 200) {

				String jsonString = (String) msg.obj;

				if (jsonString != null) {
					data = (ArrayList<HashMap<String, String>>) JSONParser
							.toJavaBean(
									jsonString,
									new TypeToken<ArrayList<HashMap<String, Object>>>() {
									}.getType());

					updateList();
				}else {
					Toast.makeText(HistoryActivity.this, "网络错误！！",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(HistoryActivity.this, "网络错误！！",
						Toast.LENGTH_SHORT).show();
			}

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// 设置线程的策略
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		// 设置虚拟机的策略
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		mListView = (ListView) findViewById(R.id.listView);

		// 获得历史信息
		getOrders();
		mListView.setOnItemClickListener(new OnItemClickListenerImpl());
	}

	/**
	 * 跟新列表
	 */
	private void updateList() {

		// 初始化适配器，并且绑定数据
		_Adapter = new SimpleAdapter(HistoryActivity.this, data,
				R.layout.itemlayout, new String[] { "orders",

				"thing", "name", "state" }, new int[] { R.id.orders,
						R.id.thing, R.id.name, R.id.state });
		mListView.setAdapter(_Adapter);

	}

	

	// 加载列表数据
	public void getOrders() {
		String url = HttpConfig.URL_HEADER + "/HistoryServlet";

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("userName", username);
		HttpURLTask task = new HttpURLTask(url, param, handler);
		task.start();

	}


	// 每行单击事件响应监听
	private class OnItemClickListenerImpl implements OnItemClickListener {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<String, String> mapItem = (Map<String, String>) HistoryActivity.this._Adapter
					.getItem(position);
			// String order = mapItem.get("orders");
			// order_info(order);
			// String name = map.get("describes");
			StringBuilder sb = new StringBuilder();
			sb.append("报修物品: " + mapItem.get("thing") + "\n" + "\n");
			sb.append("故障描述: " + mapItem.get("describes") + "\n" + "\n");
			sb.append("报修地点: " + mapItem.get("address") + "\n" + "\n");
			sb.append("报修人员: " + mapItem.get("name") + "\n" + "\n");
			sb.append("联系电话: " + mapItem.get("tel") + "\n" + "\n");
			sb.append("报修时间: " + mapItem.get("time") + "\n" + "\n");
			sb.append("报修工单: " + mapItem.get("orders") + "\n" + "\n");
			sb.append("维修状态: " + mapItem.get("state") + "\n");

			AlertDialog.Builder builder = new Builder(HistoryActivity.this);
			builder.setTitle(mapItem.get("orders") + "工单详细信息")
					.setMessage(sb.toString())
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).create().show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
