package com.tapia.mji.demo.Activities;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tapia.mji.demo.Database.Mydatabase;
import com.tapia.mji.demo.R;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Exceptions.LanguageNotSupportedException;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.STTProvider;
import com.tapia.mji.tapialib.Providers.Interfaces.TTSProvider;
import com.tapia.mji.tapialib.TapiaApp;
import com.tapia.mji.tapialib.Utils.TapiaRobot;

import java.util.List;

/**
 * Created by ais75114 on 2017/10/03.
 */

public class EnvisettingActivity extends TapiaActivity{

    AudioManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /*宣言部***********************************************************************************/

        //環境設定画面の表示
        setContentView(R.layout.activity_environmentalsetting);

        //日本語の設定
        TapiaApp.setCurrentLanguage(Language.LanguageID.JAPANESE);
        ttsProvider=TapiaApp.currentLanguage.getTTSProvider();

        //オーディオマネージャの設定
        manager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);

        //パーツ宣言
        Button finish=(Button)findViewById(R.id.finish);

        //yahooに繋ぐ
        Uri uri=Uri.parse("https://www.yahoo.com/");
        final Intent yahoo=new Intent(Intent.ACTION_VIEW,uri);

        /*音量設定*********************************************************************************/

        //minusボタン押下
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //音量を下げる(現在の音量を取得し、1引いた値を設定している)
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,manager.getStreamVolume(AudioManager.STREAM_MUSIC)-1,0);
                //話す
                try{
                    ttsProvider.say(getString(R.string.soundtest));
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
            }
        });

        //「plus」ボタン押下
        findViewById(R.id.plus).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //音量を上げる(現在の音量を取得し、1足した値を設定している)
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,manager.getStreamVolume(AudioManager.STREAM_MUSIC)+1,0);
                //話す
                try{
                    ttsProvider.say(getString(R.string.soundtest));
                }catch(LanguageNotSupportedException e){
                    e.printStackTrace();
                }
            }
        });

        /*角度設定*********************************************************************************/

        //TAPIA回転処理
        findViewById(R.id.up).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TapiaRobot.rotate(activity, TapiaRobot.RotateOrientation.UP, 15,null);
            }
        });

        findViewById(R.id.down).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TapiaRobot.rotate(activity, TapiaRobot.RotateOrientation.DOWN, 15,null);
            }
        });
        findViewById(R.id.right).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TapiaRobot.rotate(activity, TapiaRobot.RotateOrientation.RIGHT, 15,null);
            }
        });
        findViewById(R.id.left).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TapiaRobot.rotate(activity, TapiaRobot.RotateOrientation.LEFT, 15,null);
            }
        });

        /*アプリ終了処理***************************************************************************/

        //「アプリを終了する」ボタン処理
        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //パスワード入力画面へ遷移
                setContentView(R.layout.inputpassword);

                //パスワード入力アナウンス
                Toast inputpass=Toast.makeText(EnvisettingActivity.this,
                        "パスワードを入力してください",Toast.LENGTH_LONG);
                inputpass.show();

                //パーツ宣言
                Button kakutei=(Button)findViewById(R.id.kakutei);
                final EditText pass=(EditText)findViewById(R.id.pass);

                //「確定」ボタン処理
                kakutei.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //入力された文字列の取得
                        SpannableStringBuilder sb=(SpannableStringBuilder)pass.getText();
                        String password=sb.toString();

                        //パスワードの判定
                        if(password.equals("test")){
                            //ホーム画面に戻る
                            moveTaskToBack(true);
                        }else{
                            //エラーメッセージアナウンス
                            Toast error=Toast.makeText(EnvisettingActivity.this,
                                    "パスワードが違います",Toast.LENGTH_LONG);
                            error.show();
                            //TAPIAの顔の画面に遷移
                            startActivity(new Intent(activity,StartActivity.class));
                        }
                    }
                });
            }
        });

        /*戻る*************************************************************************************/

        //「戻る」ボタン押下
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //1つ前の画面に戻る
                /*SleepActivityのTimerを作動させる為,startActivityでの遷移*/
                startActivity(new Intent(activity,SleepActivity.class));
            }
        });

        /*yahooへリンク(試験的に実装)**************************************************************/

        //「Y」ボタン押下
        findViewById(R.id.browser).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(yahoo);
            }
        });

        /*カメラ起動(試験的に実装)**************************************************************/

        //「camera」ボタン押下
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent();
                intent.setAction("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
                //finish();
            }
        });
    }
}
