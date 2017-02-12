package com.tompee.utilities.photoexplorer.model;

public class Category {
    private final int mType;
    private final String mName;
    private final String mUrl;
    private final String mId;
    private final String mCapital;
    private final String mWebsite;

    public Category(int type, String name, String url, String id, String capital, String website) {
        mType = type;
        mName = name;
        mUrl = url;
        mId = id;
        mCapital = capital;
        mWebsite = website;
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

    public String getCapital() {
        return mCapital;
    }

    public String getWebsite() {
        return mWebsite;
    }
}
