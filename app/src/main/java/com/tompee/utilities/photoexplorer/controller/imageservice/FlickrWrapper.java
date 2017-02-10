package com.tompee.utilities.photoexplorer.controller.imageservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.squareup.picasso.Picasso;
import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.controller.network.VolleySingleton;
import com.tompee.utilities.photoexplorer.model.Photo;
import com.tompee.utilities.photoexplorer.model.PhotoGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class FlickrWrapper {
    private static final String TAG = "FlickrWrapper";
    private static final String MAIN_URL = "https://api.flickr.com/services/rest/?";

    private static final String PHOTO_SEARCH_URL = "%smethod=flickr.photos.search&api_key=%s&woe_id=" +
            "%s&sort=interestingness-desc&per_page=20&page=%d&format=json&nojsoncallback=1";
    private static final String PHOTO_INFO_URL = "%smethod=flickr.photos.getInfo&api_key=%s&photo_id=" +
            "%s&format=json&nojsoncallback=1";
    private static final String PHOTO_SIZES_URL = "%smethod=flickr.photos.getSizes&api_key=%s&photo_id=" +
            "%s&format=json&nojsoncallback=1";

    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_PAGE = "page";
    private static final String TAG_PAGES = "pages";
    private static final String TAG_TOTAL = "total";
    private static final String TAG_PHOTO = "photo";
    private static final String TAG_ID = "id";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_REALNAME = "realname";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "_content";
    private static final String TAG_SIZES = "sizes";
    private static final String TAG_SIZE = "size";
    private static final String TAG_LABEL = "label";
    private static final String TAG_SOURCE = "source";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WIDTH = "width";

    private static final String THUMBNAIL_LABEL = "Medium 800";
    private static final String MEDIUM_800_LABEL = "Medium 800";

    private final Context mContext;

    public FlickrWrapper(Context context) {
        mContext = context;
    }

    public PhotoGroup getPhotosById(String woeId, int page) throws NoConnectionError {
        @SuppressLint("DefaultLocale") String url = String.format(PHOTO_SEARCH_URL, MAIN_URL,
                mContext.getString(R.string.flickr_api_key), woeId, page);
        Log.d(TAG, "PHOTO_SEARCH_URL: " + url);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject photos = future.get().getJSONObject(TAG_PHOTOS);
            PhotoGroup photoGroup = new PhotoGroup(photos.getInt(TAG_PAGES), photos.getInt(TAG_PAGE),
                    photos.getInt(TAG_TOTAL));
            JSONArray array = photos.getJSONArray(TAG_PHOTO);
            for (int index = 0; index < array.length(); index++) {
                photoGroup.addId(array.getJSONObject(index).getString(TAG_ID));
            }
            return photoGroup;
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
        }
        return null;
    }

    public Photo getPhotoInfo(String id) throws NoConnectionError {
        String url = String.format(PHOTO_INFO_URL, MAIN_URL,
                mContext.getString(R.string.flickr_api_key), id);
        Photo photo = new Photo();
        Log.d(TAG, "PHOTO_INFO_URL: " + url);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject photoObject = future.get().getJSONObject(TAG_PHOTO);
            photo.setTitle(photoObject.getJSONObject(TAG_TITLE).getString(TAG_CONTENT));
            photo.setRealName(photoObject.getJSONObject(TAG_OWNER).getString(TAG_REALNAME));
            photo.setUserName(photoObject.getJSONObject(TAG_OWNER).getString(TAG_USERNAME));
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
            return null;
        }
        url = String.format(PHOTO_SIZES_URL, MAIN_URL, mContext.getString(R.string.flickr_api_key), id);
        Log.d(TAG, "PHOTO_SIZES_URL: " + url);
        future = RequestFuture.newFuture();
        jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONArray sizes = future.get().getJSONObject(TAG_SIZES).getJSONArray(TAG_SIZE);
            for (int index = 0; index < sizes.length(); index++) {
                JSONObject sizeObject = sizes.getJSONObject(index);
                if (sizeObject.getString(TAG_LABEL).equals(THUMBNAIL_LABEL)) {
                    photo.setThumbnailUrl(sizeObject.getString(TAG_SOURCE));
                    photo.setWidth(sizeObject.getInt(TAG_WIDTH));
                    photo.setHeight(sizeObject.getInt(TAG_HEIGHT));
                    /* Preload images */
                    Picasso.with(mContext).load(sizeObject.getString(TAG_SOURCE)).fetch();
                } else if (sizeObject.getString(TAG_LABEL).equals(MEDIUM_800_LABEL)) {
                    photo.setViewableImageUrl(sizeObject.getString(TAG_SOURCE));
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Log.e(TAG, e.getMessage());
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
            return null;
        }
        return photo;
    }
}
