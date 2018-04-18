package com.tapia.mji.demo.Tools;

import android.util.Log;

public class Locker {
    private static Locker singleton = new Locker();

    public static int WORKER_DEFAULT = -1;
    public static int WORKER_MEDAMA = 0;
    public static int WORKER_NAISEN = 1;
    public static int WORKER_CAMERA = 2;
    private static int worker = WORKER_DEFAULT;

    private Locker() {}

    public static Locker getInstance(){
        return singleton;
    }

    // lockedWork 経由で使用すること
    public static void setWorker(int w) {
        worker = w;
    }
    //lockedWork 経由で使用すること
    public static int getWorker() {
        return worker;
    }

    public static synchronized void whenMedamaWorking(LockedWork lockedWork) {
        if (worker == WORKER_MEDAMA) {
            lockedWork.work();
        } else {
            lockedWork.workElse();
        }
    }

    public static synchronized void whenNaisenWorking(LockedWork lockedWork) {
        if (worker == WORKER_NAISEN) {
            lockedWork.work();
        } else {
            lockedWork.workElse();
        }
    }
}
