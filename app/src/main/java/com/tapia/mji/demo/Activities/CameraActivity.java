package com.tapia.mji.demo.Activities;

/**
 * Created by ais75114 on 2018/01/26.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tapia.mji.demo.R;

public class CameraActivity extends Activity{
    private Camera mCam = null;
    private CameraPreview mCamPreview = null;
    private boolean mIsTake=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        try {
            mCam = Camera.open();
        } catch (Exception e) {
            // エラー
            this.finish();
        }

        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        mCamPreview = new CameraPreview(this, mCam);
        preview.addView(mCamPreview);

        mCamPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(mIsTake){
                    return true;
                }

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    mIsTake=true;
                    mCam.autoFocus(mAutoFocuslistener);
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // カメラ破棄インスタンスを解放
        if (mCam != null) {
            mCam.release();
            mCam = null;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            findViewById(R.id.cameraPreview).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            findViewById(R.id.cameraPreview).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }else{
            findViewById(R.id.cameraPreview).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private  Camera.PictureCallback mPicJpgListener=new Camera.PictureCallback(){
        public void onPictureTaken(byte[] data, Camera camera){
            if(data==null){
                return;
            }

            String saveDir= Environment.getExternalStorageDirectory().getPath()+"/tapia";

            File file=new File(saveDir);

            if(!file.exists()){
                if(!file.mkdir()){
                    Log.e("Debug","Make Dir Error");
                }
            }

            Calendar cal=Calendar.getInstance();
            SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd_HHmmss");
            String imgPath=saveDir+"/"+sf.format(cal.getTime())+".jpg";

            FileOutputStream fos;
            try{
                fos=new FileOutputStream(imgPath,true);
                fos.write(data);
                fos.close();
                registAndroidDB(imgPath);
            }catch (Exception e){
                Log.e("Debug",e.getMessage());
            }

            fos=null;

            mCam.startPreview();

            mIsTake=false;
        }
    };

    private void registAndroidDB(String path){
        ContentValues values=new ContentValues();
        ContentResolver contentResolver=this.getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        values.put("_data",path);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
    }

    private Camera.AutoFocusCallback mAutoFocuslistener=new Camera.AutoFocusCallback(){
        public void onAutoFocus(boolean success,Camera camera){
            mCam.takePicture(null,null,mPicJpgListener);
        }
    };

    //「戻る」ボタンの処理
    public void onbackbuttonClick(View view){
        switch (view.getId()){
            case R.id.back:
                //1つ前の画面に遷移
                startActivity(new Intent(this,SleepActivity.class));
                break;
        }
    }

}
