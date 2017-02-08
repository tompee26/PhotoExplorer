package com.tompee.utilities.photoexplorer.controller.imageservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.controller.network.VolleySingleton;
import com.tompee.utilities.photoexplorer.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FlickrWrapper {
    private static final String TAG = "FlickrWrapper";
    private static final String MAIN_URL = "https://api.flickr.com/services/rest/?";

    private static final String PHOTO_SEARCH_URL = "%smethod=flickr.photos.search&api_key=%s&woe_id=" +
            "%s&sort=interestingness-desc&format=json&nojsoncallback=1";

    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_PAGE = "page";
    private static final String TAG_PAGES = "pages";
    private static final String TAG_TOTAL = "total";
    private static final String TAG_PHOTO = "photo";
    private static final String TAG_ID = "id";

    private final Context mContext;

    public FlickrWrapper(Context context) {
        mContext = context;
    }

    public Photo getPhotosById(String woeId) throws NoConnectionError {
        String url = String.format(PHOTO_SEARCH_URL, MAIN_URL,
                mContext.getString(R.string.flickr_api_key), woeId);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, future, future);
        VolleySingleton.getInstance(mContext).addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            List<String> idList = new ArrayList<>();
            JSONArray array = response.getJSONObject(TAG_PHOTOS).getJSONArray(TAG_PHOTO);
            for(int index = 0; index < array.length(); index++) {
                idList.add(array.getJSONObject(index).getString(TAG_ID));
            }
            Log.d(TAG, array.toString());
            return new Photo(response.getInt(TAG_PAGES), response.getInt(TAG_PAGE),
                    response.getInt(TAG_TOTAL), idList);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            if (e.getCause() instanceof NoConnectionError) {
                throw (NoConnectionError) e.getCause();
            }
        }
        return null;
    }

    public void getPhotoInfo(String id) {

    }
}
