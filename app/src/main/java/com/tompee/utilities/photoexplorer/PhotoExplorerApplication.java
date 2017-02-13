package com.tompee.utilities.photoexplorer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.tompee.utilities.photoexplorer.controller.network.VolleySingleton;

public class PhotoExplorerApplication extends Application {
    public static final String SHARED_PREF = "knowyourmedspref";
    public static final String TAG_DISCLAIMER = "disclaimer";
    private static final String TAG_VERSION = "version";

    @Override
    public void onCreate() {
        super.onCreate();
        VolleySingleton.getInstance(this);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,
                Context.MODE_PRIVATE);
        int version = sharedPreferences.getInt(TAG_VERSION, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (version < BuildConfig.VERSION_CODE) {
            editor.putBoolean(TAG_DISCLAIMER, false);
        }
        editor.putInt(TAG_VERSION, BuildConfig.VERSION_CODE);
        editor.apply();
    }
}
