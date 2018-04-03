package com.repairapp.tool;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpEntryTask<T> extends Thread {

	private Handler mHandler;
	private T mT;
	private String mURL;

	public HttpEntryTask(String url, Handler handler, T t) {
		this.mHandler = handler;
		this.mT = t;
		this.mURL = url;
	}

	@Override
	public void run() {

		Message msg = new Message();

		String data = JSONParser.toJSONString(mT);

		URL url;

		HttpURLConnection conn = null;
		InputStream is = null;

		try {
			url = new URL(mURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setReadTimeout(50000);
			conn.setUseCaches(false);

			conn.connect();
			
			OutputStream out = conn.getOutputStream();
			out.write(data.getBytes());
			out.flush();
			out.close();

			int responseCode = conn.getResponseCode();
			Log.i("code", String.valueOf(responseCode));
			String result = "";

			if (responseCode == 200) {
				is = conn.getInputStream();
				// 将结果解析成字符串
				result = StreamParser.parseInputStream(is);
			}
			// 将结果封装成消息，发送出去
			msg.obj = result;
			msg.what = responseCode;// 将网络响应码传递出去
			mHandler.sendMessage(msg);

			if (is != null) {
				is.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

}
