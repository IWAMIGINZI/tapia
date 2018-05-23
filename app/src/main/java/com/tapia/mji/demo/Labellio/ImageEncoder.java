package com.tapia.mji.demo.Labellio;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;

public class ImageEncoder extends Encoder {

    @Override
    public String encode(String param) throws Exception {
        File file = new File(param);
        int fileLen = (int)file.length();
        byte[] data = new byte[fileLen];
        FileInputStream fis = new FileInputStream(file);
        fis.read(data);
        return Base64.encodeToString(data, Base64.DEFAULT | Base64.NO_WRAP);
    }
}
