package com.tompee.utilities.photoexplorer.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;
import com.tompee.utilities.photoexplorer.view.custom.TouchImageView;

public class ImageViewerActivity extends BaseActivity {
    public static final String TAG_URL = "url";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_REALNAME = "realname";
    public static final String TAG_TITLE = "title";

    private TouchImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ImageViewerScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewer);
        mImageView = (TouchImageView) findViewById(R.id.imageview_viewer);
        Picasso.with(this).load(getIntent().getStringExtra(TAG_URL)).into(mImageView);
        final View creditsView = findViewById(R.id.credits);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditsView.getVisibility() == View.VISIBLE) {
                    creditsView.setVisibility(View.GONE);
                } else {
                    creditsView.setVisibility(View.VISIBLE);
                }
            }
        });

        TextView textView = (TextView) findViewById(R.id.textview_title);
        textView.setText(getIntent().getStringExtra(TAG_TITLE));
        textView = (TextView) findViewById(R.id.textview_copyright);
        String copyright = String.format(getString(R.string.copyright_prefix), getIntent().
                getStringExtra(TAG_USERNAME));
        String realname = getIntent().getStringExtra(TAG_REALNAME);
        if (realname != null && realname.length() != 0) {
            copyright += String.format(" (%s)", realname);
        }
        textView.setText(copyright);
    }

    @Override
    public void onBackPressed() {
        mImageView.resetZoom();
        super.onBackPressed();
    }
}
