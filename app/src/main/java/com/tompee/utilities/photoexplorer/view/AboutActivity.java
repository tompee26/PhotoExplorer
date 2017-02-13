package com.tompee.utilities.photoexplorer.view;

import android.os.Bundle;
import android.widget.TextView;

import com.tompee.utilities.photoexplorer.BuildConfig;
import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setToolbar(R.id.toolbar, true);
        setToolbarTitle(R.string.about);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));
    }
}
