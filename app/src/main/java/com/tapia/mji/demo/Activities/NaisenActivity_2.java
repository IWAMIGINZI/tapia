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
        ExtensionNumber en=new ExtensionNumber();
        final String Dep1_WINsupport=en.Dep1_WINsupport;     //第一事業部-WINサポート部
        final String Dep1_WINEsupport=en.Dep1_WINEsupport;     //第一事業部-WIN-Eサポート部
        final String Dep1_Kaihatu=en.Dep1_Kaihatu;   //第一事業部-開発部
        final String Dep2_Syssupport=en.Dep2_Syssupport;     //第二事業部-システムサポート部
        final String Dep2_Kaihatu=en.Dep2_Kaihatu;   //第二事業部-開発部
        final String Hinsitukanri=en.Hinsitukanri;     //プロジェクト統括室-品質管理部
        final String Projectkanri=en.Projectkanri;     //プロジェクト統括室-プロジェクト管理部
        final String Kiban=en.Kiban;      //技術本部-基盤ソリューション部
        final String Ict=en.Ict;        //技術本部-ICTソリューション推進部

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

        //画面遷移先
        final Intent intentkakudai = new Intent(activity, NaisenKakudaiActivity.class);

        /*画面表示処理*****************************************************************************/

        //IwataniMenuActivityからflagを受け取る
        Intent intent=getIntent();
        String data=intent.getStringExtra("flag");
        final int flag=Integer.parseInt(data);

        //flagで分岐
        switch (flag) {
            case 1:
                /*第一事業部内線番号一覧表示*/
                //タイトル設定
                title.setText(R.string.daiiti);
                //1行目設定
                button1.setText(R.string.WINsupport);
                button1.setTextSize(textsize_normal);
                textView1.setText(Dep1_WINsupport);
                //2行目設定
                button2.setText(R.string.WINEsupport);
                button2.setTextSize(textsize_normal);
                textView2.setText(Dep1_WINEsupport);
                //3行目設定
                button3.setText(R.string.kaihatu);
                button3.setTextSize(textsize_normal);
                textView3.setText(Dep1_Kaihatu);
                break;
            case 2:
                /*第二事業部内線番号一覧表示*/
                //タイトル設定
                title.setText(R.string.daini);
                //1行目設定
                button1.setText(R.string.Syssupport);
                button1.setTextSize(textsize_gizyutu);
                textView1.setText(Dep2_Syssupport);
                //2行目設定
                button2.setText(R.string.kaihatu);
                button2.setTextSize(textsize_normal);
                textView2.setText(Dep2_Kaihatu);
                //3行目設定
                button3.setVisibility(View.INVISIBLE);
                break;
            case 4:
                /*技術本部内線番号一覧表示*/
                //タイトル設定
                title.setText(R.string.gizyutu);
                //1行目設定
                button1.setText(R.string.kiban);
                button1.setTextSize(textsize_gizyutu);
                textView1.setText(Kiban);
                //2行目設定
                button2.setText(R.string.ict);
                button2.setTextSize(textsize_normal);
                button2.setTextSize(textsize_ict);
                textView2.setText(Ict);
                //3行目設定
                button3.setVisibility(View.INVISIBLE);
                break;
            case 6:
                /*プロジェクト統括室内線番号一覧表示*/
                //タイトル設定
                title.setText(R.string.Projecttokatu);
                //1行目設定
                button1.setText(R.string.Projectkanri);
                button1.setTextSize(textsize_gizyutu);
                textView1.setText(Projectkanri);
                //2行目設定
                button2.setText(R.string.Hinsitukanri);
                button2.setTextSize(textsize_normal);
                textView2.setText(Hinsitukanri);
                //3行目設定
                button3.setVisibility(View.INVISIBLE);
                break;
        }

        /*ボタン押下時の処理***********************************************************************/

        //button1押下
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flag){
                    case 1:
                        intentkakudai.putExtra("flag", "11");
                        intentkakudai.putExtra("number",Dep1_WINsupport);
                        break;
                    case 2:
                        intentkakudai.putExtra("flag", "21");
                        intentkakudai.putExtra("number",Dep2_Syssupport);
                        break;
                    case 4:
                        intentkakudai.putExtra("flag", "41");
                        intentkakudai.putExtra("number",Kiban);
                        break;
                    case 6:
                        intentkakudai.putExtra("flag", "61");
                        intentkakudai.putExtra("number",Projectkanri);
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
                        intentkakudai.putExtra("number",Dep1_WINEsupport);
                        break;
                    case 2:
                        intentkakudai.putExtra("flag", "22");
                        intentkakudai.putExtra("number",Dep2_Kaihatu);
                        break;
                    case 4:
                        intentkakudai.putExtra("flag", "42");
                        intentkakudai.putExtra("number",Ict);
                        break;
                    case 6:
                        intentkakudai.putExtra("flag", "62");
                        intentkakudai.putExtra("number",Hinsitukanri);
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
                        intentkakudai.putExtra("number",Dep1_Kaihatu);
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
