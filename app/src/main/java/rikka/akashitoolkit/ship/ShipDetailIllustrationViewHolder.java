package rikka.akashitoolkit.ship;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.gallery.GalleryAdapter;
import rikka.akashitoolkit.gallery.ImagesActivity;
import rikka.akashitoolkit.model.ExtraIllustration;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ExtraIllustrationList;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/18.
 */
public class ShipDetailIllustrationViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<Ship> {

    public RecyclerView mRecyclerView;
    public GalleryAdapter mAdapter;
    private boolean mIsEnemy;

    public ShipDetailIllustrationViewHolder(View itemView) {
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

    @Override
    public void bind(Ship data, int position) {
        mIsEnemy = data.getId() >= 500;

        mAdapter.setUrls(getIllustrationUrls(data));
    }

    private List<String> getIllustrationUrls(Ship data) {
        List<String> list = new ArrayList<>();

        if (data.getWikiId().equals("030a")
                || data.getWikiId().equals("026a")
                || data.getWikiId().equals("027a")
                || data.getWikiId().equals("042a")
                || data.getWikiId().equals("065a")
                || data.getWikiId().equals("094a")
                || data.getWikiId().equals("183a")) {
            list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sIllust.png", data.getWikiId())));
            list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sDmgIllust.png", data.getWikiId())));

        } else {
            if (!mIsEnemy) {
                list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sIllust.png", data.getWikiId().replace("a", ""))));
                list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sDmgIllust.png", data.getWikiId().replace("a", ""))));
            } else {
                list.add(Utils.getKCWikiFileUrl(String.format("ShinkaiSeikan%s.png", data.getWikiId())));
            }
        }

        ExtraIllustration extraIllustration = ExtraIllustrationList.findItemById(itemView.getContext(), data.getWikiId());
        if (extraIllustration != null) {
            for (String name :
                    extraIllustration.getImage()) {
                list.add(Utils.getKCWikiFileUrl(name));
            }
        }

        return list;
    }

    private class Adapter extends GalleryAdapter {

        @Override
        public void onItemClicked(View v, List<String> data, int position) {
            ImagesActivity.start(v.getContext(), data, position, null);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            ImageView imageView = (ImageView) holder.itemView;

            if (mIsEnemy) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dpToPx(150), Utils.dpToPx(150)));
            } else {
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dpToPx(150), Utils.dpToPx(300)));
            }
        }
    }
}
