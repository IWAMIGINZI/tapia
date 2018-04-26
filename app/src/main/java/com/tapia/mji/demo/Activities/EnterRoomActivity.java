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
import com.tapia.mji.demo.Tools.Locker;
import com.tapia.mji.tapialib.Activities.TapiaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EnterRoomActivity extends TapiaActivity {
    private CameraPreview cameraPreview = null;
    AnalyzerRecognitionSync ars;
    JSONObject json;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
            cameraPreview = new CameraPreview(this, cameraIndex);
            preview.addView(cameraPreview);
        }
        ApplicationInfo appliInfo = null;
        int wait_msec = 1000;
        try {
            appliInfo = getApplicationContext().getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            wait_msec = appliInfo.metaData.getInt("camera_scan_interval");
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NumberFormatException e) {
        }

        final Runnable r = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if (cameraPreview != null) {
                    Camera camera = cameraPreview.getCamera();
                    if (camera != null) {
                        try {
                            camera.autoFocus(autoFocusCallback);
                        } catch (RuntimeException e) {
                            Log.d("tapia", "autoFocusFailed");
                        }
                    }
                }
            }
        };
        handler.postDelayed(r, wait_msec);

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

    @Override
    protected void onPause() {
        super.onPause();
        cameraPreview = null;
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            try {
                camera.takePicture(null, null, pictureCallback);
            } catch(RuntimeException e) {
                Log.d("tapia", "onAutoFocus");
            }
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
                ars.setImageParameter("MSG/FRAME_JPG_B64", imgPath);
                ars.setParameter("MSG/FRAME_KEY", timeString);
                new AsyncCaller().execute();
                handler.post(new Runnable() {
                    public void run() {
                        onCompleteRecognition();
                    }
                });
            } catch (Exception e) {
                Log.e("EnterRoom::picture", e.getMessage());
            }

            fos = null;
        }
    };

    public void onCompleteRecognition() {
        startActivity(new Intent(activity, SleepActivity.class));
    }

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
                String jsons = json.toString();
                Log.d("tapia", jsons);
                actionRecognition(json);
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
            return null;
        }

        private void actionRecognition(JSONObject json) {
            try {
                if (json.getInt("STATUS") == 0) {
                    JSONObject result = json.getJSONObject("RESULT");
                    actionRecognitionResult(result);
                } else {
                    Log.d("tapia", "検出失敗");
                }
            } catch (JSONException e) {
                Log.d("tapia", "JSONException in actionRecognition");
            }
        }

        private void actionRecognitionResult(JSONObject result) {
            try {
                if (result.has("FACE")) {
                    actionRecognitionFace(result.getJSONArray("FACE"));
                }
            } catch (JSONException e) {
                Log.d("tapia", "JSONException in actionRecognitionResult");
            }
        }

        private void actionRecognitionFace(JSONArray faces) {
            try {
                for (int i = 0; i < faces.length(); i++) {
                    JSONObject face = faces.getJSONObject(i);
                    if (face.has("PERSON_CODE")) {
                        actionOpenSesami(face);
                    }
                }
            } catch (JSONException e) {
                Log.d("tapia", "JSONException in actionRecognitionFace");
            }
        }

        private void actionOpenSesami(JSONObject face) {
            try {
                String code = face.getString("PERSON_CODE");
                String name = "";
                if (face.has("PERSON_NAME")) {
                    name = face.getString("PERSON_NAME");
                }
                // do open sesami!!
            } catch (JSONException e) {
                Log.d("tapia", "JSONException in actionOpenSesami");
            }
        }
    }
}
