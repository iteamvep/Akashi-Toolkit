package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikka on 2016/12/11.
 */

abstract public class FilterAdapterHelper<T> {

    private RecyclerView.Adapter mAdapter;

    private List<T> mOriginalData;
    private List<T> mFilteredData;

    private String mKeyword;
    private boolean mIsSearching;
    private SparseIntArray mIntKeys;
    private SparseBooleanArray mBooleanKeys;

    public FilterAdapterHelper(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mIntKeys = new SparseIntArray();
        mBooleanKeys = new SparseBooleanArray();
    }

    public List<T> getOriginalData() {
        return mOriginalData;
    }

    public void setData(List<T> originalData) {
        mOriginalData = originalData;
        mFilteredData = filter(mOriginalData);
    }

    public List<T> getFilteredData() {
        return mFilteredData;
    }

    public void setKeyword(String keyword) {
        if (mKeyword != null && mKeyword.equals(keyword)) {
            return;
        }

        mKeyword = keyword;
        mFilteredData = filter(mOriginalData);
        mAdapter.notifyDataSetChanged();
        //mAdapter.requestResetData();
    }

    public boolean isSearching() {
        return mIsSearching;
    }

    public void setSearching(boolean searching) {
        mIsSearching = searching;
        mFilteredData = filter(mOriginalData);
        mAdapter.notifyDataSetChanged();
        //mAdapter.requestResetData();
    }

    public void addKey(int key, int value) {
        mIntKeys.put(key, value);
    }

    public void addKey(int key, boolean value) {
        mBooleanKeys.put(key, value);
    }

    public void setKey(int key, int value) {
        setKey(key, value, true);
    }

    public void setKey(int key, int value, boolean notify) {
        mIntKeys.put(key, value);
        mFilteredData = filter(mOriginalData);

        if (notify) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setKey(int key, boolean value) {
        setKey(key, value, true);
    }

    public void setKey(int key, boolean value, boolean notify) {
        mBooleanKeys.put(key, value);
        mFilteredData = filter(mOriginalData);

        if (notify) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<T> filter(List<T> list) {
        List<T> newList = new ArrayList<>();

        if (list == null) {
            return newList;
        }

        for (T obj : list) {
            boolean check = true;
            for (int i = 0; i < mIntKeys.size(); i++) {
                int key = mIntKeys.keyAt(i);
                if (!check(key, mIntKeys.get(key), obj)) {
                    check = false;
                    break;
                }
            }

            for (int i = 0; i < mBooleanKeys.size(); i++) {
                int key = mBooleanKeys.keyAt(i);
                if (!check(key, mBooleanKeys.get(key), obj)) {
                    check = false;
                    break;
                }
            }

            if (!check) {
                continue;
            }

            if (mIsSearching && !contains(mKeyword, obj)) {
                continue;
            }

            newList.add(obj);
        }
        return newList;
    }

    abstract public boolean contains(String key, T obj);

    public boolean check(int key, int value, T obj) {
        return true;
    }

    public boolean check(int key, boolean value, T obj) {
        return true;
    }
}
