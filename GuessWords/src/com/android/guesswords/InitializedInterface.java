package com.android.guesswords;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class InitializedInterface extends Activity implements Thread.UncaughtExceptionHandler{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial);
		
		//获得相应的控件
		final TextView txt_start=(TextView) findViewById(R.id.txt_start);
		final TextView txt_set=(TextView) findViewById(R.id.txt_set);
		final TextView txt_exit=(TextView) findViewById(R.id.txt_exit);
		
		int i=1;
		fileInLetters(i);
		
		outInitialRank();
		

		
		Player player=new Player();
		
		player.setName("player");
		player.setRightTimes(0);
		player.setWrongTimes(0);
		player.setTimes(0);
		player.setTotalRightTimes(0);
		
		fileInTemp("player", player);
		
		initializeStarShown();
		
		txt_start.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
								
				Intent intent=new Intent();
				intent.setClass(InitializedInterface.this, MainActivity.class);
				startActivity(intent);
				
				return false;
			}
		});
		
		txt_exit.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
//				
//				int i=0;
//				txt_exit.setText(i);
//				ActivityManager activityMgr= (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//
//				activityMgr.restartPackage(getPackageName());
//				finish();
				return false;
			}
		});
		
		txt_set.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Intent intent=new Intent();
				intent.setClass(InitializedInterface.this, OptionsActivity.class);
				startActivity(intent);
				return false;
			}
		});
	}


	

	/**
	 * 写入临时玩家数据
	 * @param fileName
	 * @param player
	 */
	public void fileInTemp(String fileName,Player player){

		FileOutputStream fos=null;
	    String name=player.getName();
	    int rightTimes=player.getRightTimes();
	    int wrongTimes=player.getWrongTimes();
	    int totalRightTimes=player.getTotalRightTimes();
	    int times=player.getTimes();
		 try{
		    	fos=openFileOutput(fileName,MODE_PRIVATE);
		  
				fos.write((name+" "+String.valueOf(rightTimes)+" "+wrongTimes
		    			+" "+String.valueOf(totalRightTimes)+" "+
		    			String.valueOf(times)).getBytes());
		    	fos.flush();
		    }catch(FileNotFoundException e){
		    	e.printStackTrace();
		    }catch(IOException e){
		    	e.printStackTrace();
		    }finally{
		    	if(fos!=null){
		    		try{
		    			fos.close();
		    		}catch(IOException e){
		    			e.printStackTrace();
		    		}
		    	}
		    }
	} 
    
	/**
	 *初始化显示星星的个数
	 */
	public void initializeStarShown(){
		
		String[] stars=new String[]{
			"true","true" ,"true","false","false"	
		};
		
		FileOutputStream fos=null;
		 try{
		    	fos=openFileOutput("stars",MODE_PRIVATE);
		    	fos.write((stars[0]+" "+stars[1]+" "+stars[2]+" "
		    			+stars[3]+" "+stars[4]).getBytes());
		    	fos.flush();
		    }catch(FileNotFoundException e){
		    	e.printStackTrace();
		    }catch(IOException e){
		    	e.printStackTrace();
		    }finally{
		    	if(fos!=null){
		    		try{
		    			fos.close();
		    		}catch(IOException e){
		    			e.printStackTrace();
		    		}
		    	}
		    }
	}
	
	/**
	 * 写入猜字母个数的记录
	 */
	public void fileInLetters(int letters){
		
		FileOutputStream fos=null;
		 try{
		    	fos=openFileOutput("letter",MODE_PRIVATE);
		    	fos.write((String.valueOf(letters)).getBytes());
		    	fos.flush();
		    }catch(FileNotFoundException e){
		    	e.printStackTrace();
		    }catch(IOException e){
		    	e.printStackTrace();
		    }finally{
		    	if(fos!=null){
		    		try{
		    			fos.close();
		    		}catch(IOException e){
		    			e.printStackTrace();
		    		}
		    	}
		    }
	}


	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
//		finish();
		AlertDialog alert=new AlertDialog.Builder(InitializedInterface.this).create();
		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle("tips");
		alert.setMessage("Do you really want to exit?");
		alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				
			}
		});
	}

	/**
	 *初始化排名
	 */
	public void outInitialRank(){
        String playerInfo="";
		
		FileOutputStream fosa=null;
		 try{
		    	fosa=openFileOutput("information",MODE_APPEND);
		    	fosa.write((playerInfo).getBytes());
		    	fosa.flush();
		    }catch(FileNotFoundException e){
		    	e.printStackTrace();
		    }catch(IOException e){
		    	e.printStackTrace();
		    }finally{
		    	if(fosa!=null){
		    		try{
		    			fosa.close();
		    		}catch(IOException e){
		    			e.printStackTrace();
		    		}
		    	}
		    }
	

	}
}
