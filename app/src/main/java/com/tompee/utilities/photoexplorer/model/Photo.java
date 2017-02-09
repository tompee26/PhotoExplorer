package com.tompee.utilities.photoexplorer.model;

public class Photo {
    private String mUsername;
    private String mRealName;
    private String mTitle;
    private String mThumbnailUrl;
    private String mViewableImageUrl;

    public String getUsername() {
        return mUsername;
    }

    public void setUserName(String username) {
        mUsername = username;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        mRealName = realName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public String getViewableImageUrl() {
        return mViewableImageUrl;
    }

    public void setViewableImageUrl(String viewableImageUrl) {
        mViewableImageUrl = viewableImageUrl;
    }
}
