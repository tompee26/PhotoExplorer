package com.tompee.utilities.photoexplorer.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LicenseActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        setToolbar(R.id.toolbar, true);
        setToolbarTitle(R.string.license);

        TextView content = (TextView) findViewById(R.id.content);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            content.setText(Html.fromHtml(getStringFromAsset(this, "opensource.html"),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            content.setText(Html.fromHtml(getStringFromAsset(this, "opensource.html")));
        }
        content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String getStringFromAsset(Context context, String filename) {
        StringBuilder buffer = new StringBuilder();
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
