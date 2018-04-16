package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tapia.mji.demo.Actions.MySimpleAction;
import com.tapia.mji.demo.R;
import com.tapia.mji.tapialib.Actions.SimpleAction;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Exceptions.LanguageNotSupportedException;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.STTProvider;
import com.tapia.mji.tapialib.Providers.Interfaces.TTSProvider;
import com.tapia.mji.tapialib.TapiaApp;
import com.tapia.mji.tapialib.Utils.TapiaRobot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ais75114 on 2017/09/22.
 */

public class NaisenKakudaiActivity extends TapiaActivity {

    //時間が経つと遷移画面へ戻る処理の部品宣言
    Timelag timelag=new Timelag();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //内線拡大レイアウト表示
        setContentView(R.layout.naisenkakudai);

        /*宣言部***********************************************************************************/

        //パーツ
        TextView title=(TextView)findViewById(R.id.zigyoubuname);
        TextView naisenbangou=(TextView) findViewById(R.id.naisenbangou);

        //日本語設定
        TapiaApp.setCurrentLanguage(Language.LanguageID.JAPANESE);
        ttsProvider=TapiaApp.currentLanguage.getTTSProvider();

        /*話す***********************************************************************************/

        try {
            ttsProvider.say(getString(R.string.attention2));
        } catch (LanguageNotSupportedException e) {
            e.printStackTrace();
        }
        ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
            @Override
            public void onSpeechComplete(){
                //終話後の処理
            }
        });

        /*画面表示処理*****************************************************************************/

        //flagとnumberを受け取る
        Intent intent=getIntent();
        String data=intent.getStringExtra("flag");
        String number=intent.getStringExtra("number");

        //内線番号の表示
        naisenbangou.setText(number);

        //NaisenActivity_2からのflagで分岐し、押されたボタンの内線番号を拡大表示する
        //第一事業部
        if(data.matches("1"+".*")){
            switch (data){
                case "11":
                    //WINサポート部
                    title.setText(R.string.WINsupport);
                    break;
                case "12":
                    //WIN-Eサポート部
                    title.setText(R.string.WINEsupport);
                    break;
                case "13":
                    //開発部
                    title.setText(R.string.kaihatu);
                    break;
            }
        }
        //第二事業部
        if(data.matches("2"+".*")){
            switch (data){
                case "21":
                    //システムサポート部
                    title.setText(R.string.Syssupport);
                    break;
                case "22":
                    //開発部
                    title.setText(R.string.kaihatu);
                    break;
            }
        }
        //サポートデスク
        if(data.equals("3")){
            title.setText(R.string.support);
        }
        //技術本部
        if(data.matches("4"+".*")){
            switch (data){
                case "41":
                    //システムサポート部
                    title.setText(R.string.kiban);
                    break;
                case "42":
                    //開発部
                    title.setText(R.string.ict);
                    break;
            }
        }
        //管理部
        if(data.equals("5")){
            title.setText(R.string.kanri);
        }
        //プロジェクト統括室
        if(data.matches("6"+".*")){
            switch (data){
                case "61":
                    //システムサポート部
                    title.setText(R.string.Projectkanri);
                    break;
                case "62":
                    //開発部
                    title.setText(R.string.Hinsitukanri);
                    break;
            }
        }

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
    protected void onResume() {
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
                finish();
                break;
        }
    }
}
