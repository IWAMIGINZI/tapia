package com.tapia.mji.demo.Tools;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tapia.mji.tapialib.TapiaApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeviceLog {
    DeviceLog logger = new DeviceLog();
    private DeviceLog() {}
    public DeviceLog getLogger() {
        return logger;
    }

    public static void d2file(String tag, String msg, Throwable tr) {
        try {
            String millis = "000" + (System.currentTimeMillis() % 1000);
            millis = millis.substring(millis.length() - 3);
            Date d = new Date();
            String dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/log";
            File dir = new File(dirpath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            SimpleDateFormat fn = new SimpleDateFormat("yyyyMMdd");
            String path = dirpath + "/" + tag + "-" + fn.format(d) + ".log";
            File file = new File(path);
            String delpath = null;
            boolean doScan = false;
            if (!file.exists()) {
                // 新規にファイルを作成することになるので、1週間前のファイルは削除してしまう。
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                delpath = dirpath + "/" + tag + "-" + fn.format(calendar.getTime()) + ".log";
                File delFile = new File(delpath);
                if (delFile.exists()) {
                    delFile.delete();
                }
            }
            SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String log = ts.format(d) + "." + millis + " D/" + tag + ": " + msg;
            if (tr != null) {
                log += "\n" + Log.getStackTraceString(tr);
            }
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                if (tr != null) {
                    pw.print(log);
                } else {
                    pw.println(log);
                }
                pw.close();
                if (doScan) {
                    MediaScannerConnection.scanFile(
                            TapiaApp.getAppContext(),
                            new String[]{path},
                            null,
                            null);
                    TapiaApp.getAppContext().getContentResolver().delete(
                            MediaStore.Files.getContentUri("external"),
                            MediaStore.Files.FileColumns.DATA + "=?",
                            new String[]{ delpath });
                }
            } catch (IOException e) {
                // ログ出力用のクラス内でLogを出さねばならないという間抜けな処理
                Log.d("tapia", "Print Log Failed.", e);
            }
        } catch (RuntimeException e) {
            // ログ出力用のクラス内でLogを出さねばならないという間抜けな処理
            Log.d("tapia", "DeviceLog Failed.", e);
        }
    }

    public static int d(String tag, String msg, Throwable tr) {
        d2file(tag, msg, tr);
        return Log.d(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        d2file(tag, msg, null);
        return Log.d(tag, msg);
    }
}
