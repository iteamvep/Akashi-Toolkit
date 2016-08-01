package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Rikka on 2016/7/28.
 */
public interface DividerItemTouchHelperAdapter extends ItemTouchHelperAdapter {
    void onItemSelected(int position, int actionState);
}
