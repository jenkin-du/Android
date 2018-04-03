package com.android.guesswords;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShowFailure extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_failure);
		
		TextView txt_score2=(TextView) findViewById(R.id.txt_scores2);
		Button button=(Button) findViewById(R.id.button1);
		final EditText name=(EditText) findViewById(R.id.name2);
		
		Intent intent=getIntent();
		Bundle bundle= intent.getExtras();
		final double score=bundle.getDouble("score");
		
		txt_score2.setText(String.valueOf(score));
		if(!name.equals("")){
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Player player=new Player();
					player.setName(name.getText().toString());
					player.setScore(score);
					appendInfomation(player);
					
					Intent intent =new Intent();
					intent.setClass(ShowFailure.this, InitializedInterface.class);
					startActivity(intent);
					
				}
			});
		}
	}
	
	/**
	 * 写入玩家的得分
	 * @param player
	 */
	public void appendInfomation(Player player){
		
		String playerInfo= player.getName()+" "+String.valueOf(player.getScore())+" ";
		
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
