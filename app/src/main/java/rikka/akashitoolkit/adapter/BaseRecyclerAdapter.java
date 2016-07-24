package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;

/**
 * Created by Rikka on 2016/4/13.
 *
 * // TODO 让旧的 Adapter 们用这里新加的东西
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<Data> mData;

    public BaseRecyclerAdapter() {
        mData = new ArrayList<>();
    }

    public abstract void rebuildDataList();

    public void onDataListRebuilt(List data) {

    }

    final public List<Data> getItemList() {
        return mData;
    }

    final public Object getItem(int position) {
        return mData.get(position).data;
    }

    final public void addItem(long id, int type, Object data) {
        mData.add(new Data(data, type, id));
    }

    final public void clearItemList() {
        mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class Data {
        protected Object data;
        protected int type;
        protected long id;

        public Data(Object data, int type, long id) {
            this.data = data;
            this.type = type;
            this.id = id;
        }
    }
}
