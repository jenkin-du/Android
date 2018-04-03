package com.android.guesswords;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class RankActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.rank);
		
		List<Player> playerList = OutInformation();
		
		Log.i("singlePlayer", "  ");
		
		if(playerList.size()==0){
			
			String[] empty=new String[]{"There is no ranking!"};
			
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
					android.R.layout.simple_expandable_list_item_1,empty);
			
			setListAdapter(adapter);
			
		}else{

			String[] singlePlayer=new String[5];
			int i;
			for( i=0;i<playerList.size()&&i<5;i++){
				singlePlayer[i]=playerList.get(i).getName()
						+" "+playerList.get(i).getScore();
			}
		
			for(;i<5;i++){
				singlePlayer[i]="";
			}
			
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,singlePlayer);
			Log.i("adapter", "  ");
			setListAdapter(adapter);
		}
		Log.i("last", "  ");
	}

	/**
	 * 读取玩家信息
	 */
	
	public List<Player> OutInformation(){
		
		Player player=new Player();
		List<Player> playerList=new ArrayList<Player>();
		
		FileInputStream fis=null;
		byte[] buffer=null;
		
		try{
			fis=openFileInput("information");
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
		
		String information=new String(buffer);
		String[] singlgPlayerInfo=information.split(" ");
		Log.i("information", String.valueOf(singlgPlayerInfo.length));
		for(int i=0;i<information.length();i+=2){
			player.setName(singlgPlayerInfo[i]);
			player.setScore(Double.valueOf(singlgPlayerInfo[i+1]));
			playerList.add(player);
		}
		Log.i("player", String.valueOf(playerList.size()));
		Collections.sort(playerList);
		return playerList;
		
	}
}
