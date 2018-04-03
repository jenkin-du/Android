package com.android.guesswords;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class OptionsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] options =new String[]{
				"The Rank","Select the Level","The Rule","About"
		};
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,options);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i("option", String.valueOf(position));
		if(position==0){
			
			Intent intent=new Intent();
			intent.setClass(OptionsActivity.this, RankActivity.class);
			startActivity(intent);
		}
		
		if(position==1){
			
			chooseTheLevel();
		}
	}

	/**
	 * 读取猜字母个数记录
	 */
	public int fileOutLetters(){
		
		FileInputStream fis=null;
		byte[] buffer=null;
		
		try{
			fis=openFileInput("letter");
			buffer=new byte[fis.available()];
			fis.read(buffer);
		}catch(FileNotFoundException e){
	    	e.printStackTrace();
	    }catch(IOException e){
	    	e.printStackTrace();
	    }finally{
	    	if(fis!=null){
	    		try{
	    			fis.close();
	    		}catch(IOException e){
	    			e.printStackTrace();
	    		}
	    	}
	    }
		
		int letter=Integer.parseInt(new String(buffer).toString());
		return letter;
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
	
	/**
	 * Choose the level
	 */
	
	public void chooseTheLevel(){
		
		final String[] levels=new String[]{
				"Simple","Ordinary","Difficult"
		};
		int level = 0;
		if(fileOutLetters()==1){
			level=0;
		}else if(fileOutLetters()==3){
			level=1;
		}else if(fileOutLetters()==5){
			level=2;
		}
	    Builder builder= new AlertDialog.Builder(OptionsActivity.this);
	    builder.setIcon(R.drawable.ic_launcher);
	    builder.setTitle("Choose the Level");
	    builder.setSingleChoiceItems(levels, level, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0){
					Toast.makeText(OptionsActivity.this, "The simple level is chosen",
                            Toast.LENGTH_SHORT).show();
			        fileInLetters(1);
				}else if(which==1){
					Toast.makeText(OptionsActivity.this, "The ordinary level is chosen",
                            Toast.LENGTH_SHORT).show();
					fileInLetters(3);
				}else if(which==2){
					Toast.makeText(OptionsActivity.this, "The difficult level is chosen",
                            Toast.LENGTH_SHORT).show();
					fileInLetters(5);
				}
								
			}
		});
	    builder.setPositiveButton("ok", null);
	    builder.create().show();
	}
}
