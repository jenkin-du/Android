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
        
        //��ȡ��Ӧ�Ŀؼ�
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
			    		Toast.makeText(MainActivity.this,"�����뵥��", Toast.LENGTH_SHORT).show();
			
			    	}else if(word_edit.length()<word_to_guess[0].length()){
			    		Toast.makeText(MainActivity.this, "�������������ʣ�", Toast.LENGTH_SHORT).show();
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
     * ��ȡ���ڵ������ļ�
     */
    public String getFromAssets(){
    	byte[] buffer=null;
    	   try {
    	InputStream in = getResources().getAssets().open("words.txt");
    	//��ȡ�ļ����ֽ���
    	int lenght = in.available();
    	//����byte����
    	  buffer = new byte[lenght];
    	//���ļ��е����ݶ���byte������
    	in.read(buffer);
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	        String result =new String(buffer);
	    	return result;
    }

   
    /*
     * ��ȡ�������
     */
    public String GetRandomWord(){
    	 //��ȡ�����ļ�
        String[] data = getFromAssets().split(" ");
        //����һ�������
        int randomNumber=new Random().nextInt(data.length);
        //��ȡһ���������
        String randomWord=data[randomNumber];
        return randomWord;
 
    }
    
 
    /* 
     * ���ɲµ�����Ŀ
     */
    public String[] GenerateWordToGuess(){
    	
    	int lettersToGuess=1;
    	String wordToGuess;
    	String[] wordsArray;
    	String word=null;
    	
    	RandomCollection randomColletion=new RandomCollection();
    	//��ȡ�����ȡ���õ���
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
   
   
