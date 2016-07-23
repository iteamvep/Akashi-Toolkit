package rikka.akashitoolkit.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/7/15.
 */
abstract public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    @LayoutRes
    private int mResId;
    private List<String> mData;
    private Drawable mPlaceholder;

    public GalleryAdapter() {
        this(R.layout.item_illustrations);

        mData = new ArrayList<>();
    }

    public GalleryAdapter(@LayoutRes int resId) {
        mResId = resId;
        mPlaceholder = new ColorDrawable(Color.parseColor("#0d000000"));

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<String> data) {
        mData = data;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false);
        onCreateImageView((ImageView) itemView);
        return new ViewHolder(itemView);
    }

    public void onItemClicked(View v, List<String> data, int position) {

    }

    public void onCreateImageView(ImageView imageView) {

    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(mData.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .placeholder(mPlaceholder)
                .into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(v, mData, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

        Glide.clear(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView;
        }
    }
}
