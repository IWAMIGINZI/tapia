package com.tapia.mji.demo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by ais75114 on 2017/10/11.
 */

/*時間が経つと待機画面へ戻る処理*/


public class Timelag{

    //ハンドラの宣言
    final Handler handler=new Handler();

    //画面遷移処理
    public Runnable move(final Activity activity){
        final Runnable page=new Runnable() {
            @Override
            public void run() {
                //待機画面へ遷移
                activity.startActivity(new Intent(activity,SleepActivity.class));
            }
        };
        return page;
    }

    //ハンドラのキャンセル処理
    public void cancel(){
        handler.removeCallbacksAndMessages(null);
    }

    //ハンドラの設定
    public void setting(Runnable page){
        //放置時間の設定
        int time=60000;
        handler.postDelayed(page,time);
    }
}
