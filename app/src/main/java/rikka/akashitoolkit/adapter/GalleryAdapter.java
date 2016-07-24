package rikka.akashitoolkit.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/7/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    @LayoutRes
    private int mResId;
    private List<String> mUrls;
    private List<String> mNames;
    private Drawable mPlaceholder;

    private int mImagePadding;
    private Drawable mItemBackground;

    public GalleryAdapter() {
        this(R.layout.item_illustrations);

        mUrls = new ArrayList<>();
        mImagePadding = 0;
    }

    public GalleryAdapter(@LayoutRes int resId) {
        mResId = resId;
        mPlaceholder = new ColorDrawable(Color.parseColor("#00000000"));

        setHasStableIds(true);
    }

    public void setItemBackgroundColor(int color) {
        mItemBackground = new ColorDrawable(color);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setUrls(List<String> urls) {
        mUrls = urls;
    }

    public List<String> getNames() {
        return mNames;
    }

    public void setNames(List<String> names) {
        mNames = names;
    }

    public int getImagePadding() {
        return mImagePadding;
    }

    public void setImagePadding(int imagePadding) {
        mImagePadding = imagePadding;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false);
        return new ViewHolder(itemView);
    }

    public void onItemClicked(View v, List<String> data, int position) {

    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(mUrls.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .placeholder(mPlaceholder)
                .into(holder.mImageView);

        if (mItemBackground != null) {
            holder.itemView.setBackgroundDrawable(mItemBackground);
        }

        holder.mImageView.setPadding(mImagePadding, mImagePadding, mImagePadding, mImagePadding);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(v, mUrls, holder.getAdapterPosition());
            }
        });

        if (mNames != null) {
            holder.mTitle.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    holder.mTitle.getLayoutParams().width = holder.itemView.getWidth();

                    v.removeOnLayoutChangeListener(this);
                }
            });
            holder.mTitle.setVisibility(View.VISIBLE);
            holder.mTitle.setText(mNames.get(position));
        } else {
            holder.mTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

        Glide.clear(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mUrls == null ? 0 : mUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(android.R.id.icon);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
        }
    }
}
