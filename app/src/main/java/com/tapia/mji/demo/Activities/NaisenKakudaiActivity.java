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
        sttProvider=TapiaApp.currentLanguage.getOnlineSTTProvider();
        ttsProvider=TapiaApp.currentLanguage.getTTSProvider();
        offlineNLUProvider=TapiaApp.currentLanguage.getOfflineNLUProvider();
        final ArrayList actions=new ArrayList<>();

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

        //NaisenActivity_2からflagを受け取る
        Intent intent=getIntent();
        String data=intent.getStringExtra("flag");
        String number=intent.getStringExtra("number");

        //NaisenActivity_2からのflagで分岐し、押されたボタンの内線番号を拡大表示する
        //営業部
        if(data.matches(".*"+"1")){
            title.setText(R.string.eigyo);
            switch (data){
                case "11":
                    //第一事業部
                    naisenbangou.setText(number);
                    break;
                case "21":
                    //第二事業部
                    naisenbangou.setText(number);
                    break;
                case "31":
                    //第三事業部
                    naisenbangou.setText(number);
                    break;
            }
        }
        //運用部
        if(data.matches(".*"+"2")){
            title.setText(R.string.unyo);
            switch (data){
                case "12":
                    //第一事業部
                    naisenbangou.setText(number);
                    break;
                case "22":
                    //第二事業部
                    naisenbangou.setText(number);
                    break;
                case "32":
                    //第三事業部
                    naisenbangou.setText(number);
                    break;
            }
        }
        //開発部
        if(data.matches(".*"+"3")){
            title.setText(R.string.kaihatu);
            switch (data){
                case "13":
                    //第一事業部
                    naisenbangou.setText(number);
                    break;
                case "23":
                    //第二事業部
                    naisenbangou.setText(number);
                    break;
                case "33":
                    //第三事業部
                    naisenbangou.setText(number);
                    break;
            }
        }
        //技術本部・管理部・サポートデスク
        switch (data){
            case "44":
                //基盤ソリューション部
                title.setText(R.string.kiban);
                naisenbangou.setText(number);
                break;
            case "45":
                //プロジェクト統轄部
                title.setText(R.string.tokatu);
                naisenbangou.setText(number);
                break;
            case "46":
                //ICTソリューション推進部
                title.setText(R.string.ict);
                naisenbangou.setText(number);
                break;
            case "54":
                //管理部
                title.setText(R.string.kanri2);
                naisenbangou.setText(number);
                break;
            case "64":
                //サポートデスク
                title.setText(R.string.support);
                naisenbangou.setText(number);
                break;
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

        /*言葉認識*********************************************************************************/

/*
        actions.add(new MySimpleAction.Introduce(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("私の名前はタピアです",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        sttProvider.listen();   //録音の開始

        //録音認識完了
        sttProvider.setOnRecognitionCompleteListener(new STTProvider.OnRecognitionCompleteListener(){
            @Override
            public void onRecognitionComplete(List<String> list){
                offlineNLUProvider.analyseText(list,actions);
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener(){
                    @Override
                    public void onSpeechComplete(){
                        sttProvider.stopListening();
                    }
                });
            }
        });
*/
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
