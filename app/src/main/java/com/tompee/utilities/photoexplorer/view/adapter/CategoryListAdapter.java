package com.tompee.utilities.photoexplorer.view.adapter;

import android.content.Context;
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

public class CategoryListAdapter extends ArrayAdapter<Category> {

    public CategoryListAdapter(Context context, int resource, List<Category> list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_category, parent, false);
        }
        Category item = getItem(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.
                imageview_category_background);
        Picasso.with(getContext()).load(item.getUrl()).into(imageView);
        TextView textView = (TextView) view.findViewById(R.id.textview_category_name);
        textView.setText(item.getName());
        return view;
    }
}
