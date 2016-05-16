package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;

/**
 * Created by Rikka on 2016/5/15.
 */
public abstract class BaseBookmarkRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends BaseRecyclerAdapter<VH> {
    private boolean mBookmarked;

    public boolean requireBookmarked() {
        return mBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        mBookmarked = bookmarked;
    }

    @Override
    public void onDataListRebuilt(List data) {
        super.onDataListRebuilt(data);

        if (data.size() == 0 && requireBookmarked()) {
            BusProvider.instance().post(new BookmarkAction.NoItem());
        }
    }
}
