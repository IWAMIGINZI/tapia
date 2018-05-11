package com.tapia.mji.demo.Tools;

import android.util.Log;

public class WatcherController {

    Watcher watcher;
    Thread thread;

    public boolean start() {
        watcher = new Watcher();
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
