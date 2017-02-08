package com.tompee.utilities.photoexplorer.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.NoConnectionError;
import com.tompee.utilities.photoexplorer.controller.imageservice.FlickrWrapper;
import com.tompee.utilities.photoexplorer.model.Photo;

public class GetPhotoTask extends AsyncTask<Void, Void, Void> {
    private final String mWoeId;
    private final FlickrWrapper mFlickrWrapper;

    public GetPhotoTask(Context context, String woeId) {
        mWoeId = woeId;
        mFlickrWrapper = new FlickrWrapper(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Photo photo;
        try {
            photo = mFlickrWrapper.getPhotosById(mWoeId);
        } catch (NoConnectionError noConnectionError) {
            photo = null;
            noConnectionError.printStackTrace();
            return null;
        }
        for (String id : photo.getIdList()) {
            mFlickrWrapper.getPhotoInfo(id);
        }
        return null;
    }
}
