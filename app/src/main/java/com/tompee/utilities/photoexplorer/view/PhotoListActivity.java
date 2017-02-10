package com.tompee.utilities.photoexplorer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.controller.listener.EndlessRecyclerViewScrollListener;
import com.tompee.utilities.photoexplorer.controller.listener.ItemClickListener;
import com.tompee.utilities.photoexplorer.controller.task.GetPhotoTask;
import com.tompee.utilities.photoexplorer.model.Photo;
import com.tompee.utilities.photoexplorer.view.adapter.PhotoListAdapter;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PhotoListActivity extends BaseActivity implements GetPhotoTask.GetPhotoListener,
        ItemClickListener.OnItemClickListener {
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    private static final String TAG = "PhotoListActivity";

    private GetPhotoTask mGetPhotoTask;
    private PhotoListAdapter mAdapter;
    private List<Photo> mPhotoList;
    private List<String> mIdList;
    private int mPageCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photolist);
        setToolbar(R.id.toolbar, true);
        setToolbarTitle(getIntent().getStringExtra(TAG_NAME));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_photo_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        ItemClickListener.addTo(recyclerView).setOnItemClickListener(this);
        mIdList = new ArrayList<>();

        mPhotoList = new ArrayList<>();
        mAdapter = new PhotoListAdapter(this, mPhotoList);
        recyclerView.setAdapter(mAdapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "Loading more photos!");
                createGetPhotoTask();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        createGetPhotoTask();
    }

    private void createGetPhotoTask() {
        if (mGetPhotoTask == null) {
            mGetPhotoTask = new GetPhotoTask(this, mIdList, getIntent().getStringExtra(TAG_ID), this);
            mGetPhotoTask.execute(mPageCount);
        }
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "Page count: " + mPageCount);
        mPageCount++;
        mGetPhotoTask = null;
    }

    @Override
    public void onError() {
        mGetPhotoTask = null;
    }

    @Override
    public void onNewPhoto(Photo photo) {
        int currentSize = mAdapter.getItemCount();
        mPhotoList.add(photo);
        mAdapter.notifyItemRangeInserted(currentSize, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGetPhotoTask != null) {
            mGetPhotoTask.cancel(true);
            mGetPhotoTask = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createGetPhotoTask();
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Photo photo = mPhotoList.get(position);
        Log.d(TAG, "Position: " + position + " Title: " + photo.getTitle());
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ImageViewerActivity.TAG_URL, photo.getThumbnailUrl());
        intent.putExtra(ImageViewerActivity.TAG_TITLE, photo.getTitle());
        intent.putExtra(ImageViewerActivity.TAG_REALNAME, photo.getRealName());
        intent.putExtra(ImageViewerActivity.TAG_USERNAME, photo.getUsername());

        Pair<View, String> photoTransition = Pair.create((v.findViewById(R.id.
                imageview_photo)), "photo");
        //noinspection unchecked
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, photoTransition);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBackTransition();
    }
}
