package com.tompee.utilities.photoexplorer.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.controller.task.GetPhotoTask;
import com.tompee.utilities.photoexplorer.model.Photo;
import com.tompee.utilities.photoexplorer.view.adapter.PhotoListAdapter;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PhotoListActivity extends BaseActivity implements GetPhotoTask.GetPhotoListener {
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    private static final String TAG = "PhotoListActivity";

    private GetPhotoTask mGetPhotoTask;
    private PhotoListAdapter mAdapter;
    private List<Photo> mPhotoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photolist);
        setToolbar(R.id.toolbar, true);
        setToolbarTitle(getIntent().getStringExtra(TAG_NAME));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_photo_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mPhotoList = new ArrayList<>();
        mAdapter = new PhotoListAdapter(this, mPhotoList);
        recyclerView.setAdapter(mAdapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        createGetPhotoTask();
    }

    private void createGetPhotoTask() {
        if (mGetPhotoTask == null) {
            mGetPhotoTask = new GetPhotoTask(this, getIntent().getStringExtra(TAG_ID), this);
            mGetPhotoTask.execute();
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError() {
    }

    @Override
    public void onNewPhoto(Photo photo) {
        int currentSize = mAdapter.getItemCount();
        mPhotoList.add(photo);
        mAdapter.notifyItemRangeInserted(currentSize, 1);
    }
}
