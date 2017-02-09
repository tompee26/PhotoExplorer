package com.tompee.utilities.photoexplorer.view;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.model.Category;
import com.tompee.utilities.photoexplorer.view.adapter.CategoryListAdapter;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";
    private static final String PREFECTURE_ARRAY_NAME = "Prefectures";

    private List<Category> mCategoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, false);
        setToolbarTitle(R.string.app_name);

        ListView listView = (ListView) findViewById(R.id.listview_category);
        listView.setOnItemClickListener(this);
        mCategoryList = new ArrayList<>();
        createCategoryList();
        CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.list_category,
                mCategoryList);
        listView.setAdapter(adapter);
    }

    private void createCategoryList() {
        int arrayId = getResources().getIdentifier(PREFECTURE_ARRAY_NAME, "array", getPackageName());
        TypedArray prefectureArray = getResources().obtainTypedArray(arrayId);
        Log.d(TAG, "Length of prefecture array: " + prefectureArray.length());
        for (int index = 0; index < prefectureArray.length(); index++) {
            String name = prefectureArray.getString(index);
            Log.d(TAG, "Prefecture name: " + name);
            int prefectureId = getResources().getIdentifier(name, "array", getPackageName());
            TypedArray prefectureObject = getResources().obtainTypedArray(prefectureId);
            //noinspection ConstantConditions,ConstantConditions
            mCategoryList.add(new Category(name, prefectureObject.getString(0),
                    prefectureObject.getString(1)));
            prefectureObject.recycle();
        }
        prefectureArray.recycle();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PhotoListActivity.class);
        intent.putExtra(PhotoListActivity.TAG_ID, mCategoryList.get(position).getId());
        intent.putExtra(PhotoListActivity.TAG_NAME, mCategoryList.get(position).getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        setNextTransition();
    }
}
