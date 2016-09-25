package rikka.akashitoolkit.detail;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.List;

import rikka.akashitoolkit.gallery.GalleryAdapter;
import rikka.akashitoolkit.gallery.ImagesActivity;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/9/21.
 * 舰娘详情和装备详情的立绘
 */

public class IllustrationViewHolder extends RecyclerView.ViewHolder {

    public RecyclerView mRecyclerView;
    public GalleryAdapter mAdapter;

    public IllustrationViewHolder(View itemView) {
        super(itemView);

        // really need it ?
        setIsRecyclable(false);

        mRecyclerView = (RecyclerView) itemView.findViewById(android.R.id.content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                if (parent.getChildLayoutPosition(view) < parent.getAdapter().getItemCount() - 1) {
                    outRect.right = Utils.dpToPx(8);
                }
            }
        });
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(mRecyclerView);
    }

    private class Adapter extends GalleryAdapter {

        @Override
        public void onItemClicked(View v, List<String> data, int position) {
            ImagesActivity.start(v.getContext(), data, position, null);
        }
    }
}
