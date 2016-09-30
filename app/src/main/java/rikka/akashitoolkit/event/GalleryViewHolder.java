package rikka.akashitoolkit.event;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.gallery.GalleryActivity;
import rikka.akashitoolkit.gallery.GalleryAdapter;
import rikka.akashitoolkit.gallery.ImagesActivity;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/8/12.
 */
public class GalleryViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<Event.Gallery> {

    private static final String TAG = "GalleryViewHolder";

    public static final int MAX_IMAGE = 12;
    public static final int SPAN_COUNT = 4;

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

                outRect.set(0, Utils.dpToPx(4), Utils.dpToPx(4), 0);

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

    @Override
    public void bind(Event.Gallery data, int position) {
        Context context = itemView.getContext();

        final String title = data.getTitle().get();
        final String summary = data.getSummary().get();
        final String content = data.getContent().get();
        final List<String> urls = data.getUrls();
        final List<String> names = data.getNames();

        mTitle.setText(title);
        mSummary.setText(summary);
        mSummary.setVisibility(TextUtils.isEmpty(summary) ? View.GONE : View.VISIBLE);
        mContent.setText(content);
        mContent.setVisibility(!TextUtils.isEmpty(content) ? View.VISIBLE : View.GONE);

        mButton.setText(R.string.view_all);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                /*List<String> list = new ArrayList<>();
                for (LocaleMultiLanguageEntry entry : names) {
                    list.add(entry.get());
                }*/
                GalleryActivity.start(context, urls, names/*list*/, title);
            }
        });
        mButton.setVisibility(TextUtils.isEmpty(content) ? View.VISIBLE : View.GONE);

        if (urls.size() > GalleryViewHolder.MAX_IMAGE) {
            mButton.setText(String.format(context.getString(R.string.view_all_format),
                    urls.size() - GalleryViewHolder.MAX_IMAGE));
        }
        ((GalleryAdapter) mRecyclerView.getAdapter()).setUrls(urls);

        Log.d(TAG, title + " gallery size " + urls.size() + " item width " + mItemSize);

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mItemSize = (v.getWidth() - (Utils.dpToPx(4) * (GalleryViewHolder.SPAN_COUNT - 1))) / GalleryViewHolder.SPAN_COUNT;
                mRecyclerView.getAdapter().notifyDataSetChanged();

                v.removeOnLayoutChangeListener(this);
            }
        });

        // some magic (((
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }, 300);
    }
}
