package com.tapia.mji.demo.Labellio;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class WebAPI {
    private String labellioApiUrl;
    private String labellioAccessKey;
    private HashMap<String, String> params;

    WebAPI(@NonNull Context context) {
        ApplicationInfo appliInfo = null;
        try {
            appliInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            labellioApiUrl = appliInfo.metaData.getString("labellio_api_url");
            labellioAccessKey = appliInfo.metaData.getString("labellio_access_key");
            params = new HashMap<String, String>();
            setParameter("ACCESS_KEY", labellioAccessKey);
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public void setParameter(String key, String value) {
        setParameter(key, value, new Encoder());
    }

    public void setImageParameter(String key, String value) {
        setParameter(key, value, new ImageEncoder());
    }

    public void setParameter(String key, String value, Encoder encoder) {
        try {
            params.put(key, encoder.encode(value));
        } catch (Exception e) {
            Log.e("WebAPI::setParameter", e.getMessage());
        }
    }

    public String getJSON() {
        JSONObject obj = new JSONObject();
        for (String key: params.keySet()) {
            int pos = key.indexOf('/');
            try {
                if (pos < 0) {
                    obj.put(key, params.get(key));
                } else {
                    String subkey = key.substring(pos + 1);
                    String mainkey = key.substring(0, pos);
                    JSONObject sub = null;
                    if (obj.has(mainkey)) {
                        sub = obj.getJSONObject(mainkey);
                    } else {
                        sub = new JSONObject();
                    }
                    sub.put(subkey, params.get(key));
                    obj.put(mainkey, sub);
                }
            } catch (JSONException e) {
                Log.d("tapia", "JSONObject");
            }
        }
        return obj.toString();
    }

    public JSONObject post() throws JSONException {
        HttpURLConnection con = null;
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(labellioApiUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(false);
            con.setRequestProperty("Accept-Language", "jp");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            String json = getJSON();
            Log.d("Request JSON", json);
            con.setRequestProperty("Content-Length", String.valueOf(json.length()));
            OutputStream os = con.getOutputStream();
            PrintStream ps = new PrintStream(os);
            ps.print(json);
            ps.close();

            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                final InputStream in = con.getInputStream();
                String encoding = con.getContentEncoding();
                if (null == encoding) {
                    encoding = "UTF-8";
                }
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();
            } else {
                Log.d("WebAPI::HTTP RESPONSE", String.valueOf(status));
            }
        } catch (Exception e1) {
            Log.e("WebAPI::Exception", e1.getMessage());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return new JSONObject(result.toString());
    }
}
