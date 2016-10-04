package rikka.akashitoolkit.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import rikka.akashitoolkit.viewholder.IBindViewHolder;
import rikka.akashitoolkit.viewholder.SimpleTitleViewHolder;

/**
 * Created by Rikka on 2016/7/25.
 */
public class SimpleAdapter<T> extends BaseRecyclerAdapter<RecyclerView.ViewHolder, T> {

    public interface Listener {
        void OnClick(int position);
    }

    private Listener mListener;

    @LayoutRes
    private int mResId;

    /**
     * @param resId LayoutRest
     */
    public SimpleAdapter(@LayoutRes int resId) {
        mResId = resId;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IBindViewHolder) {
            ((IBindViewHolder) holder).bind(getItem(position), position);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(holder, holder.getAdapterPosition());

            }
        });
    }

    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        if (mListener != null) {
            mListener.OnClick(holder.getAdapterPosition());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false);
        return createViewHolder(view, viewType);
    }

    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new SimpleTitleViewHolder(view);
    }

    public void setOnItemClickListener(Listener listener) {
        mListener = listener;
    }
}
