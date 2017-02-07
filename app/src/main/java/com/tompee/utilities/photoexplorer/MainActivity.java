package com.tompee.utilities.photoexplorer;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.photoexplorer.model.Category;
import com.tompee.utilities.photoexplorer.view.adapter.CategoryListAdapter;
import com.tompee.utilities.photoexplorer.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, false);
        setToolbarTitle(R.string.app_name);

        ListView listView = (ListView) findViewById(R.id.listview_category);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Tokyo", "http://c1.staticflickr.com/8/7022/6788055657_c450e39855_b.jpg"));
        categoryList.add(new Category("Sapporo", "http://c2.staticflickr.com/2/1687/26375975211_2b80aa93c2_b.jpg"));
        categoryList.add(new Category("Nagoya", "http://c1.staticflickr.com/3/2625/13011103584_c589a35c7f_b.jpg"));
        CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.list_category,
                categoryList);
        listView.setAdapter(adapter);
    }

    private void setToolbarTitle(int resId) {
        TextView toolbar = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setText(resId);
    }
}
