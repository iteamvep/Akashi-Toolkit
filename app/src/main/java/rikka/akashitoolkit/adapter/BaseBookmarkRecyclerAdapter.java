package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.BaseDataModel;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/5/15.
 */
public abstract class BaseBookmarkRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends BaseRecyclerAdapter<VH> {
    private boolean mBookmarked;

    private Toast mToast;

    public boolean requireBookmarked() {
        return mBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        mBookmarked = bookmarked;
    }

    public BaseBookmarkRecyclerAdapter(boolean bookmarked) {
        mBookmarked = bookmarked;
    }

    @Override
    public void onDataListRebuilt(List data) {
        super.onDataListRebuilt(data);

        if (data.size() == 0 && requireBookmarked()) {
            BusProvider.instance().post(new BookmarkAction.NoItem());
        }
    }

    public void showToast(Context context, boolean bookmarked) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(context, bookmarked ? context.getString(R.string.bookmark_add) : context.getString(R.string.bookmark_remove), Toast.LENGTH_SHORT);
        mToast.show();
    }
}
