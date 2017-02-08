package com.tompee.utilities.photoexplorer;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.photoexplorer.model.Category;
import com.tompee.utilities.photoexplorer.view.adapter.CategoryListAdapter;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFECTURE_ARRAY_NAME = "Prefectures";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, false);
        setToolbarTitle(R.string.app_name);

        ListView listView = (ListView) findViewById(R.id.listview_category);
        List<Category> categoryList = new ArrayList<>();
        createCategoryList(categoryList);
        CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.list_category,
                categoryList);
        listView.setAdapter(adapter);
    }

    private void setToolbarTitle(int resId) {
        TextView toolbar = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setText(resId);
    }

    private void createCategoryList(List<Category> list) {
        int arrayId = getResources().getIdentifier(PREFECTURE_ARRAY_NAME, "array", getPackageName());
        TypedArray prefectureArray = getResources().obtainTypedArray(arrayId);
        Log.d(TAG, "Length of prefecture array: " + prefectureArray.length());
        for (int index = 0; index < prefectureArray.length(); index++) {
            String name = prefectureArray.getString(index);
            Log.d(TAG, "Prefecture name: " + name);
            int prefectureId = getResources().getIdentifier(name, "array", getPackageName());
            TypedArray prefectureObject = getResources().obtainTypedArray(prefectureId);
            list.add(new Category(name, prefectureObject.getString(0), prefectureObject.getString(1)));
        }
//        list.add(new Category("Tokyo", "http://c1.staticflickr.com/8/7022/6788055657_c450e39855_b.jpg"));
//        list.add(new Category("Sapporo", "http://c2.staticflickr.com/2/1687/26375975211_2b80aa93c2_b.jpg"));
//        list.add(new Category("Nagoya", "http://c1.staticflickr.com/3/2625/13011103584_c589a35c7f_b.jpg"));
    }
}
