package com.tompee.utilities.photoexplorer;

import android.app.Application;

import com.tompee.utilities.photoexplorer.controller.network.VolleySingleton;

public class PhotoExplorerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VolleySingleton.getInstance(this);
    }
}
