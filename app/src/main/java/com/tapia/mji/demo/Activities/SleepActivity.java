package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapia.mji.demo.Actions.GiveDate;
import com.tapia.mji.demo.Actions.GiveTime;
import com.tapia.mji.demo.Actions.MySimpleAction;
import com.tapia.mji.demo.Actions.Rotate;
import com.tapia.mji.demo.R;
import com.tapia.mji.tapialib.Actions.Action;
import com.tapia.mji.tapialib.Actions.SimpleAction;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Exceptions.LanguageNotSupportedException;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.STTProvider;
import com.tapia.mji.tapialib.Providers.Interfaces.TTSProvider;
import com.tapia.mji.tapialib.TapiaApp;
import com.tapia.mji.tapialib.Utils.TapiaAnimation;
import com.tapia.mji.tapialib.Utils.TapiaRobot;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;
import static com.tapia.mji.tapialib.Utils.TapiaRobot.startSoundLocation;
import static com.tapia.mji.tapialib.Utils.TapiaRobot.stopSoundLocation;


/**
 * Created by Sami on 12-Jul-16.
 */
public class SleepActivity extends TapiaActivity implements SensorEventListener {
    TapiaAnimation[] tapiaAnimation = new TapiaAnimation[14];

    //センサマネージャの取得
    private SensorManager sensorManager;

    //アニメーション切り替え用
    Timer timer = new Timer();
    int time = 60000;

    TTSProvider.OnStateChangeListener onTTSstateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*宣言部***********************************************************************************/

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
        sttProvider=TapiaApp.currentLanguage.getOnlineSTTProvider();
        ttsProvider=TapiaApp.currentLanguage.getTTSProvider();
        offlineNLUProvider=TapiaApp.currentLanguage.getOfflineNLUProvider();
        final ArrayList actions=new ArrayList<>();
        sttProvider.listen();   //録音の開始

        //内線番号表示用
        final Intent intentsitei = new Intent(activity, NaisenKakudaiSiteiActivity.class);

        //現在の日時を取得
        Calendar cal=Calendar.getInstance();
        final String month=String.valueOf(cal.get(Calendar.MONTH)+1);
        final String day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        final String hour=String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        final String minute=String.valueOf(cal.get(Calendar.MINUTE));
        Log.v("テスト",hour+"時"+ minute+"分");
        Log.v("テスト",month+"月" +day+"日");

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

        actions.add(new MySimpleAction.Kanda(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
/*
                intentsitei.putExtra("name","神田");
                intentsitei.putExtra("number","5102");
                startActivity(intentsitei);
*/
                try {
                    ttsProvider.ask("私の名前はタピアです", sttProvider);
                } catch (LanguageNotSupportedException e) {
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        actions.add(new MySimpleAction.Test(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try {
                    ttsProvider.ask("テストです", sttProvider);
                } catch (LanguageNotSupportedException e) {
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));

        actions.add(new MySimpleAction.Day(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                try{
                    ttsProvider.ask("今日は"+month+"月"+day+"日です",sttProvider);
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
                startActivity(new Intent(activity, IwataniMenuActivity.class));
            }
        }));

        actions.add(new MySimpleAction.Move(new SimpleAction.OnSimpleActionListener(){
            @Override
            public void onSimpleAction(){
                //移動
                startActivity(new Intent(activity, IwataniMenuActivity.class));
            }
        }));


/*
        actions.add(new GiveTime(new GiveTime.OnGiveTimeListener() {
            @Override
            public void onGiveTime(Date time) {
                try {
                    ttsProvider.ask(time+"です", sttProvider);
                } catch (LanguageNotSupportedException e) {
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));
*/

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

/*
        actions.add(new GiveDate(new GiveDate.OnGiveDateListener() {
            @Override
            public void onGiveDate(Date date) {
                try{
                    ttsProvider.ask(date+"です",sttProvider);
                }catch (LanguageNotSupportedException e){
                    e.printStackTrace();
                }
                ttsProvider.setOnSpeechCompleteListener(null);
            }
        }));
*/

        //録音認識完了
        sttProvider.setOnRecognitionCompleteListener(new STTProvider.OnRecognitionCompleteListener(){
            @Override
            public void onRecognitionComplete(List<String> list){
                offlineNLUProvider.analyseText(list,actions);
                ttsProvider.setOnSpeechCompleteListener(new TTSProvider.OnSpeechCompleteListener(){
                    @Override
                    public void onSpeechComplete(){
                        sttProvider.listen();
                        //sttProvider.stopListening();
                    }
                });
            }
        });
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

        //音のする方向へ動く処理のキャンセル
        //stopSoundLocation(this);
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

        //一定時間(1分)毎に処理を行う
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
            }
        }, 0, time);

        //近接センサ登録
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_PROXIMITY);
        if (sensors.size() > 0) {
            Sensor s = sensors.get(0);
            sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
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

}



