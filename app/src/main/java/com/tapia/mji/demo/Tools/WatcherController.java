package com.tapia.mji.demo.Tools;

import android.util.Log;

import com.tapia.mji.demo.Activities.SleepActivity;

public class WatcherController {

    Watcher watcher;
    Thread thread;
    SleepActivity activity;

    public WatcherController(SleepActivity activity) {
        this.activity = activity;
    }

    public boolean start() {
        watcher = new Watcher(activity);
        thread = new Thread(watcher);
        thread.start();
        DeviceLog.d("tapia", "WatcherController started.");
        return true;
    }

    public boolean stop() {
        watcher.stop();
        try {
            thread.join();
        } catch (InterruptedException e) {
            DeviceLog.d("tapia", "Watcher Thread join failed.", e);
            return false;
        }
        DeviceLog.d("tapia", "WatcherController stopped.");
        return true;
    }
}
