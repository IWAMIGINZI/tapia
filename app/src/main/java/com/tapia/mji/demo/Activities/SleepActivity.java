package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapia.mji.demo.R;
import com.tapia.mji.demo.Tools.DeviceLog;
import com.tapia.mji.demo.Tools.RecognitionTimer;
import com.tapia.mji.demo.Tools.WatcherController;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Exceptions.LanguageNotSupportedException;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.TTSProvider;
import com.tapia.mji.tapialib.TapiaApp;
import com.tapia.mji.tapialib.Utils.TapiaAnimation;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Sami on 12-Jul-16.
 */
public class SleepActivity extends TapiaActivity implements SensorEventListener {
    TapiaAnimation[] tapiaAnimation = new TapiaAnimation[14];
    WatcherController wc;

    //センサマネージャの取得
    private SensorManager sensorManager;

    //アニメーション切り替え用
    Timer timer = null;
    int time = 60000;
    Timer recognitionTimer = null;
    boolean talking = false;

    // TTSProvider.OnStateChangeListener onTTSstateListener;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*宣言部***********************************************************************************/

        // 画面をスリープさせない
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //待機画面表示
        setContentView(R.layout.eyes_layout);

        //パーツ
        ImageView tapiaEyes = (ImageView) findViewById(R.id.eyes);

        //TAPIA初期音量設定
        //TapiaAudio.setVolume(this, TapiaAudio.getCurrent(),false);

        //センサ
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //TAPIAの表情
        for (int i = 0; i < tapiaAnimation.length; i++) {
            tapiaAnimation[i] = new TapiaAnimation(this, tapiaEyes);
        }

        //会話
        TapiaApp.setCurrentLanguage(Language.LanguageID.JAPANESE);
        //TapiaApp.setCurrentLanguage(Language.LanguageID.ENGLISH_UK);
        sttProvider=TapiaApp.currentLanguage.getOnlineSTTProvider();
        ttsProvider=TapiaApp.currentLanguage.getTTSProvider();
        //offlineNLUProvider=TapiaApp.currentLanguage.getOfflineNLUProvider();
        //final ArrayList actions=new ArrayList<>();
        //sttProvider.listen();   //録音の開始
        ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
            public void onSpeechComplete() {
                talking = false;
            }
        });

        //内線番号表示用
        final Intent intentsitei = new Intent(activity, NaisenKakudaiSiteiActivity.class);
        final ExtensionNumber en=new ExtensionNumber();
        final NumberSplit ns=new NumberSplit();

        //現在の日時を取得
/*
        Calendar cal=Calendar.getInstance();
        final String month=String.valueOf(cal.get(Calendar.MONTH)+1);   //月
        final String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));    //日
        final String hour=String.valueOf(cal.get(Calendar.HOUR_OF_DAY));    //時
        final String minute=String.valueOf(cal.get(Calendar.MINUTE));   //分
        final String week=String.valueOf(cal.get(Calendar.DAY_OF_WEEK));    //曜日
*/
        //Log.v("テスト",);
        //Log.v("テスト",month+"月" +day+"日");

        //ブラウザ表示画面
        //final Intent browser=new Intent(activity,BrowserActivity.class);

        /*画面遷移*********************************************************************************/

        //画面ロングタップ処理
        tapiaEyes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //環境設定画面へ遷移
                startActivity(new Intent(activity, EnvisettingActivity.class));
                return false;
            }
        });

        //画面タップ処理
        tapiaEyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //メニュー画面へ遷移
                startActivity(new Intent(activity, IwataniMenuActivity.class));
            }
        });

/*
        sttProvider.listen();   //録音の開始

        sttProvider.setOnRecognitionCompleteListener(new STTProvider.OnRecognitionCompleteListener() {

            public void onRecognitionComplete(List<String> list) {
                try {
                    ttsProvider.say(list.get(0).toString());
                } catch (LanguageNotSupportedException e) {
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {

                    public void onSpeechComplete() {
                        sttProvider.listen();
                        //sttProvider.stopListening();
                    }
                });
            }

        });
*/

        /*音声認識*********************************************************************************/

/*
        //「天気」
        actions.add(new MySimpleAction.Weather(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                browser.putExtra("flag","1");
                startActivity(browser);
            }
        }));

        //「ニュース」
        actions.add(new MySimpleAction.News(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                browser.putExtra("flag","2");
                startActivity(browser);
            }
        }));

        //「占い」
        actions.add(new MySimpleAction.Fortune(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                browser.putExtra("flag","3");
                startActivity(browser);
            }
        }));

        //「何日」
        actions.add(new MySimpleAction.Day(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("今日は"+month+"月"+day+"日です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「何時」
        actions.add(new MySimpleAction.Time(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask(hour+"時"+minute+"分です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「何曜日」
        actions.add(new MySimpleAction.Week(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                String week_name=null;
                switch (week){
                    case "1":
                        week_name="にち";
                        break;
                    case "2":
                        week_name="げつ";
                        break;
                    case "3":
                        week_name="か";
                        break;
                    case "4":
                        week_name="すい";
                        break;
                    case "5":
                        week_name="もく";
                        break;
                    case "6":
                        week_name="きん";
                        break;
                    case "7":
                        week_name="ど";
                        break;
                }
                try{
                    ttsProvider.ask(week_name+"曜日です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「カメラ」
        actions.add(new MySimpleAction.Camera(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                startActivity(new Intent(activity,CameraActivity.class));
            }
        }));

        //「はじめまして」
        actions.add(new MySimpleAction.Nicetomeetyou(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("はじめましてタピアです",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「おはよう」
        actions.add(new MySimpleAction.Goodmorning(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("おはようございます いらっしゃいませ",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「こんにちは」
        actions.add(new MySimpleAction.Hello(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("こんにちは いらっしゃいませ",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「こんばんは」
        actions.add(new MySimpleAction.Goodevening(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("こんばんは 遅くまでお疲れ様です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「ただいま」
        actions.add(new MySimpleAction.Imhome(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("おかえりなさい　お疲れ様です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「おやすみ」
        actions.add(new MySimpleAction.Goodnight(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("今日も一日お疲れ様でした",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        //「サポート」or「ヘルプ」
        actions.add(new MySimpleAction.Support(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.support; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {}
                });
            }
        }));

        //「第一事業部」
        actions.add(new MySimpleAction.Division1(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.eigyo1; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第二事業部」
        actions.add(new MySimpleAction.Division2(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.eigyo2; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第三事業部」
        actions.add(new MySimpleAction.Division3(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.eigyo3; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「技術本部」
        actions.add(new MySimpleAction.Divisiontech(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.ict; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「管理部」
        actions.add(new MySimpleAction.Kanri(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.kanri; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「技術本部-基盤」
        actions.add(new MySimpleAction.Kiban(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.kiban; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「技術本部-プロジェクト統轄」
        actions.add(new MySimpleAction.Tokatu(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.tokatu; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「技術本部-ICT」
        actions.add(new MySimpleAction.Ict(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.ict; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第一事業部-営業部」
        actions.add(new MySimpleAction.Eigyo1(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.eigyo1; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第二事業部-営業部」
        actions.add(new MySimpleAction.Eigyo2(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.eigyo2; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第三事業部-営業部」
        actions.add(new MySimpleAction.Eigyo3(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.eigyo3; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第一事業部-運用部」
        actions.add(new MySimpleAction.Unyo1(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.unyo1; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第二事業部-運用部」
        actions.add(new MySimpleAction.Unyo2(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.unyo2; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第三事業部-運用部」
        actions.add(new MySimpleAction.Unyo3(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.unyo3; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第一事業部-開発部」
        actions.add(new MySimpleAction.Kaihatu1(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.kaihatu1; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第二事業部-開発部」
        actions.add(new MySimpleAction.Kaihatu2(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.kaihatu2; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //「第三事業部-開発部」
        actions.add(new MySimpleAction.Kaihatu3(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                final String number=en.kaihatu3; //内線番号
                String new_number=ns.Execution(number);
                try{
                    ttsProvider.ask("内線番号は"+new_number+"です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                //終話後の処理
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener() {
                    @Override
                    public void onSpeechComplete() {
                    }
                });
            }
        }));

        //録音認識完了
        /*sttProvider.setOnRecognitionCompleteListener(new STTProvider.OnRecognitionCompleteListener(){
            @Override
            public void onRecognitionComplete(List<String> list){
                offlineNLUProvider.setOnAnalyseCompleteListener(new NLUProvider.OnAnalyseCompleteListener() {
                    @Override
                    public void OnAnalyseComplete(Action action) {
                        if(action==null){
                            try{
                                ttsProvider.say("もう一度お願いします");
                            }catch(LanguageNotSupportedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
                offlineNLUProvider.analyseText(list,actions);
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener(){
                    @Override
                    public void onSpeechComplete(){
                        //sttProvider.listen();
                        //sttProvider.stopListening();
                    }
                });
            }
        });*/
    }

    //Activity終了の際呼ばれる
    @Override
    protected void onPause() {
        super.onPause();

        //アニメーションを止める
        for (int i = 0; i < tapiaAnimation.length; i++) {
            tapiaAnimation[i].stopAnimation();
        }

        //タイマキャンセル
        timer.cancel();
        timer = null;
        if (recognitionTimer != null) {
            recognitionTimer.cancel();
            recognitionTimer = null;
        }

        //音のする方向へ動く処理のキャンセル
        //stopSoundLocation(this);

        // バックグランドカメラ監視停止
        wc.stop();
        wc = null;

        talking = false;
    }


    /*↓近接センサ処理↓*/

    @Override
    protected void onStop() {
        super.onStop();
        //解除
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //音のする方向へ回転
        //startSoundLocation(this);

        talking = false;

        //一定時間(1分)毎に処理を行う
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //乱数によって、アニメーションを切り替える
                RandomNumber rm = new RandomNumber();
                int n = rm.RN();
                //処理:乱数毎にアニメーションを設定
                //上記以外のアニメーションを停止する
                switch (n) {
                    case 0:
                        tapiaAnimation[0].startAnimation(TapiaAnimation.BLINKING, true);
                        for(int i=1;i<tapiaAnimation.length;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        break;
                    case 1:
                        tapiaAnimation[1].startAnimation(TapiaAnimation.SMILING, true);
                        tapiaAnimation[0].stopAnimation();
                        for(int i=2;i<tapiaAnimation.length;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        break;
                    case 2:
                        tapiaAnimation[2].startAnimation(TapiaAnimation.EXHAUSTED, true);
                        for(int i=0;i<2;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=3;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 3:
                        tapiaAnimation[3].startAnimation(TapiaAnimation.ALARM, true);
                        for(int i=0;i<3;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=4;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 4:
                        tapiaAnimation[4].startAnimation(TapiaAnimation.CONFUSED, true);
                        for(int i=0;i<4;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=5;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 5:
                        tapiaAnimation[5].startAnimation(TapiaAnimation.EXCITED, true);
                        for(int i=0;i<5;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=6;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 6:
                        tapiaAnimation[6].startAnimation(TapiaAnimation.TRANSITION1, true);
                        for(int i=0;i<6;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=7;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 7:
                        tapiaAnimation[7].startAnimation(TapiaAnimation.FUNNY, true);
                        for(int i=0;i<7;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=8;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 8:
                        tapiaAnimation[8].startAnimation(TapiaAnimation.LOOKDOWN, true);
                        for(int i=0;i<8;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=9;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 9:
                        tapiaAnimation[9].startAnimation(TapiaAnimation.LOOKLEFT, true);
                        for(int i=0;i<9;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=10;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 10:
                        tapiaAnimation[10].startAnimation(TapiaAnimation.LOOKRIGHT, true);
                        for(int i=0;i<10;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=11;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 11:
                        tapiaAnimation[11].startAnimation(TapiaAnimation.LOOKUP, true);
                        for(int i=0;i<11;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        for(int j=12;j<tapiaAnimation.length;j++){
                            tapiaAnimation[j].stopAnimation();
                        }
                        break;
                    case 12:
                        tapiaAnimation[12].startAnimation(TapiaAnimation.LOVE, true);
                        for(int i=0;i<12;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                        tapiaAnimation[13].stopAnimation();
                        break;
                    case 13:
                        tapiaAnimation[13].startAnimation(TapiaAnimation.PLAIN, true);
                        for(int i=0;i<13;i++){
                            tapiaAnimation[i].stopAnimation();
                        }
                }
                int log=tapiaAnimation[n].getIndex();
                Log.v("テスト",String.valueOf(log));
            }
        }, 0, time);

        //近接センサ登録
//        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_PROXIMITY);
//        if (sensors.size() > 0) {
//            Sensor s = sensors.get(0);
//            sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
//        }

        // バックグラウンドカメラ監視
        wc = new WatcherController(this);
        wc.start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //使わない
    }

    //近接センサの値が変わった時
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            //近接センサが0.0の時(何かがTAPIAに近づいた時)
            if (event.values[0] == 0.0) {
                //メニュー画面へ遷移する
                startActivity(new Intent(activity, IwataniMenuActivity.class));
            }
        }
    }

    public void onRecognitionCompleted(ArrayList<String> names) {
        String opened = null;
        try {
            if (names.size() > 0) {
                String name = "";
                for (int i = 0; i < names.size(); i++) {
                    if (i != 0) {
                        name += "\n";
                    }
                    Pattern p = Pattern.compile("^([^:]*)::(.*)$");
                    Matcher m = p.matcher(names.get(i));
                    if (m.matches()) {
                        name += m.group(1);
                        if (opened == null && m.group(2).equals("talk")) {
                            opened = m.group(1);
                        }
                    } else {
                        name += names.get(i);
                    }
                }
                TextView tvPersonName = (TextView) findViewById(R.id.textPersonName);
                tvPersonName.setText(name);
                if (recognitionTimer != null) {
                    recognitionTimer.cancel();
                    recognitionTimer = null;
                }
                recognitionTimer = new Timer();
                recognitionTimer.schedule(new RecognitionTimer(this), 3 * 1000);
            }
        } catch (RuntimeException e) {
            DeviceLog.d("tapia", "onRecognitionCompleted", e);
        }
        if (opened != null) {
            try {
                if (!talking) {
                    talking = true;
                    String talk = opened;
                    /*int idx = opened.indexOf(" ");
                    if (idx > 0) {
                        talk = opened.substring(0, idx);
                    } else {
                        idx = opened.indexOf("　");
                        if (idx > 0) {
                            talk = opened.substring(0, idx);
                        }
                    }*/

                    int idx = opened.indexOf("@");
                    if (idx > 0) {
                        talk = opened.substring(idx + 1);
                        //画面表示を再設定
                        TextView tvPersonName = (TextView) findViewById(R.id.textPersonName);
                        String call = opened.substring(0,idx);
                        tvPersonName.setText(call);
                    } else {
                        idx = opened.indexOf(" ");
                        if (idx > 0) {
                            talk = opened.substring(0, idx);
                        } else {
                            idx = opened.indexOf("　");
                            if (idx > 0) {
                                talk = opened.substring(0, idx);
                            }
                        }
                    }
                    ttsProvider.say(talk + "さん、お入りください。");
                }else{
                    //talking＝trueの場合は画面表示の再設定のみ行う
                    int idx = opened.indexOf("@");
                    if (idx > 0) {
                        //画面表示を再設定
                        TextView tvPersonName = (TextView) findViewById(R.id.textPersonName);
                        String call = opened.substring(0,idx);
                        tvPersonName.setText(call);
                    }
                }
            } catch (LanguageNotSupportedException e) {
                DeviceLog.d("tapia", "LanguageNotSupportedException", e);
            }
        }
    }

    public void onRecognitionCompleteTimer() {
        try {
            TextView tvPersonName = (TextView) findViewById(R.id.textPersonName);
            tvPersonName.setText("");
            recognitionTimer.cancel();
            recognitionTimer = null;
        } catch (RuntimeException e) {
            DeviceLog.d("tapia", "onRecognitionCompleteTimer", e);
        }
    }
}
