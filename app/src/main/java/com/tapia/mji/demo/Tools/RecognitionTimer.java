package com.tapia.mji.demo.Tools;

import android.os.Handler;
import android.os.Looper;

import com.tapia.mji.demo.Activities.SleepActivity;

import java.util.TimerTask;

public class RecognitionTimer extends TimerTask {
    final Handler handler = new Handler(Looper.getMainLooper());
    SleepActivity activity;

    public RecognitionTimer(SleepActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                activity.onRecognitionCompleteTimer();
            }
        });
    }
}
