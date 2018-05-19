package com.tapia.mji.demo.Tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.tapia.mji.demo.Labellio.AnalyzerRecognitionSync;
import com.tapia.mji.tapialib.TapiaApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class AsyncCaller extends AsyncTask<Void, Void, Void> {
    AnalyzerRecognitionSync ars;
    JSONObject json;
    Watcher watcher;
    ArrayList<String> names = new ArrayList<String>();

    public AsyncCaller(AnalyzerRecognitionSync ars, JSONObject json, Watcher watcher) {
        this.ars = ars;
        this.json = json;
        this.watcher = watcher;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            json = ars.post();
            String jsons = json.toString();
            //++ DeviceLog.d("tapia", jsons);
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
                DeviceLog.d("tapia", "検出失敗");
            }
        } catch (JSONException e) {
            DeviceLog.d("tapia", "JSONException in actionRecognition", e);
        }
    }

    private void actionRecognitionResult(JSONObject result) {
        try {
            if (result.has("FACE")) {
                actionRecognitionFace(result.getJSONArray("FACE"));
            }
        } catch (JSONException e) {
            DeviceLog.d("tapia", "JSONException in actionRecognitionResult", e);
        }
    }

    private void actionRecognitionFace(JSONArray faces) {
        try {
            names.clear();
            for (int i = 0; i < faces.length(); i++) {
                JSONObject face = faces.getJSONObject(i);
                if (face.has("PERSON_CODE")) {
                    actionOpenSesami(face);
                    if (face.has("PERSON_NAME")) {
                        names.add(face.getString("PERSON_NAME"));
                    }
                }
            }
        } catch (JSONException e) {
            DeviceLog.d("tapia", "JSONException in actionRecognitionFace", e);
        }
    }

    private void actionOpenSesami(JSONObject face) {
        try {
            String code = face.getString("PERSON_CODE");
            if (!code.equals("00000000")) {
                String name = "";
                if (face.has("PERSON_NAME")) {
                    name = face.getString("PERSON_NAME");
                    doOpenSesami(code, name, face);
                }
            } else {
                DeviceLog.d("tapia","face recognition code Unknown.");

            }
        } catch (JSONException e) {
            DeviceLog.d("tapia", "JSONException in actionOpenSesami", e);
        }
    }

    private void doOpenSesami(String code, String name, JSONObject face) {
        DeviceLog.d("tapia", "Found: code=" + code + ", name=" + name);
        ApplicationInfo appliInfo = null;
        Context context = TapiaApp.getAppContext();
        try {
            appliInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String server = appliInfo.metaData.getString("opensesami_server");
            int port = appliInfo.metaData.getInt("opensesami_port");
            Socket sock = new Socket(server, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
            int c;
            while ((c = reader.read()) != -1) {
                if (c == '>') { // RJE> の > のみを判断に使用する。
                    break;
                }
            }
            writer.println("OPEN DOOR " + code);
            String rc = reader.readLine();
            DeviceLog.d("tapia", code + ": Open Door: " + rc);
            reader.close();
            writer.close();
            sock.close();
        } catch (PackageManager.NameNotFoundException e) {
            DeviceLog.d("tapia", "doOpenSesami::NameNotFoundException", e);
        } catch (IOException e) {
            DeviceLog.d("tapia", "doOpenSesami::IOException", e);
        }
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        //++ DeviceLog.d("tapia", "async task onPostExecute");
        watcher.onCompleteRecognition(names);
    }
}
