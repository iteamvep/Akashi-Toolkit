package rikka.akashitoolkit.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ship.TitleContentViewHolder;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/9/21.
 * <p>
 * 舰娘详情和装备详情的属性
 */

public abstract class AttributeViewHolder extends RecyclerView.ViewHolder {

    public static final int SPAN_COUNT = 2;

    public RecyclerView mRecyclerView;
    public Adapter mAdapter;

    public AttributeViewHolder(View itemView) {
        super(itemView);

        // really need it ?
        setIsRecyclable(false);

        mRecyclerView = (RecyclerView) itemView.findViewById(android.R.id.content);

        mRecyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), SPAN_COUNT));
        mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new Adapter(itemView.getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int PADDING = Utils.dpToPx(16);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) % SPAN_COUNT == 0) {
                    outRect.right = PADDING;
                } else {
                    outRect.left = PADDING;
                }
            }
        });
    }

    public static class Adapter extends RecyclerView.Adapter<TitleContentViewHolder> {

        private List<String[]> mList;
        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
            mList = new ArrayList<>();
        }

        public void clear() {
            mList.clear();
        }

        @SuppressLint("DefaultLocale")
        public void add(@StringRes int title, int value) {
            mList.add(new String[]{mContext.getString(title), String.format("%d", value)});
        }

        public void add(@StringRes int title, String content) {
            mList.add(new String[]{mContext.getString(title), content});
        }

        public void add(String title, String content) {
            mList.add(new String[]{title, content});
        }

        @Override
        public TitleContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TitleContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attribute, parent, false));
        }

        @Override
        public void onBindViewHolder(TitleContentViewHolder holder, int position) {
            holder.mTitle.setText(mList.get(position)[0]);
            holder.mContent.setText(mList.get(position)[1]);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
