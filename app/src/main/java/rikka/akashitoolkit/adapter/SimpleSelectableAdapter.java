package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Rikka on 2016/8/6.
 */
public class SimpleSelectableAdapter<T> extends SimpleAdapter<T> {

    private boolean mSingleSelection;
    private long mSelection;

    /**
     * @param resId  布局文件id
     * @param single 是否为单选
     */
    public SimpleSelectableAdapter(int resId, boolean single) {
        super(resId);

        mSingleSelection = single;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        boolean selected;
        if (!mSingleSelection) {
            selected = (mSelection & 1 << position) > 0;

        } else {
            selected = position == mSelection;
        }

        holder.itemView.setSelected(selected);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        super.onItemClick(holder, position);

        if (!mSingleSelection) {
            if ((mSelection & 1 << position) > 0) {
                mSelection &= ~1 << position;
            } else {
                mSelection |= 1 << position;
            }
            notifyItemChanged(position);
        } else {
            mSelection = position;
            notifyDataSetChanged();
        }
    }

    public void resetSelected() {
        mSelection = 0;
        notifyDataSetChanged();
    }

    /**
     * 返回当前已选择的
     *
     * @return 单选时返回已选择项目的位置, 多选时返回
     */
    public long getSelection() {
        return mSelection;
    }

    public void setSelection(long selection) {
        mSelection = selection;
        notifyDataSetChanged();
    }
}
