package rikka.akashitoolkit.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rikka on 2016/7/25.
 */
public class SimpleRecyclerViewAdapter<T> extends BaseRecyclerAdapter<SimpleRecyclerViewAdapter.ViewHolder, T> {

    public interface Listener {
        void OnClick(int position);
    }

    private Listener mListener;

    @LayoutRes
    int mResId;

    public SimpleRecyclerViewAdapter(int resId) {
        mResId = resId;
    }

    @Override
    public void onBindViewHolder(final SimpleRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mTitle.setText((String) getItem(position));

        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public SimpleRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false));
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setDataList(List<T> list) {
        clearItemList();

        for (int i = 0; i < list.size(); i++) {
            addItem(i, 0, list.get(i));
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
        }
    }
}
