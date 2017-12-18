package com.tapia.mji.demo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ais75114 on 2017/10/11.
 */

public class Mydatabase extends SQLiteOpenHelper{

    public Mydatabase(Context context){
        //データベースファイル名と、バージョンの設定
        super(context,"tapia.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //テーブルの作成と初期データの投入

        //部署テーブル作成
        db.execSQL(
            "CREATE TABLE dep_table("
            +"dep_code primary key autoincrement not null, "
            +"dep_name text not null)"
        );

        //部署 初期データ登録
        db.execSQL(
                "INSERT INTO dep_table(dep_name) VALUES("
                +"第一事業部"
                +",第二事業部"
                +",第三事業部"
                +",技術本部"
                +",管理部"
                +",サポートデスク)"
        );

        //部テーブル作成
        db.execSQL(
            "CREATE TABLE cate_table("
            +"cate_code primary key autoincrement not null, "
            +"cate_name text not null)"
        );

        //部 初期データ登録
        db.execSQL(
                "INSERT INTO cate_table(cate_name) VALUES("
                +"営業部"
                +",運用部"
                +",開発部"
                +",基盤ソリューション部"
                +",プロジェクト統轄部"
                +",ICTソリューション推進部"
                +",人事総務/経理"
                +",サポートデスク)"
        );
    }


/*
    アプリケーションの更新などによって、データベースのバージョンが上がった場合に実行される
*/
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //データの退避、テーブルの再構成等を行う
    }
}
