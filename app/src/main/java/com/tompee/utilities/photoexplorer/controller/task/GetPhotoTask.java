package com.tompee.utilities.photoexplorer.controller.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.tompee.utilities.photoexplorer.controller.imageservice.FlickrWrapper;
import com.tompee.utilities.photoexplorer.model.Photo;
import com.tompee.utilities.photoexplorer.model.PhotoGroup;

import java.util.List;

public class GetPhotoTask extends AsyncTask<Integer, Photo, Boolean> {
    private static final String TAG = "GetPhotoTask";
    private final String mWoeId;
    private final FlickrWrapper mFlickrWrapper;
    private final GetPhotoListener mGetPhotoListener;
    private final List<String> mIdList;
    private final List<Photo> mPhotoList;

    /* Max photos is set to prevent unstable behavior of recycler view and staggered grid view */
    private static final int MAX_PHOTOS = 20;

    public GetPhotoTask(Context context, List<Photo> photoList, List<String> idList,
                        String woeId, GetPhotoListener listener) {
        mPhotoList = photoList;
        mWoeId = woeId;
        mFlickrWrapper = new FlickrWrapper(context);
        mGetPhotoListener = listener;
        mIdList = idList;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        PhotoGroup photoGroup;
        try {
            photoGroup = mFlickrWrapper.getPhotosById(mWoeId, params[0]);
            List<String> idList = photoGroup.getIdList();
            if (isCancelled()) {
                return true;
            }
            for (String id : idList) {
                if (isCancelled()) {
                    return true;
                }
                if (mPhotoList.size() >= MAX_PHOTOS) {
                    return true;
                }
                if (!mIdList.contains(id)) {
                    Log.d(TAG, "Unique ID size: " + mIdList.size());
                    mIdList.add(id);
                    publishProgress(mFlickrWrapper.getPhotoInfo(id));
                } else {
                    Log.d(TAG, "ID not unique: " + id);
                }
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
