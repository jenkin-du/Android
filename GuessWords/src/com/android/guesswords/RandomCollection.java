package com.android.guesswords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomCollection {
	/*
	 * 生成多个随机数
	 */
	public List<Integer> GenerateRandomNumber(int word_length,int lettersToGuess){
		
		List<Integer> randomIntegerList=new ArrayList<Integer>();
		Random random=new Random();
		Integer random_i;
		Integer random_k;
    		if(word_length==2){
    			
    			
    			 random_i=random.nextInt(2);
    			
    			randomIntegerList.add(random_i);
    		}
    		
    		if(word_length==3||word_length==4){
    			if(lettersToGuess==1){
    				
        		    random_i=random.nextInt(word_length);
        			
        			randomIntegerList.add(random_i);
    			}
    			if(lettersToGuess==3){
    				
    				for(int i=0;i<lettersToGuess;i++){
    					do{
    						random_k=random.nextInt(word_length);
    					}while(randomIntegerList.contains(random_k));
    					
	    				randomIntegerList.add(random_k);
    				}
    			}
    		}
    		
    		if(word_length==5||word_length==6){
    			
    			if(lettersToGuess==1){
    				
                    random_i=random.nextInt(word_length);
        			
        			randomIntegerList.add(random_i);
    			}
    			
    			if(lettersToGuess==3){
    				
    				for(int i=0;i<lettersToGuess;i++){
    					do{
    						random_k=random.nextInt(word_length);
    					}while(randomIntegerList.contains(random_k));
    					
	    				randomIntegerList.add(random_k);
    				}
    			}
    		}
    		
    		if(word_length>=7){
    			
    			if(lettersToGuess==1){
    				
        		    random_i=random.nextInt(word_length);
        			
        			randomIntegerList.add(random_i);
       			}
       			
       			if(lettersToGuess==3){
       				
       				for(int i=0;i<lettersToGuess;i++){
    					do{
    						random_k=random.nextInt(word_length);
    					}while(randomIntegerList.contains(random_k));
    					
	    				randomIntegerList.add(random_k);
    				}
       			}
       			
       			if(lettersToGuess==5){
       				
       				for(int i=0;i<lettersToGuess;i++){
    					do{
    						random_k=random.nextInt(word_length);
    					}while(randomIntegerList.contains(random_k));
    					
	    				randomIntegerList.add(random_k);
    				}
       			}
    		}
    	Collections.sort(randomIntegerList);
		return randomIntegerList;		
	}
}
