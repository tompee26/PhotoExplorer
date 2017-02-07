package com.tompee.utilities.photoexplorer.model;

import android.support.annotation.NonNull;

public class Category {
    private String mName;
    private String mUrl;

    public Category(@NonNull String name, @NonNull String url) {
        mName = name;
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }
}
