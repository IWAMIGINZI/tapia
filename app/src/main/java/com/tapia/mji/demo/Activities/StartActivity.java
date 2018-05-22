package com.tapia.mji.demo.Activities;


import android.content.Intent;
import android.os.Bundle;

import com.tapia.mji.tapialib.Activities.TapiaActivity;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.TapiaApp;
import com.tapia.mji.tapialib.Utils.TapiaAudio;

/**
 * Created by Sami on 30-Jun-16.
 */
public class StartActivity extends TapiaActivity {

    //TapiaAnimation tapiaAnimation;
    //SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TapiaAudio.setVolume(this, 0, false);
        //TapiaApp.setCurrentLanguage(Language.LanguageID.JAPANESE);
        //タブレットで開発するときは以下をfalseにする
        TapiaApp.ENABLE_ROBOT_FEATURE = true;
        TapiaApp.hideAndroidUi(false);
        startActivity(new Intent(activity,SleepActivity.class));
        //↓壁紙をTAPIAに変えてしまう
/*        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setResource(R.raw.anim00188);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        finish();
    }
}
