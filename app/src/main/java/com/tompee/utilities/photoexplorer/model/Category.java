package com.tompee.utilities.photoexplorer.model;

public class Category {
    private final int mType;
    private final String mName;
    private final String mUrl;
    private final String mId;

    public Category(int type, String name, String url, String id) {
        mType = type;
        mName = name;
        mUrl = url;
        mId = id;
    }

    public int getType() {
        return mType;
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
