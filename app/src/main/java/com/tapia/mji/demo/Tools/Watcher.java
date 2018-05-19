package com.tapia.mji.demo.Tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.tapia.mji.demo.Activities.SleepActivity;
import com.tapia.mji.demo.Labellio.AnalyzerRecognitionSync;
import com.tapia.mji.tapialib.TapiaApp;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Watcher implements Runnable {

    static enum DetectResult { NOT_FOUND, FOUND_FACE };
    boolean running = true;
    int cameraIndex = -1;
    boolean pictureTaking = false;
    SurfaceTexture surface = new SurfaceTexture(0);
    Handler handler = new Handler();
    AnalyzerRecognitionSync ars;
    JSONObject json;
    Watcher watcher;
    String imgPath;
    SleepActivity activity;

    public Watcher(SleepActivity activity) {
        this.activity = activity;
    }

    protected void setCameraIndex() {
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
    }

    protected Camera cameraOpen() {
        Camera camera;
        if (cameraIndex < 0) {
            camera = Camera.open();
        } else {
            camera = Camera.open(cameraIndex);
        }
        return camera;
    }

    protected void cameraInit(Camera camera) {
        try {
            camera.setPreviewTexture(surface);
        } catch (IOException e) {
            Log.d("tapia", "setPreviewTexture failed.");
        }
        Parameters params = camera.getParameters();
        params.setPictureSize(1280, 768);
        camera.setParameters(params);
        camera.startPreview();
    }

    protected void cameraUninit(Camera camera) {
        // camera.stopPreview();
    }

    protected void cameraRelease(Camera camera) {
        camera.release();
    }

    @Override
    public void run() {
        setCameraIndex();
        Camera camera = cameraOpen();
        watcher = this;

        while (running) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.d("tapia", "camera");
            }
            capture(camera);
        }

        cameraRelease(camera);
    }

    public void stop() {
        running = false;
    }
    public boolean isStopped() {
        if (running) {
            return false;
        }
        return true;
    }
    public DetectResult capture(Camera camera) {
        if (!pictureTaking) {
            pictureTaking = true;
            cameraInit(camera);
            camera.autoFocus(autoFocusCallback);
            Log.d("tapia", "autoFocus called.");
        }
        return DetectResult.NOT_FOUND;
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if (!isStopped()) {
                try {
                    camera.takePicture(null, null, pictureCallback);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Log.d("tapia", "Exception onAutoFocus");
                }
                Log.d("tapia", "onAutoFocus");
            } else {
                Log.d("tapia", "watcher stopped, so do nothing.(onAutoFocus)");
            }
        }
    };

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            if (!isStopped()) {
                Log.d("tapia", "onPictureTaken");
                String saveDir = Environment.getExternalStorageDirectory().getPath() + "/tapia";

                File file = new File(saveDir);

                if (!file.exists()) {
                    if (!file.mkdir()) {
                        Log.e("tapia", "Make Dir Error");
                    }
                }

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timeString = sf.format(cal.getTime());
                imgPath = saveDir + "/" + timeString + ".jpg";

                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(imgPath, true);
                    fos.write(data);
                    fos.close();
                    registAndroidDB(imgPath);
                    ars = new AnalyzerRecognitionSync();
                    ars.setImageParameter("MSG/FRAME_JPG_B64", imgPath);
                    ars.setParameter("MSG/FRAME_KEY", timeString);
                    new AsyncCaller(ars, json, watcher).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("EnterRoom::picture", e.getMessage());
                }

                fos = null;

                cameraUninit(camera);
            } else {
                Log.d("tapia", "watcher stopped, so do nothing.(onPictureTaken)");
            }
        }
    };

    private void registAndroidDB(String path) {
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = TapiaApp.getAppContext().getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put("_data", path);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void unregistAndroidDB(String path) {
        File file = new File(path);
        file.delete();
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = TapiaApp.getAppContext().getContentResolver();
        contentResolver.delete(MediaStore.Files.getContentUri("external"),
                MediaStore.Files.FileColumns.DATA + "=?",
                new String[]{ path });
    }

    public void onCompleteRecognition(ArrayList<String> names) {
        Log.d("tapia", "I'm working.");
        unregistAndroidDB(imgPath);
        activity.onRecognitionCompleted(names);
        pictureTaking = false;
    }
}
