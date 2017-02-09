package com.tompee.utilities.photoexplorer.controller.task;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.NoConnectionError;
import com.tompee.utilities.photoexplorer.controller.imageservice.FlickrWrapper;
import com.tompee.utilities.photoexplorer.model.Photo;
import com.tompee.utilities.photoexplorer.model.PhotoGroup;

import java.util.List;

public class GetPhotoTask extends AsyncTask<Void, Photo, Boolean> {
    private final String mWoeId;
    private final FlickrWrapper mFlickrWrapper;
    private final GetPhotoListener mGetPhotoListener;

    public GetPhotoTask(Context context, String woeId, GetPhotoListener listener) {
        mWoeId = woeId;
        mFlickrWrapper = new FlickrWrapper(context);
        mGetPhotoListener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        PhotoGroup photoGroup;
        try {
            photoGroup = mFlickrWrapper.getPhotosById(mWoeId);
            List<String> idList = photoGroup.getIdList();
            for (String id : idList) {
                publishProgress(mFlickrWrapper.getPhotoInfo(id));
            }
            return true;
        } catch (NoConnectionError noConnectionError) {
            noConnectionError.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Photo... photo) {
        mGetPhotoListener.onNewPhoto(photo[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            mGetPhotoListener.onCompleted();
        } else {
            mGetPhotoListener.onError();
        }
    }

    public interface GetPhotoListener {
        void onCompleted();

        void onError();

        void onNewPhoto(Photo photo);
    }
}
