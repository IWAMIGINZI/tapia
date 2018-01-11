package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tapia.mji.demo.R;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Exceptions.LanguageNotSupportedException;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.TTSProvider;
import com.tapia.mji.tapialib.TapiaApp;

/**
 * Created by ais75114 on 2018/01/09.
 */

public class NaisenKakudaiSiteiActivity extends TapiaActivity{

    //時間が経つと遷移画面へ戻る処理の部品宣言
    Timelag timelag=new Timelag();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naisenkakudai);

        /*宣言部***********************************************************************************/

        //パーツ
        TextView title=(TextView)findViewById(R.id.zigyoubuname);   //名前表示用
        TextView naisenbangou=(TextView) findViewById(R.id.naisenbangou);   //内線番号表示用

        //SleepActivityから名前と内線番号を受け取る
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");  //名前
        String number=intent.getStringExtra("number");  //内線番号

        //日本語設定
        TapiaApp.setCurrentLanguage(Language.LanguageID.JAPANESE);
        ttsProvider=TapiaApp.currentLanguage.getTTSProvider();

        /*画面表示処理*****************************************************************************/

        title.setText(name);
        naisenbangou.setText(number);

        /*話す***********************************************************************************/

        try{
            ttsProvider.say("内線番号は、"+number+"です");
            //ttsProvider.say(getString(R.string.attention2));
        }catch (LanguageNotSupportedException e){
            e.printStackTrace();
        }
        ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                //終話後の処理
            }
        });

        /*アイドル処理*****************************************************************************/

        //(Textview)タップ処理
        findViewById(R.id.naisenbangou).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //画面をタップしている間は待機画面へ遷移しない
                //ハンドラキャンセル
                timelag.cancel();
                //ハンドラ再設定
                timelag.setting(timelag.move(activity));
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        //1分後待機画面に戻る
        timelag.setting(timelag.move(activity));
    }

    @Override
    protected void onPause(){
        super.onPause();
        //ハンドラのキャンセル
        timelag.cancel();
    }

    //「戻る」ボタンの処理
    public void onbackbuttonClick(View view){
        switch (view.getId()){
            case R.id.back:
                //1つ前の画面に戻る
                startActivity(new Intent(activity, SleepActivity.class));
                break;
        }
    }
}
