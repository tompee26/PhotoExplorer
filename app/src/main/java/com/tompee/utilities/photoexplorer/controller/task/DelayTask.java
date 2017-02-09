package com.tompee.utilities.photoexplorer.controller.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Async task is used for delay since Espresso only synchronizes with asynctask.
 */
public class DelayTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "DelayTask";
    private final DelayListener mListener;
    private final int mDelayTime;

    public DelayTask(int delayTime, @NonNull DelayListener listener) {
        mDelayTime = delayTime;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(mDelayTime);
        } catch (InterruptedException e) {
            Log.d(TAG, "Sleep cancelled");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mListener.onDelayFinished();
    }

    public interface DelayListener {
        void onDelayFinished();
    }
}