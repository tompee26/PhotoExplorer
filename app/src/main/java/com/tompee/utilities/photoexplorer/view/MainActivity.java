package com.tompee.utilities.photoexplorer.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tompee.utilities.photoexplorer.PhotoExplorerApplication;
import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.model.Category;
import com.tompee.utilities.photoexplorer.view.adapter.CategoryListAdapter;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;
import com.tompee.utilities.photoexplorer.view.dialog.DisclaimerDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        DisclaimerDialog.DisclaimerDialogListener {
    private static final String PREFECTURE_ARRAY_NAME = "Prefectures";

    private List<Category> mCategoryList;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, false);
        setToolbarTitle(R.string.app_name);

        mSharedPreferences = getSharedPreferences(PhotoExplorerApplication.SHARED_PREF,
                Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(PhotoExplorerApplication.TAG_DISCLAIMER, false)) {
            showDisclaimer(true);
        }

        ListView listView = (ListView) findViewById(R.id.listview_category);
        listView.setOnItemClickListener(this);
        mCategoryList = new ArrayList<>();
        createCategoryList();
        CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.list_category,
                mCategoryList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                setNextTransition();
                break;
            case R.id.menu_disclaimer:
                showDisclaimer(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createCategoryList() {
        int arrayId = getResources().getIdentifier(PREFECTURE_ARRAY_NAME, "array", getPackageName());
        TypedArray prefectureArray = getResources().obtainTypedArray(arrayId);
        for (int index = 0; index < prefectureArray.length(); index++) {
            String name = prefectureArray.getString(index);
            int prefectureId = getResources().getIdentifier(name, "array", getPackageName());
            TypedArray prefectureObject = getResources().obtainTypedArray(prefectureId);
            if (prefectureObject.getInt(0, 0) == CategoryListAdapter.TYPE_SECTION) {
                /* Section */
                mCategoryList.add(new Category(0, prefectureObject.getString(1),
                        null, null, null, null));
            } else {
                //noinspection ConstantConditions,ConstantConditions
                mCategoryList.add(new Category(1, name, prefectureObject.getString(1),
                        prefectureObject.getString(2), prefectureObject.getString(3),
                        prefectureObject.getString(4)));
            }
            prefectureObject.recycle();
        }
        prefectureArray.recycle();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCategoryList.get(position).getType() == CategoryListAdapter.TYPE_SECTION) {
            return;
        }
        Intent intent = new Intent(this, PhotoListActivity.class);
        intent.putExtra(PhotoListActivity.TAG_ID, mCategoryList.get(position).getId());
        intent.putExtra(PhotoListActivity.TAG_NAME, mCategoryList.get(position).getName());
        intent.putExtra(PhotoListActivity.TAG_URL, mCategoryList.get(position).getUrl());
        intent.putExtra(PhotoListActivity.TAG_CAPITAL, mCategoryList.get(position).getCapital());
        intent.putExtra(PhotoListActivity.TAG_WEBSITE, mCategoryList.get(position).getWebsite());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        setNextTransition();
    }

    private void showDisclaimer(boolean firstTime) {
        DisclaimerDialog dialog = DisclaimerDialog.newInstance(firstTime);
        dialog.show(getSupportFragmentManager(), "disclaimer");
    }

    @Override
    public void onUnderstand() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(PhotoExplorerApplication.TAG_DISCLAIMER, true);
        editor.apply();
    }

    @Override
    public void onCancelled() {
        finish();
    }
}
