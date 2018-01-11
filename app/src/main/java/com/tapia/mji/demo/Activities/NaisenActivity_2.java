package com.tapia.mji.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ais75114 on 2017/10/13.
 */

public class NaisenActivity_2 extends TapiaActivity {

    /*一定時間経つと待機画面へ戻る処理*/
    Timelag timelag=new Timelag();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naisen);

        /*宣言部***********************************************************************************/

        //内線番号
        final String eigyo1="3152";
        final String eigyo2="2206";
        final String eigyo3="6006";
        final String unyo1="1103";
        final String unyo2="3021";
        final String unyo3="3211";
        final String kaihatu1="1101";
        final String kaihatu2="2302";
        final String kaihatu3="3103";
        final String kiban="3111";
        final String tokatu="5104";
        final String ict="6021";
        final String kanri="5807";
        final String support="1532";

        //テキストサイズ(Button用)
        float textsize_normal=40;
        float textsize_gizyutu=35;
        float textsize_ict=30;

        //TextView
        TextView title=(TextView)findViewById(R.id.title);
        TextView textView1=(TextView)findViewById(R.id.textView1);
        TextView textView2=(TextView)findViewById(R.id.textView2);
        TextView textView3=(TextView)findViewById(R.id.textView3);

        //Button
        Button button1=(Button)findViewById(R.id.button1);
        Button button2=(Button)findViewById(R.id.button2);
        Button button3=(Button)findViewById(R.id.button3);

        final Intent intentkakudai = new Intent(activity, NaisenKakudaiActivity.class);

        /*画面表示処理*****************************************************************************/

        //IwataniMenuActivityTestからflagを受け取る
        Intent intent=getIntent();
        String data=intent.getStringExtra("flag");
        final int flag=Integer.parseInt(data);

        //IwataniMenuActivityからのflagで分岐
        if(flag>=1&&flag<=3) {
            //第一事業部・第二事業部・第三事業部の設定
            //Button設定
            button1.setText(R.string.eigyo);
            button2.setText(R.string.unyo);
            button3.setText(R.string.kaihatu);
            button1.setTextSize(textsize_normal);
            button2.setTextSize(textsize_normal);
            button3.setTextSize(textsize_normal);
            switch (flag) {
                case 1:
                    /*第一事業部内線番号一覧表示*/
                    //タイトル設定
                    title.setText(R.string.daiiti);
                    //1行目設定
                    textView1.setText(eigyo1);
                    //2行目設定
                    textView2.setText(unyo1);
                    //3行目設定
                    textView3.setText(kaihatu1);
                    break;
                case 2:
                    /*第二事業部内線番号一覧表示*/
                    //タイトル設定
                    title.setText(R.string.daini);
                    //1行目設定
                    textView1.setText(eigyo2);
                    //2行目設定
                    textView2.setText(unyo2);
                    //3行目設定
                    textView3.setText(kaihatu2);
                    break;
                case 3:
                    /*第三事業部内線番号一覧表示*/
                    //タイトル設定
                    title.setText(R.string.daisan);
                    //1行目設定
                    textView1.setText(eigyo3);
                    //2行目設定
                    textView2.setText(unyo3);
                    //3行目設定
                    textView3.setText(kaihatu3);
                    break;
            }
        }else{
            switch (flag){
                case 4:
                    /*技術本部内線番号一覧表示*/
                    //タイトル設定
                    title.setText(R.string.gizyutu);
                    //1行目設定
                    button1.setText(R.string.kiban);
                    button1.setTextSize(textsize_gizyutu);
                    textView1.setText(kiban);
                    //2行目設定
                    button2.setText(R.string.tokatu);
                    button2.setTextSize(textsize_gizyutu);
                    textView2.setText(tokatu);
                    //3行目設定
                    button3.setText(R.string.ict);
                    button3.setTextSize(textsize_ict);
                    textView3.setText(ict);
                    break;
                case 5:
                    /*管理部内線番号一覧表示*/
                    //タイトル設定
                    title.setText(R.string.kanri);
                    //1行目設定
                    button1.setText(R.string.kanri2);
                    button1.setTextSize(textsize_normal);
                    textView1.setText(kanri);
                    //2行目設定
                    button2.setVisibility(View.INVISIBLE);
                    button3.setVisibility(View.INVISIBLE);
                    break;
                case 6:
                    /*サポートデスク内線番号一覧表示*/
                    //タイトル設定
                    title.setText(R.string.support);
                    //1行目設定
                    button1.setText(R.string.support);
                    button1.setTextSize(textsize_normal);
                    textView1.setText(support);
                    //2行目設定
                    button2.setVisibility(View.INVISIBLE);
                    button3.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        /*ボタン押下時の処理***********************************************************************/

        //button1押下
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flag){
                    case 1:
                        intentkakudai.putExtra("flag", "11");
                        intentkakudai.putExtra("number",eigyo1);
                        break;
                    case 2:
                        intentkakudai.putExtra("flag", "21");
                        intentkakudai.putExtra("number",eigyo2);
                        break;
                    case 3:
                        intentkakudai.putExtra("flag", "31");
                        intentkakudai.putExtra("number",eigyo3);
                        break;
                    case 4:
                        intentkakudai.putExtra("flag", "44");
                        intentkakudai.putExtra("number",kiban);
                        break;
                    case 5:
                        intentkakudai.putExtra("flag", "54");
                        intentkakudai.putExtra("number",kanri);
                        break;
                    case 6:
                        intentkakudai.putExtra("flag", "64");
                        intentkakudai.putExtra("number",support);
                        break;
                }
                //NaisenKakudaiにflagを持って飛ぶ
                startActivity(intentkakudai);
            }
        });

        //button2押下
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flag){
                    case 1:
                        intentkakudai.putExtra("flag", "12");
                        intentkakudai.putExtra("number",unyo1);
                        break;
                    case 2:
                        intentkakudai.putExtra("flag", "22");
                        intentkakudai.putExtra("number",unyo2);
                        break;
                    case 3:
                        intentkakudai.putExtra("flag", "32");
                        intentkakudai.putExtra("number",unyo3);
                        break;
                    case 4:
                        intentkakudai.putExtra("flag", "45");
                        intentkakudai.putExtra("number",tokatu);
                        break;
                }
                //NaisenKakudaiにflagを持って飛ぶ
                startActivity(intentkakudai);
            }
        });

        //button3押下
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flag){
                    case 1:
                        intentkakudai.putExtra("flag", "13");
                        intentkakudai.putExtra("number",kaihatu1);
                        break;
                    case 2:
                        intentkakudai.putExtra("flag", "23");
                        intentkakudai.putExtra("number",kaihatu2);
                        break;
                    case 3:
                        intentkakudai.putExtra("flag", "33");
                        intentkakudai.putExtra("number",kaihatu3);
                        break;
                    case 4:
                        intentkakudai.putExtra("flag", "46");
                        intentkakudai.putExtra("number",ict);
                        break;
                }
                //NaisenKakudaiにflagを持って飛ぶ
                startActivity(intentkakudai);
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
