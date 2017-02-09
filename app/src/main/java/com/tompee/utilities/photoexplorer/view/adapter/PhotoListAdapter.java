package com.tompee.utilities.photoexplorer.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tompee.utilities.photoexplorer.R;
import com.tompee.utilities.photoexplorer.model.Photo;

import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private final List<Photo> mPhotos;
    private final Context mContext;

    public PhotoListAdapter(Context context, List<Photo> photos) {
        mPhotos = photos;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.list_photo, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);
        ImageView imageView = holder.mImageView;
        Picasso.with(mContext).load(photo.getThumbnailUrl()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final CardView mCardview;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardview = (CardView) itemView.findViewById(R.id.cardview_background);
            mImageView = (ImageView) itemView.findViewById(R.id.imageview_photo);
        }
    }
}
