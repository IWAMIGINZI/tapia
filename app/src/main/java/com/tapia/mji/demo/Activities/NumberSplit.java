package com.tapia.mji.demo.Activities;

/**
 * Created by ais75114 on 2018/01/25.
 */

public class NumberSplit {

    public String Execution(String number){
        String new_number[]=new String[8] ;
        StringBuilder sb=new StringBuilder();

        for(int i=0;i<new_number.length-1;i++){
            if(i%2==0){
                new_number[i]=String.valueOf(number.charAt(i/2));
            }else{
                new_number[i]=" ";
            }
            sb.append(new_number[i]);
        }

        return String.valueOf(sb);
    }
}
