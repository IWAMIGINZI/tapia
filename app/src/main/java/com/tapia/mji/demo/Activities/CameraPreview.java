package com.tapia.mji.demo.Activities;

/**
 * Created by ais75114 on 2018/01/26.
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.security.Policy;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCam;

    public CameraPreview(Context context, Camera cam) {
        super(context);

        mCam = cam;

        // サーフェスホルダーの取得とコールバック通知先の設定
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Camera.Parameters params=mCam.getParameters();
        List<Camera.Size> sizeList=params.getSupportedPictureSizes();
        for(int i=0;i<sizeList.size();i++){
            Log.v("CameraPictureSize","Size="+sizeList.get(i).width+"x"+sizeList.get(i).height);
        }
        params.setPictureSize(1280,768);
        mCam.setParameters(params);
        try {
            // カメラインスタンスに、画像表示先を設定
            mCam.setPreviewDisplay(holder);
            // プレビュー開始
            mCam.startPreview();
        } catch (IOException e) {
            //
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 画面回転に対応する場合は、ここでプレビューを停止し、
        // 回転による処理を実施、再度プレビューを開始する。
    }
}
