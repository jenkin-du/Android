package com.android.milkapp2.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Image {

	public Image() {
		
	}


	private String ImageName;
	private int Length;
	private String ImageCode;

	public String getImageName() {
		return ImageName;
	}

	public void setImageName(String imageName) {
		this.ImageName = imageName;
	}

	public int getLength() {
		return Length;
	}

	public void setLength(int length) {
		this.Length = length;
	}

	public String getImageCode() {
		return ImageCode;
	}

	public void setImageCode(String imageCode) {
		this.ImageCode = imageCode;
	}

	public void setImageCode(byte[] b) {

		this.ImageCode = Base64.encodeToString(b,Base64.DEFAULT);
	}

	/**
	 * 浠庢枃浠朵腑璇诲彇鍥剧墖
	 * @param filePath  鍥剧墖璺緞
     */
	public void setImageCodeFromFile(String filePath){

		File file = new File(filePath);
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			this.Length = fis.read(buffer);
			this.ImageCode = Base64.encodeToString(buffer,Base64.DEFAULT);

			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *灏哹itmap杞寲鎴愬姞瀵嗘暟鎹�
	 * @param bitmap
     */
	public void setImageByBitmap(Bitmap bitmap){

		ByteArrayOutputStream os=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);

		byte[] buffer=os.toByteArray();

		this.ImageCode =Base64.encodeToString(buffer,Base64.DEFAULT);


	}

	/***
	 * 浠庡姞瀵嗘暟鎹腑鑾峰緱bitmap
	 * @return
     */
	public Bitmap getBitmap(){

		byte[] buffer=Base64.decode(ImageCode,Base64.DEFAULT);

		return BitmapFactory.decodeByteArray(buffer,0,buffer.length);
	}


}
