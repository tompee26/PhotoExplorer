package com.tompee.utilities.photoexplorer.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.model.Category;

import java.util.List;

import de.halfbit.pinnedsection.PinnedSectionListView;

public class CategoryListAdapter extends ArrayAdapter<Category> implements
        PinnedSectionListView.PinnedSectionListAdapter {
    public static final int TYPE_SECTION = 0;

    public CategoryListAdapter(Context context, int resource, List<Category> list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Category item = getItem(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.getType() == TYPE_SECTION) {
                view = inflater.inflate(R.layout.list_section, parent, false);
            } else {
                view = inflater.inflate(R.layout.list_category, parent, false);
            }
        }
        if (item.getType() == TYPE_SECTION) {
            TextView textView = (TextView) view.findViewById(R.id.textview_section_name);
            textView.setText(item.getName());
        } else {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview_category_background);
            Picasso.with(getContext()).load(item.getUrl()).into(imageView);
            TextView textView = (TextView) view.findViewById(R.id.textview_category_name);
            textView.setText(item.getName());
        }
        return view;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TYPE_SECTION;
    }

    @Override public int getViewTypeCount() {
        return 2;
    }

    @Override public int getItemViewType(int position) {
        return getItem(position).getType();
    }
}
