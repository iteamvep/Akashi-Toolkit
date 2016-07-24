package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Seasonal;
import rikka.akashitoolkit.ui.GalleryActivity;
import rikka.akashitoolkit.ui.ImagesActivity;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/7/23.
 */
public class SeasonalAdapter extends BaseRecyclerAdapter {

    private static final String TAG = "SeasonalAdapter";

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_GALLERY = 1;
    private static final int TYPE_CONTENT = 2;

    public SeasonalAdapter() {
    }

    public void parseData(List<Seasonal> data) {
        clearItemList();

        for (Seasonal entry : data) {

            switch (entry.getType()) {
                case TYPE_GALLERY:
                    List<String> urls = new ArrayList<>();
                    for (String url : entry.getGallery().getUrls()) {
                        if (!url.startsWith("http")) {
                            urls.add(Utils.getKCWikiFileUrl(url));
                        }
                    }
                    entry.getGallery().getUrls().clear();
                    entry.getGallery().getUrls().addAll(urls);

            }

            addItem(RecyclerView.NO_ID, entry.getType(), entry);
        }

        notifyDataSetChanged();
    }

    @Override
    public void rebuildDataList() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_title, parent, false));
            case TYPE_GALLERY:
                return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_gallery, parent, false));
            case TYPE_CONTENT:
                return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_seansonal_text, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_TITLE:
                bindViewHolder((TitleViewHolder) holder, position);
                break;
            case TYPE_GALLERY:
                bindViewHolder((GalleryViewHolder) holder, position);
                break;
            case TYPE_CONTENT:
                bindViewHolder((ContentViewHolder) holder, position);
                break;
        }
    }

    private void bindViewHolder(TitleViewHolder holder, int position) {
        Seasonal data = (Seasonal) getItem(position);
        holder.mTitle.setText(data.getTitle());
        holder.mSummary.setText(data.getSummary());
    }

    private void bindViewHolder(final GalleryViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        Seasonal data = (Seasonal) getItem(position);
        final String title = data.getTitle();
        final String summary = data.getSummary();
        final String content = data.getContent();
        final List<String> urls = data.getGallery().getUrls();
        final List<String> names = data.getGallery().getNames();

        holder.mTitle.setText(title);
        holder.mSummary.setText(summary);
        holder.mSummary.setVisibility(TextUtils.isEmpty(summary) ? View.GONE : View.VISIBLE);
        holder.mContent.setText(content);
        holder.mContent.setVisibility(!TextUtils.isEmpty(content) ? View.VISIBLE : View.GONE);

        holder.mButton.setText(R.string.view_all);
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                GalleryActivity.start(context, urls, names, title);
            }
        });
        holder.mButton.setVisibility(TextUtils.isEmpty(content) ? View.VISIBLE : View.GONE);

        if (urls.size() > GalleryViewHolder.MAX_IMAGE) {
            holder.mButton.setText(String.format(context.getString(R.string.view_all_format),
                    urls.size() - GalleryViewHolder.MAX_IMAGE));
        }
        ((GalleryAdapter) holder.mRecyclerView.getAdapter()).setUrls(urls);

        Log.d(TAG, title + " gallery size " + urls.size() + " item width " + holder.mItemSize);

        holder.mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                holder.mItemSize = (v.getWidth() - (Utils.dpToPx(2) * (GalleryViewHolder.SPAN_COUNT - 1))) / GalleryViewHolder.SPAN_COUNT;
                holder.mRecyclerView.getAdapter().notifyDataSetChanged();

                v.removeOnLayoutChangeListener(this);
            }
        });

        // some magic (((
        holder.mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }, 300);
    }

    private void bindViewHolder(ContentViewHolder holder, int position) {
        Seasonal data = (Seasonal) getItem(position);
        holder.mTitle.setText(data.getTitle());
        holder.mContent.setText(data.getContent());
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mSummary;

        public TitleViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mSummary = (TextView) itemView.findViewById(android.R.id.summary);
        }
    }

    private static class GalleryViewHolder extends RecyclerView.ViewHolder {

        private static final int MAX_IMAGE = 12;
        private static final int SPAN_COUNT = 4;

        public TextView mTitle;
        public TextView mSummary;
        public TextView mContent;
        public RecyclerView mRecyclerView;
        public Button mButton;

        public int mItemSize;

        public GalleryViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mSummary = (TextView) itemView.findViewById(android.R.id.summary);
            mContent = (TextView) itemView.findViewById(android.R.id.content);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.content_container);
            mButton = (Button) itemView.findViewById(android.R.id.button1);

            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int position = parent.getChildAdapterPosition(view) + 1;

                    outRect.set(0, Utils.dpToPx(2), Utils.dpToPx(2), 0);

                    if (position % SPAN_COUNT == 0) {
                        outRect.right = 0;
                    }

                    if (position <= SPAN_COUNT) {
                        outRect.top = 0;
                    }
                }
            });

            mRecyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), SPAN_COUNT));
            mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);
            mRecyclerView.setNestedScrollingEnabled(false);

            mRecyclerView.setAdapter(new GalleryAdapter(R.layout.item_gallery_image) {
                @Override
                public void onItemClicked(View v, List<String> data, int position) {
                    ImagesActivity.start(v.getContext(), data, position, null);
                }

                @Override
                public void onBindViewHolder(ViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    holder.mImageView.setLayoutParams(new FrameLayout.LayoutParams(mItemSize, mItemSize));
                }

                @Override
                public int getItemCount() {
                    return super.getItemCount() > MAX_IMAGE ? MAX_IMAGE : super.getItemCount();
                }
            });
        }
    }

    private static class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mContent;

        public ContentViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mContent = (TextView) itemView.findViewById(android.R.id.content);
        }
    }
}
