package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tapia.mji.demo.R;
import com.tapia.mji.tapialib.Activities.TapiaActivity;

/**
 * Created by ais75114 on 2018/01/25.
 */

public class BrowserActivity extends TapiaActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webbrowser);

        //URL宣言
        String url=null;
        String weather="https://weather.yahoo.co.jp/weather/";  //天気
        String news="https://news.yahoo.co.jp/";  //ニュース

        //パーツ宣言
        WebView webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        //flagを受け取る
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
        }

        //決定したURLの表示
        webView.loadUrl(url);
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
