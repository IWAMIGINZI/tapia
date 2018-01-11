package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.tapia.mji.demo.Actions.MySimpleAction;
import com.tapia.mji.demo.R;
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

public class IwataniMenuActivity extends TapiaActivity implements View.OnClickListener {

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
        //「1分後」セット
        timelag.setting(timelag.move(activity));
    }

    @Override
    protected void onPause(){
        super.onPause();
        //ハンドラのキャンセル
        timelag.cancel();
    }

    //ボタン処理(「戻る」ボタン以外)
    public void onClick(View view){
        //naisenに飛ばすためのflagを設定
        Intent intent = new Intent(this, NaisenActivity_2.class);
        switch(view.getId()){
            case R.id.daiiti:
                intent.putExtra("flag", "1");
                break;
            case R.id.daini:
                intent.putExtra("flag", "2");
                break;
            case R.id.daisan:
                intent.putExtra("flag", "3");
                break;
            case R.id.gizyutu:
                intent.putExtra("flag", "4");
                break;
            case R.id.kanri:
                intent.putExtra("flag", "5");
                break;
            case R.id.support:
                intent.putExtra("flag", "6");
                break;
        }
        //NaisenActivity_2にflagを持って飛ぶ
        startActivity(intent);
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
}
