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
        Log.d("tapia", "WatcherController started.");
        return true;
    }

    public boolean stop() {
        watcher.stop();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d("tapia", "Watcher Thread join failed.");
            return false;
        }
        Log.d("tapia", "WatcherController stopped.");
        return true;
    }
}
