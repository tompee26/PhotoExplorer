package com.tompee.utilities.photoexplorer.controller.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tompee.utilities.photoexplorer.R;

public class ItemClickListener {
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                // ask the RecyclerView for the viewHolder of this view.
                // then use it to get the position for the adapter
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };
    private OnItemLongClickListener mOnItemLongClickListener;
    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                return mOnItemLongClickListener.onItemLongClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };
    private final RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            // every time a new child view is attached add click listeners to it
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    private ItemClickListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        // the ID must be declared in XML, used to avoid
        // replacing the ItemClickListener without removing
        // the old one from the RecyclerView
        mRecyclerView.setTag(R.id.item_click, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickListener addTo(RecyclerView view) {
        // if there's already an ItemClickListener attached
        // to this RecyclerView do not replace it, use it
        ItemClickListener support = (ItemClickListener) view.getTag(R.id.item_click);
        if (support == null) {
            support = new ItemClickListener(view);
        }
        return support;
    }

    public static ItemClickListener removeFrom(RecyclerView view) {
        ItemClickListener support = (ItemClickListener) view.getTag(R.id.item_click);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    public ItemClickListener setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public ItemClickListener setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(R.id.item_click, null);
    }

    public interface OnItemClickListener {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
    }
}
