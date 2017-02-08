package com.tompee.utilities.photoexplorer.model;

import android.support.annotation.NonNull;

public class Category {
    private String mName;
    private String mUrl;
    private String mId;

    public Category(@NonNull String name, @NonNull String url, @NonNull String id) {
        mName = name;
        mUrl = url;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getId() {
        return mId;
    }
}
