package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tapia.mji.demo.R;
import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.TapiaApp;

/**
 * Created by ais75114 on 2018/01/25.
 */

public class BrowserActivity extends TapiaActivity{

    Timelag timelag=new Timelag();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webbrowser);

        //URL
        String url=null;
        String weather="https://weather.yahoo.co.jp/weather/";  //天気
        String news="https://news.yahoo.co.jp/";  //ニュース
        String fortune="https://fortune.yahoo.co.jp/";  //占い

        //パーツ
        WebView webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        //flag
        Intent flag_b=getIntent();
        String flag=flag_b.getStringExtra("flag");

        //表示URLの決定
        switch (flag){
            case "1":
                url=weather;
                break;
            case "2":
                url=news;
                break;
            case "3":
                url=fortune;
                break;
        }

        //決定したURLの表示
        webView.loadUrl(url);

        //JavaScript設定
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //1分後メニューに戻る
        timelag.setting(timelag.move(activity));
    }

    @Override
    protected void onPause(){
        super.onPause();
        timelag.cancel();
    }

    //「戻る」ボタンの処理
    public void onbackbuttonClick(View view){
        switch (view.getId()){
            case R.id.back:
                //1つ前の画面に遷移
                startActivity(new Intent(activity,SleepActivity.class));
                break;
        }
    }
}
