package com.android.guesswords;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface.OnClickListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //获取对应的控件
        final TextView textView_word=(TextView) findViewById(R.id.textView_words);
        final EditText editText_word=(EditText) findViewById(R.id.editText_words);
        final String[] word_to_guess=GenerateWordToGuess();
        
        StringBuffer buffer=new StringBuffer();
        
        for(int i=0;i<word_to_guess[1].length();i++){
        	 buffer.append(word_to_guess[1].charAt(i));
        	 buffer.append(" ");
        }
        	textView_word.setText(buffer.toString());
            editText_word.setHint(buffer.toString());
        
        Button button_ok=(Button) findViewById(R.id.button_ok); 	   
		    
        button_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String word_edit;
			    word_edit=editText_word.getText().toString();
			    
				if(word_to_guess[0].equals(word_edit)){
					
					Player player=new Player();
					player.setName("player1");
					player.setRightTimes(1);
					String name=player.getName();
					String rightTimes=String.valueOf(player.getRightTimes());
					
					FileOutputStream fos=null;
					 try{
					    	fos=openFileOutput("player", MODE_PRIVATE);
					    	fos.write((name+" "+rightTimes).getBytes());
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
					
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, DialogActivityRight.class);
						intent.putExtra("rightTimes",Integer.parseInt(rightTimes));
						startActivity(intent);
						
			    	}else if("".equals(word_edit)){
			    		Toast.makeText(MainActivity.this,"请输入单词", Toast.LENGTH_SHORT).show();
			
			    	}else if(word_edit.length()<word_to_guess[0].length()){
			    		Toast.makeText(MainActivity.this, "请输入完整单词！", Toast.LENGTH_SHORT).show();
			    	}else if(!word_to_guess[0].equals(word_edit)){
			    		
			    		Intent intent = new Intent();
						intent.setClass(MainActivity.this, DialogActivity.class);
						intent.putExtra("rightWord", word_to_guess[0]);
						startActivity(intent);
			    	}
			}
		});
       } 
	 /*
     * 获取存在的数据文件
     */
    public String getFromAssets(){
    	byte[] buffer=null;
    	   try {
    	InputStream in = getResources().getAssets().open("words.txt");
    	//获取文件的字节数
    	int lenght = in.available();
    	//创建byte数组
    	  buffer = new byte[lenght];
    	//将文件中的数据读到byte数组中
    	in.read(buffer);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	        String result =new String(buffer);
	    	return result;
    }

   
    /*
     * 抽取随机单词
     */
    public String GetRandomWord(){
    	 //获取数据文件
        String[] data = getFromAssets().split(" ");
        //生成一个随机数
        int randomNumber=new Random().nextInt(data.length);
        //抽取一个随机单词
        String randomWord=data[randomNumber];
        return randomWord;
 
    }
    
 
    /* 
     * 生成猜单词条目
     */
    public String[] GenerateWordToGuess(){
    	
    	int lettersToGuess=1;
    	String wordToGuess;
    	String[] wordsArray;
    	String word=null;
    	
    	RandomCollection randomColletion=new RandomCollection();
    	//获取随机抽取所得单词
    	if(lettersToGuess==1){
    		do{
    			 word=GetRandomWord();
    			 
    		}while(word.length()<2);
    	}
    	if(lettersToGuess==3){
    		do{
    			 word=GetRandomWord();
    			 
    		}while(word.length()<5);
    	}
    	if(lettersToGuess==5){
    		do{
    			 word=GetRandomWord();
    			 
    		}while(word.length()<7);
    	}
    	StringBuffer stringBuffer=new StringBuffer(word);
    	
    	List<Integer> randomIntegerList=new ArrayList<Integer>();
    	randomIntegerList=randomColletion.GenerateRandomNumber(
    			word.length(), lettersToGuess);
    	
    	for(int i=0;i<lettersToGuess;i++){
    		stringBuffer.deleteCharAt(randomIntegerList.get(i));
    		stringBuffer.insert(randomIntegerList.get(i),"-");
    	}
    	wordToGuess=stringBuffer.toString();
    	
    	wordsArray=new String[]{word,wordToGuess};
    	return wordsArray;
    }

}
   
   
