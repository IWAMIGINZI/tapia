package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tapia.mji.demo.Actions.MySimpleAction;
import com.tapia.mji.demo.R;
import com.tapia.mji.demo.Tools.LockedWork;
import com.tapia.mji.demo.Tools.Locker;
import com.tapia.mji.tapialib.Actions.SimpleAction;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Exceptions.LanguageNotSupportedException;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.STTProvider;
import com.tapia.mji.tapialib.Providers.Interfaces.TTSProvider;
import com.tapia.mji.tapialib.TapiaApp;
import com.tapia.mji.tapialib.Utils.TapiaAnimation;
import com.tapia.mji.tapialib.Utils.TapiaRobot;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ais75114 on 2017/09/19.
 */

public class IwataniMenuActivity extends TapiaActivity implements View.OnClickListener, LockedWork {

    /*一定時間経つと待機画面へ戻る処理*/
    Timelag timelag=new Timelag();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //メニュー画面表示
        setContentView(R.layout.activity_iwatanimenu_2);

        /*宣言部*********************************************************************************/

        //日本語の設定
        TapiaApp.setCurrentLanguage(Language.LanguageID.JAPANESE);
        ttsProvider=TapiaApp.currentLanguage.getTTSProvider();

        /*話す*********************************************************************************/

        try {
            ttsProvider.say(getString(R.string.welcome));
        } catch (LanguageNotSupportedException e) {
            e.printStackTrace();
        }
        ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
            @Override
            public void onSpeechComplete(){
                //終話後の処理
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Locker.whenNaisenWorking(this);
        //「1分後」セット
        timelag.setting(timelag.move(activity));
    }

    @Override
    protected void onPause(){
        super.onPause();
        //ハンドラのキャンセル
        timelag.cancel();
        Locker.whenNaisenWorking(this);
    }

    //ボタン処理(「戻る」ボタン以外)
    public void onClick(View view){
        //遷移先画面
        Intent intent = new Intent(this, NaisenActivity_2.class);
        Intent intent2 = new Intent(this, NaisenKakudaiActivity.class);
        //内線番号取得
        ExtensionNumber en=new ExtensionNumber();

        //flagを設定
        switch(view.getId()){
            case R.id.daiiti:
                intent.putExtra("flag", "1");
                startActivity(intent);
                break;
            case R.id.daini:
                intent.putExtra("flag", "2");
                startActivity(intent);
                break;
            case R.id.support:
                intent2.putExtra("flag", "3");
                intent2.putExtra("number", en.Supportdesk);
                startActivity(intent2);
                break;
            case R.id.gizyutu:
                intent.putExtra("flag", "4");
                startActivity(intent);
                break;
            case R.id.kanri:
                intent2.putExtra("flag", "5");
                intent2.putExtra("number", en.Kanri);
                startActivity(intent2);
                break;
            case R.id.Projecttokatu:
                intent.putExtra("flag", "6");
                startActivity(intent);
                break;
        }

    }

    //「戻る」ボタン処理
    public void onbackbuttonClick(View view){
        switch (view.getId()){
            case R.id.back:
                //一つ前の画面に遷移
                startActivity(new Intent(activity, SleepActivity.class));
                break;
        }
    }

    public void work() {
        Locker.setWorker(Locker.WORKER_DEFAULT);
    }

    public void workElse() {
        Locker.setWorker(Locker.WORKER_NAISEN);
    }
}
