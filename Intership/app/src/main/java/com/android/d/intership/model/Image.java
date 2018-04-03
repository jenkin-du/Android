package com.android.d.intership.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Image {

	public Image() {
		
	}
	
	private String name;
	private int length;
	private String imageCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getImageCode() {
		return imageCode;
	}
	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}
	
	
	public void setImageCode(byte[] b) {

		this.imageCode= Base64.encodeToString(b,Base64.DEFAULT);
	}

	/**
	 * 从文件中读取图片
	 * @param filePath  图片路径
     */
	public void setImageCodeFromFile(String filePath){

		File file = new File(filePath);
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			this.length = fis.read(buffer);
			this.imageCode= Base64.encodeToString(buffer,Base64.DEFAULT);

			String Path = Environment.getExternalStorageDirectory().getPath();
			file=new File(Path+"/imageCode.txt");
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(imageCode.getBytes());
			fos.close();

			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *将bitmap转化成加密数据
	 * @param bitmap
     */
	public void setImageByBitmap(Bitmap bitmap){

		ByteArrayOutputStream os=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);

		byte[] buffer=os.toByteArray();

		this.imageCode=Base64.encodeToString(buffer,Base64.DEFAULT);


	}

	/***
	 * 从加密数据中获得bitmap
	 * @return
     */
	public Bitmap getBitmap(){

		byte[] buffer=Base64.decode(imageCode,Base64.DEFAULT);

		return BitmapFactory.decodeByteArray(buffer,0,buffer.length);
	}

	@Override
	public String toString() {
		return "Image{" +
				"name='" + name + '\'' +
				", length=" + length +
				", imageCode='" + imageCode + '\'' +
				'}';
	}
}
