package com.tapia.mji.demo.Labellio;

import android.content.Context;
import android.support.annotation.NonNull;

public class AnalyzerRecognitionSync extends WebAPI {

    public AnalyzerRecognitionSync(@NonNull Context context) {
        super(context);
        setParameter("CMD", "analyzer_recognition_sync");
    }
}
