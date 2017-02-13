package com.tompee.utilities.photoexplorer.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
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
        ItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_URL = "url";
    public static final String TAG_CAPITAL = "capital";
    public static final String TAG_WEBSITE = "website";

    private static final String TAG = "PhotoListActivity";

    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GetPhotoTask mGetPhotoTask;
    private PhotoListAdapter mAdapter;
    private List<Photo> mPhotoList;
    private List<String> mIdList;
    private int mPageCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.PhotoListTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photolist);
        setToolbar(R.id.toolbar, true);
        ImageView toolbarBg = (ImageView) findViewById(R.id.toolbar_bg);
        toolbarBg.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        ImageView imageView = (ImageView) findViewById(R.id.image_view_header);
        Picasso.with(this).load(getIntent().getStringExtra(TAG_URL)).into(imageView);
        TextView textView = (TextView) findViewById(R.id.textview_main_name);
        textView.setText(String.format(getString(R.string.explore_prefix),
                getIntent().getStringExtra(TAG_NAME)));

        /* Set up cardview */
        textView = (TextView) findViewById(R.id.textview_title);
        textView.setText(String.format(getString(R.string.prefecture_prefix),
                getIntent().getStringExtra(TAG_NAME)));
        textView = (TextView) findViewById(R.id.textview_capital);
        textView.setText(String.format(getString(R.string.capital_prefix),
                getIntent().getStringExtra(TAG_CAPITAL)));
        textView = (TextView) findViewById(R.id.textview_website);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            textView.setText(Html.fromHtml(String.format(getString(R.string.website_prefix),
                    getIntent().getStringExtra(TAG_WEBSITE)), Html.FROM_HTML_MODE_LEGACY));
        } else {
            //noinspection deprecation
            textView.setText(Html.fromHtml(String.format(getString(R.string.website_prefix),
                    getIntent().getStringExtra(TAG_WEBSITE))));
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        createGetPhotoTask();
    }

    private void createGetPhotoTask() {
        if (mGetPhotoTask == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mGetPhotoTask = new GetPhotoTask(this, mPhotoList, mIdList, getIntent().
                    getStringExtra(TAG_ID), this);
            mGetPhotoTask.execute(mPageCount);
        }
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "Page count: " + mPageCount);
        mPageCount++;
        mGetPhotoTask = null;
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError() {
        mGetPhotoTask = null;
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
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
        /* Prefetch data */
        Picasso.with(this).load(photo.getViewableImageUrl()).fetch();
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ImageViewerActivity.TAG_URL, photo.getViewableImageUrl());
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

    @Override
    public void onRefresh() {
        if (mGetPhotoTask != null) {
            mGetPhotoTask.isCancelled();
        }
        mPhotoList.clear();
        mAdapter.notifyDataSetChanged();
        createGetPhotoTask();
    }
}
