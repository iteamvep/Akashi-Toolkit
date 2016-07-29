package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rikka on 2016/4/13.
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {
    private List<Data<T>> mData;

    public BaseRecyclerAdapter() {
        mData = new ArrayList<>();
    }

    public void rebuildDataList() {

    }

    public void onDataListRebuilt(List data) {

    }

    final public void setItemList(List<T> list) {
        clearItemList();

        for (int i = 0; i < list.size(); i++) {
            addItem(i, 0, list.get(i));
        }

        notifyDataSetChanged();
    }

    final public List<Data<T>> getItemList() {
        return mData;
    }

    final public T getItem(int position) {
        return mData.get(position).data;
    }

    final public void addItem(Data<T> data) {
        mData.add(data);
    }

    final public void addItem(Data<T> data, int position) {
        mData.add(position, data);
    }

    final public void addItem(long id, int type, T data) {
        mData.add(new Data<>(data, type, id));
    }

    final public void addItem(long id, int type, T data, int position) {
        mData.add(position, new Data<>(data, type, id));
    }

    final public void removeItem(int position) {
        mData.remove(position);
    }

    final public void clearItemList() {
        mData.clear();
    }

    @Override
    public long getItemId(int position) {
        return mData.size() == 0 ? 0 : mData.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.size() == 0 ? 0 : mData.get(position).type;
    }

    @Override
    final public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class Data<T> {
        protected T data;
        protected int type;
        protected long id;

        public Data(T data, int type, long id) {
            this.data = data;
            this.type = type;
            this.id = id;
        }
    }
}
