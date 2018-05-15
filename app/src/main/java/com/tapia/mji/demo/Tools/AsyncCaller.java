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

public class AsyncCaller extends AsyncTask<Void, Void, Void> {
    AnalyzerRecognitionSync ars;
    JSONObject json;
    Watcher watcher;

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
            //++ Log.d("tapia", jsons);
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
                doOpenSesami(code, name, face);
            }
        } catch (JSONException e) {
            Log.d("tapia", "JSONException in actionOpenSesami");
        }
    }

    private void doOpenSesami(String code, String name, JSONObject face) {
        Log.d("tapia", "Found: code=" + code + ", name=" + name);
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
            writer.println("OPENDOOR " + code);
            String rc = reader.readLine();
            Log.d("tapia", code + ": Open Door: " + rc);
            reader.close();
            writer.close();
            sock.close();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        //++ Log.d("tapia", "async task onPostExecute");
        watcher.onCompleteRecognition();
    }
}
