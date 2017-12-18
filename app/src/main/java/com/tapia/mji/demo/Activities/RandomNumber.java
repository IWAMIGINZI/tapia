package com.tapia.mji.demo.Activities;

import java.util.Random;

/**
 * Created by ais75114 on 2017/10/04.
 */

public class RandomNumber {

    //TAPIA表情決めの乱数設定

    public int RN(){
        //毎回違う乱数を出現させる(animationを止めるため)

        Random random=new Random();
        int n=random.nextInt(14);
        return n;

/*        double n=Math.floor(Math.random()*10);
        int rn=(int)n;
        return rn;*/

    }
}
