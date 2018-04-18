package com.tapia.mji.demo.Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.tapia.mji.demo.Labellio.AnalyzerRecognitionSync;
import com.tapia.mji.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EnterRoomActivity extends Activity {
    private Camera camera = null;
    private CameraPreview cameraPreview = null;
    AnalyzerRecognitionSync ars;
    JSONObject json;
    Activity forOther;
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forOther = this;
        setContentView(R.layout.activity_camera);

        int cameraIndex = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo caminfo = new CameraInfo();
            Camera.getCameraInfo(i, caminfo);

            int facing = caminfo.facing;
            if (facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraIndex = i;
                break;
            }
        }
        if (!(cameraIndex < 0)) {
            try {
                camera = Camera.open(cameraIndex);
            } catch (Exception e) {
                this.finish();
            }
            FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
            cameraPreview = new CameraPreview(this, camera);
            preview.addView(cameraPreview);
        }
        ApplicationInfo appliInfo = null;
        int wait_msec = 1000;
        try {
            appliInfo = getApplicationContext().getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            wait_msec = Integer.parseInt(appliInfo.metaData.getString("camera_scan_interval"));
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NumberFormatException e) {
        }

        handler = new Handler();
        final Runnable r = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                camera.autoFocus(autoFocusCallback);
            }
        };
        handler.postDelayed(r, wait_msec);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.cameraPreview).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            findViewById(R.id.cameraPreview).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            findViewById(R.id.cameraPreview).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            camera.takePicture(null, null, pictureCallback);
        }
    };

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data == null) {
                return;
            }

            String saveDir = Environment.getExternalStorageDirectory().getPath() + "/tapia";

            File file = new File(saveDir);

            if (!file.exists()) {
                if (!file.mkdir()) {
                    Log.e("Debug", "Make Dir Error");
                }
            }

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timeString = sf.format(cal.getTime());
            String imgPath = saveDir + "/" + timeString + ".jpg";

            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imgPath,true);
                fos.write(data);
                fos.close();
                registAndroidDB(imgPath);
                ars = new AnalyzerRecognitionSync(getApplicationContext());
                ars.setImageParameter("FRAME_JPG_B64", imgPath);
                ars.setParameter("FRAME_KEY", timeString);
                new AsyncCaller().execute();
                startActivity(new Intent(forOther, SleepActivity.class));
            } catch (Exception e) {
                Log.e("EnterRoom::picture", e.getMessage());
            }

            fos = null;

            camera.startPreview();
        }
    };

    private void registAndroidDB(String path) {
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = this.getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put("_data", path);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    //「戻る」ボタンの処理
    public void onbackbuttonClick(View view) {
        switch (view.getId()) {
        case R.id.back:
            startActivity(new Intent(this, SleepActivity.class));
            break;
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                json = ars.post();
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
            return null;
        }
    }
}
