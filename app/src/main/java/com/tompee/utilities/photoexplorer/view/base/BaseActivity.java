package com.tompee.utilities.photoexplorer.view.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.controller.task.DelayTask;

public class BaseActivity extends FragmentActivity implements DelayTask.DelayListener {
    private AppCompatDelegate mDelegate;
    private boolean mIsTouchAllowed;
    private DelayTask mDelayTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate = AppCompatDelegate.create(this, null);
        mDelegate.onCreate(savedInstanceState);

        mIsTouchAllowed = false;
        mDelayTask = new DelayTask(getResources().getInteger(R.integer.
                activity_animation_duration), this);
        mDelayTask.execute();
    }

    @Override
    public void setContentView(int layoutResID) {
        mDelegate.setContentView(layoutResID);
    }

    @Override
    public void invalidateOptionsMenu() {
        mDelegate.invalidateOptionsMenu();
    }

    protected void setToolbar(int toolbarId, boolean enableHomeButton) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        mDelegate.setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        mDelegate.getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDelegate.getSupportActionBar().setDisplayHomeAsUpEnabled(enableHomeButton);
    }

    protected void setToolbarTitle(int resId) {
        TextView toolbar = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setText(resId);
    }

    protected void setToolbarTitle(String string) {
        TextView toolbar = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setText(string);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mDelegate.getSupportActionBar() != null) {
            mDelegate.getSupportActionBar().openOptionsMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                setBackTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setNextTransition() {
        overridePendingTransition(R.anim.slide_inleft, R.anim.slide_outleft);
    }

    protected void setBackTransition() {
        overridePendingTransition(R.anim.slide_inright, R.anim.slide_outright);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return !mIsTouchAllowed || super.dispatchTouchEvent(event);
    }

    @Override
    public void onDelayFinished() {
        mIsTouchAllowed = true;
        mDelayTask = null;
    }
}

