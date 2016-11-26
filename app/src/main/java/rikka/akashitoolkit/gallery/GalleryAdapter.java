package rikka.akashitoolkit.gallery;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.Utils;

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

    private int mImageWidth;
    private int mImageHeight;
    private ImageView.ScaleType mImageScaleType;

    public interface OnItemClickListener {
        void onItemClicked(View v, int position);
    }

    private OnItemClickListener mOnClickListener;

    public GalleryAdapter() {
        this(R.layout.item_illustrations);

        mUrls = new ArrayList<>();
        mImagePadding = 0;
    }

    public GalleryAdapter(@LayoutRes int resId) {
        mResId = resId;
        mPlaceholder = new ColorDrawable(Color.parseColor("#00000000"));

        setHasStableIds(true);

        mImageWidth = mImageHeight = -1;
        mImageScaleType = null;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnClickListener = listener;
    }

    public void setItemBackgroundColor(@ColorInt int color) {
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

    public void setImageWidth(int imageWidth) {
        mImageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        mImageHeight = imageHeight;
    }

    public void setImageScaleType(ImageView.ScaleType imageScaleType) {
        mImageScaleType = imageScaleType;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false);
        return new ViewHolder(itemView);
    }

    // TODO remove this
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
                onItemClicked(v, mUrls, holder.getLayoutPosition());
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClicked(v, holder.getLayoutPosition());
                }
            }
        });

        if (mImageWidth != -1 && mImageHeight != -1) {
            holder.mImageView.setLayoutParams(new LinearLayout.LayoutParams(mImageWidth, mImageHeight));
        }

        if (mImageScaleType != null) {
            holder.mImageView.setScaleType(mImageScaleType);
        }

        if (holder.mTitle != null) {
            if (mNames != null) {
                holder.mTitle.setVisibility(View.VISIBLE);
                holder.mTitle.setText(mNames.get(position));
            } else {
                holder.mTitle.setVisibility(View.GONE);
            }
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
        @Nullable
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(android.R.id.icon);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
        }
    }
}
