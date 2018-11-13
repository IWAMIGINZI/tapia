package com.tapia.mji.demo.Tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.tapia.mji.demo.Labellio.AnalyzerRecognitionSync;
import com.tapia.mji.demo.Labellio.LearnerLearning;
import com.tapia.mji.tapialib.TapiaApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
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
                //Log.d("processRoute", "認証画像 BASE64：" + face.getString("FACE_JPG_B64"));
                if (face.has("PERSON_CODE")) {
                    //Log.d("processRoute", "認証／解錠処理開始 時刻：" + new Timestamp(System.currentTimeMillis()));
                    boolean opened = actionOpenSesami(face);
                    //Log.d("processRoute", "認証／解錠処理終了 時刻：" + new Timestamp(System.currentTimeMillis()));
                    if (opened) {
                        if (face.has("PERSON_NAME")) {
                            String name = face.getString("PERSON_NAME");

                            if(face.has("RECOGNITION_ACCURACY")){
                                //double distance = face.getDouble("RECOGNITION_DISTANCE");
                                //int recognitionrate = (int) (((double)1 - distance) * 100);
                                int recognitionrate = face.getInt("RECOGNITION_ACCURACY");
                                name += " " + recognitionrate + "%";

                               //if(recognitionrate >= 60){
                               //     actionLearningFace(face);
                               //}

                            }

                            //if (face.has("TALK")) {
                                //if (face.getString("TALK").equals("1")) {
                                //if (face.getInt("TALK") == 1) {
                                    if(face.has("KANA")){
                                        if(!face.getString("KANA").equals("") )
                                        name += "@" + face.getString("KANA");
                                    }
                                    name += "::talk";
                                //}
                            //}
                            names.add(name);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            DeviceLog.d("tapia", "JSONException in actionRecognitionFace", e);
        }
    }

    private boolean actionOpenSesami(JSONObject face) {
        boolean opened = false;
        try {
            String code = face.getString("PERSON_CODE");
            if (!code.equals("00000000")) {
                //if (face.has("RECOGNITION_DISTANCE")) {
                if (face.has("RECOGNITION_ACCURACY")) {
                    //double distance = face.getDouble("RECOGNITION_DISTANCE");
                    double accuracy = (100 - face.getDouble("RECOGNITION_ACCURACY")) / 100;
                    Context context = TapiaApp.getAppContext();
                    ApplicationInfo appliInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                    float threshold = appliInfo.metaData.getFloat("distance_threshold");
                    if(face.has("EXCLUSION")){
                        if(face.getInt("EXCLUSION") == 1){

                        } else {
                            if (accuracy <= threshold) {
                                String name = "";
                                if (face.has("PERSON_NAME")) {
                                    name = face.getString("PERSON_NAME");
                                    doOpenSesami(code, name, face);
                                    opened = true;
                                }
                            }
                        }
                    } else {
                        //if (distance <= threshold) {
                        if (accuracy <= threshold) {
                            String name = "";
                            if (face.has("PERSON_NAME")) {
                                name = face.getString("PERSON_NAME");
                                doOpenSesami(code, name, face);
                                opened = true;
                            }
                        }
                    }
                }
            } else {
                DeviceLog.d("tapia","face recognition code Unknown.");

            }
        } catch (JSONException e) {
            DeviceLog.d("tapia", "JSONException in actionOpenSesami", e);
        } catch (PackageManager.NameNotFoundException e) {
            DeviceLog.d("tapia", "get RECOGNITION_DISTANCE", e);
        }
        return opened;
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

    // 検出画像登録処理
    private void actionLearningFace(JSONObject face){
        try{
            LearnerLearning ll = new LearnerLearning();
            ll.setParameter("MSG/FRAME_JPG_B64", face.getString("FACE_JPG_B64"));
            ll.setParameter("MSG/PERSON_CODE", face.getString("PERSON_CODE"));
            ll.setParameter("MSG/PERSON_NAME", face.getString("PERSON_NAME"));
            //ll.setParameter("MSG/BIRTHDAY", face.getString("BIRTHDAY"));
            new AsyncCaller_ll(ll, json).execute();
            /*if (json_ll.getInt("STATUS") == 0) {
                JSONObject result_ll = json_ll.getJSONObject("RESULT");
                if (result_ll.has("FACE")) {
                    DeviceLog.d("learning", "登録成功");
                }

            }else{
                DeviceLog.d("learning", "登録失敗");
            }*/
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("Learning::picture", e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        //++ DeviceLog.d("tapia", "async task onPostExecute");
        watcher.onCompleteRecognition(names);
    }
}
