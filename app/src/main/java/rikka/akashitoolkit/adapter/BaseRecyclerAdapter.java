package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rikka.akashitoolkit.viewholder.IBindViewHolder;


/**
 * Created by Rikka on 2016/4/13.
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    private List<T> mData;
    private List<Long> mId;
    private List<Integer> mType;

    public BaseRecyclerAdapter() {
        mData = new ArrayList<>();
        mId = new ArrayList<>();
        mType = new ArrayList<>();
    }

    public void rebuildDataList() {

    }

    public void onDataListRebuilt(List data) {

    }

    final public void setItemList(T[] array) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        setItemList(list);
    }

    final public void setItemList(List<T> list) {
        mData = list;

        mId.clear();
        mType.clear();

        for (int i = 0; i < list.size(); i++) {
            mId.add((long) i);
            mType.add(0);
        }

        notifyDataSetChanged();
    }

    /**
     * 得到数据 List
     *
     * @return List
     */
    final public List<T> getItemList() {
        return mData;
    }

    final public T getItem(int position) {
        if (position < 0 || position >= mData.size()) {
            return null;
        }

        return mData.get(position);
    }

    final public void addItem(T data) {
        addItem(0, data);
    }

    final public void addItem(int type, T data) {
        addItem(RecyclerView.NO_ID, type, data);
    }

    final public void addItem(long id, int type, T data) {
        mData.add(data);
        mId.add(id);
        mType.add(type);
    }

    final public void addItem(long id, int type, T data, int position) {
        mData.add(position, data);
        mId.add(position, id);
        mType.add(position, type);
    }

    final public void removeItem(int position) {
        mData.remove(position);
        mId.remove(position);
        mType.remove(position);
    }

    final public void clearItemList() {
        mData.clear();
        mId.clear();
        mType.clear();
    }

    @Override
    public long getItemId(int position) {
        return mId.size() == 0 ? 0 : mId.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mType.size() == 0 ? 0 : mType.get(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (holder instanceof IBindViewHolder) {
            ((IBindViewHolder) holder).bind(getItem(position), position);
        }
    }
}
