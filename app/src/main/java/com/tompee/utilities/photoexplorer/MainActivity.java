package com.tompee.utilities.photoexplorer;

import android.os.Bundle;
import android.widget.TextView;

import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, false);
        setToolbarTitle(R.string.app_name);
    }

    private void setToolbarTitle(int resId) {
        TextView toolbar = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setText(resId);
    }
}
