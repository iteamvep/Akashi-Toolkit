package rikka.akashitoolkit.viewholder;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Rikka on 2016/7/29.
 */
public interface ItemTouchHelperViewHolder {
    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

    int getDragFlags();

    int getSwipeFlags();
}
