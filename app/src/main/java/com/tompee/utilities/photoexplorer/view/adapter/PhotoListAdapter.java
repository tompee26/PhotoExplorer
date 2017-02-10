package com.tompee.utilities.photoexplorer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Photo photo = mPhotos.get(position);
        final ImageView imageView = holder.getImageView();
        Picasso.with(mContext).load(photo.getThumbnailUrl()).into(imageView);
        if (!holder.isDimensionSet()) {
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    double ratio = (double) photo.getWidth() / (double) imageView.getWidth();
                    int height = (int) (photo.getHeight() / ratio);
                    imageView.setLayoutParams(new FrameLayout.LayoutParams(imageView.getWidth(), height));
                    holder.setIsDimensionSet(true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;
        private boolean mIsDimensionSet;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageview_photo);
            mIsDimensionSet = false;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public boolean isDimensionSet() {
            return mIsDimensionSet;
        }

        public void setIsDimensionSet(boolean isDimensionSet) {
            mIsDimensionSet = isDimensionSet;
        }
    }
}
