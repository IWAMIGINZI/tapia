package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tapia.mji.demo.Labellio.AnalyzerRecognitionSync;
import com.tapia.mji.demo.Labellio.LearnerLearning;
import com.tapia.mji.demo.R;
import com.tapia.mji.demo.Tools.AsyncCaller;
import com.tapia.mji.demo.Tools.AsyncCaller_ll;
import com.tapia.mji.demo.Tools.Watcher;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Exceptions.LanguageNotSupportedException;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.TTSProvider;
import com.tapia.mji.tapialib.TapiaApp;

import org.json.JSONObject;


/**
 * Created by ais75114 on 2017/09/19.
 */

public class IwataniMenuActivity extends TapiaActivity implements View.OnClickListener {

    JSONObject json;
    Watcher wa;

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

    /*
    public void ontourokuClick(View view){
        switch (view.getId()){
            case R.id.touroku:
                try{
                    LearnerLearning ll = new LearnerLearning();
                    //AnalyzerRecognitionSync ll = new AnalyzerRecognitionSync();
                    ll.setParameter("MSG/FRAME_JPG_B64", "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC8aGi9jQjhCY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2P/wAARCACgAKADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDNmmAJweBWTdyGWQg/dUZxT5piZPvcLVcHcGY9T+dSAkshYAHA9hULtgcnildgSPaoWOTVABYk8VJFDJM2EUmn2lqZnGelbkcCRKAAM0m7DSuZ0OmgAGQ5qUxKjKqgc1oMCRgDFV2T98ufSs7miiRiMAUmwDoKs7AKYVzSuVYrMoHJq");
                    //ll.setImageParameter("MSG/FRAME_JPG_B64", "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC8aGi9jQjhCY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2P/wAARCACgAKADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDNmmAJweBWTdyGWQg/dUZxT5piZPvcLVcHcGY9T+dSAkshYAHA9hULtgcnildgSPaoWOTVABYk8VJFDJM2EUmn2lqZnGelbkcCRKAAM0m7DSuZ0OmgAGQ5qUxKjKqgc1oMCRgDFV2T98ufSs7miiRiMAUmwDoKs7AKYVzSuVYrMoHJq" );
                    ll.setParameter("MSG/PERSON_CODE", "test0000");
                    ll.setParameter("MSG/PERSON_NAME", "登録テスト");
                    new AsyncCaller_ll(ll, json).execute();
                    //new AsyncCaller(ll, json, wa).execute();
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Learning::picture", e.getMessage());
                }

        }

    }
    */


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
