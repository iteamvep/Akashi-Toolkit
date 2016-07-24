package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rikka on 2016/4/13.
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

    final public Object getItemData(int position) {
        return mData.get(position).data;
    }

    final public void addItem(Data data) {
        mData.add(data);
    }

    final public void addItem(Data data, int position) {
        mData.add(position, data);
    }

    final public void addItem(long id, int type, Object data) {
        mData.add(new Data(data, type, id));
    }

    final public void addItem(long id, int type, Object data, int position) {
        mData.add(position, new Data(data, type, id));
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
