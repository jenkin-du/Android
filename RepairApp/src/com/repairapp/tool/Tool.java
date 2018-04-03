package com.repairapp.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.repairapp.ui.LoginActivity;

import android.app.Activity;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;

public class Tool extends Activity {

	/**
	 * 按下这个按钮进行的颜色过滤
	 */
	public final static float[] BT_SELECTED = new float[] { 2, 0, 0, 0, 2, 0,
			2, 0, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 1, 0 };

	/**
	 * 按钮恢复原状的颜色过滤
	 */
	public final static float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

	/**
	 * 按钮焦点改变
	 */
	public final static OnFocusChangeListener buttonOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			} else {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_NOT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			}
		}
	};

	/**
	 * 按钮触碰按下效果
	 */
	public final static OnTouchListener buttonOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.getBackground().setColorFilter(
						new ColorMatrixColorFilter(BT_NOT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			}
			return false;
		}
	};

	/**
	 * 设置图片按钮获取焦点改变状态
	 * 
	 * @param inImageButton
	 */
	public final static void setButtonFocusChanged(View inView) {
		inView.setOnTouchListener(buttonOnTouchListener);
		inView.setOnFocusChangeListener(buttonOnFocusChangeListener);
	}

	public static void uploadImage(String filePath,String order) {
		
		UploadThread upload = new UploadThread(filePath,order);
		upload.start();
	}

}

class UploadThread extends Thread {

	String filePath;
	String order;

	public UploadThread(String filePath,String order) {
		this.filePath = filePath;
		this.order=order;
	}

	@Override
	public void run() {

		URL url;
		HttpURLConnection conn;
		try {

			File file = new File(filePath);
			InputStream fis = new FileInputStream(file);

			int size = (int) file.length();
			byte[] data = new byte[size];

			fis.read(data, 0, size);
			int lastIndex = filePath.lastIndexOf("/");
			url = new URL(HttpConfig.URL_HEADER
					+ "ImageSevrlet?upload=image&imageName="
					+ filePath.substring(lastIndex)+"&order="+order);

			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestProperty(
					"Accept",
					"image/gif,   image/x-xbitmap,   image/jpeg,   image/pjpeg,   application/vnd.ms-excel,   application/vnd.ms-powerpoint,   application/msword,   application/x-shockwave-flash,   application/x-quickviewplus,   */*");
			conn.setRequestProperty("Accept-Language", "zh-cn");
			conn.setRequestProperty("Content-type",
					"multipart/form-data;   boundary=---------------------------7d318fd100112");
			conn.setRequestProperty("Accept-Encoding", "gzip,   deflate");
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0   (compatible;   MSIE   6.0;   Windows   NT   5.1)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(50000);

			OutputStream los = conn.getOutputStream();
			los.write(data);

			los.flush();
			los.close();
			fis.close();

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				String line = "";
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}

				reader.close();
				is.close();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
