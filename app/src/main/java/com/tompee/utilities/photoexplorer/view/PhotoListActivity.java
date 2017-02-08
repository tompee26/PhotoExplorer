package com.tompee.utilities.photoexplorer.view;

import android.os.Bundle;

import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.controller.task.GetPhotoTask;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

public class PhotoListActivity extends BaseActivity {
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    private static final String TAG = "PhotoListActivity";

    private GetPhotoTask mGetPhotoTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        createGetPhotoTask();
    }

    private void createGetPhotoTask() {
        if (mGetPhotoTask == null) {
            mGetPhotoTask = new GetPhotoTask(this, getIntent().getStringExtra(TAG_ID));
            mGetPhotoTask.execute();
        }
    }
}
