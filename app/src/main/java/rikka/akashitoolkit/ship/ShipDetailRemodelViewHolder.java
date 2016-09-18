package rikka.akashitoolkit.ship;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/17.
 */
public class ShipDetailRemodelViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<Ship> {

    public static final int SPAN_COUNT = 2;

    public RecyclerView mRecyclerView;
    public Adapter mAdapter;

    private List<Adapter.Data> mList;
    private List<Integer> mSpanSizeList;

    public interface OnItemClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v      The view that was clicked.
         * @param shipId Ship id
         */
        void onClick(View v, int shipId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    public ShipDetailRemodelViewHolder(View itemView) {
        super(itemView);

        // really need it ?
        setIsRecyclable(false);

        mRecyclerView = (RecyclerView) itemView.findViewById(android.R.id.content);

        GridLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mSpanSizeList.get(position);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPadding(0, 0, 0, 0);

        mList = new ArrayList<>();
        mSpanSizeList = new ArrayList<>();

        mAdapter.setOnRequestSpanSizeListener(new Adapter.OnRequestSpanSizeListener() {
            @Override
            public void onRequestSpanSize(int position) {
                mSpanSizeList.set(position, 2);
                mRecyclerView.requestLayout();
            }
        });
    }

    @Override
    public void bind(Ship data, int position) {
        mList.clear();
        mSpanSizeList.clear();

        Ship cur = data;
        while (cur.getRemodel().getFromId() != 0) {
            cur = ShipList.findItemById(cur.getRemodel().getFromId());
        }

        mList.add(new Adapter.Data(cur.getId(), cur, 0, false, false, false));
        mSpanSizeList.add(1);

        cur = ShipList.findItemById(cur.getRemodel().getToId());

        while (cur != null) {
            Ship prev = ShipList.findItemById(cur.getRemodel().getFromId());
            if (prev != null) {
                Ship.RemodelEntity remodel = prev.getRemodel();
                boolean can_back = remodel.getFromId() != 0 && remodel.getFromId() == remodel.getToId();
                mList.add(new Adapter.Data(cur.getId(), cur, remodel.getLevel(), can_back, remodel.isRequireBlueprint(), false));
                mSpanSizeList.add(1);
            }

            if (cur.getRemodel().getToId() == 0 ||
                    cur.getRemodel().getFromId() == cur.getRemodel().getToId()) {
                break;
            }

            cur = ShipList.findItemById(cur.getRemodel().getToId());
        }

        mAdapter.setCurrentShipId(data.getId());
        mAdapter.setList(mList);
    }

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private static class Data {
            public int id;
            public Ship ship;
            public int level;
            public boolean can_back;
            public boolean blueprint;
            public boolean deck;

            public Data(int id, Ship ship, int level, boolean can_back, boolean blueprint, boolean deck) {
                this.id = id;
                this.ship = ship;
                this.level = level;
                this.can_back = can_back;
                this.blueprint = blueprint;
                this.deck = deck;
            }
        }

        public interface OnRequestSpanSizeListener {
            void onRequestSpanSize(int position);
        }

        private OnRequestSpanSizeListener mOnRequestSpanSizeListener;

        public void setOnRequestSpanSizeListener(OnRequestSpanSizeListener listener) {
            mOnRequestSpanSizeListener = listener;
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        private List<Data> mList;
        private int mCurrent;

        public Adapter() {
            mList = new ArrayList<>();
        }

        public void setCurrentShipId(int current) {
            mCurrent = current;
        }

        public void setList(List<Data> list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_item_remodel, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Data data = mList.get(position);

            if (data.id == mCurrent) {
                holder.mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }

            StringBuilder sb = new StringBuilder();
            sb.append(data.ship.getName().get());

            if (data.level != 0) {
                sb.append(" (").append(data.level);
                if (data.blueprint) {
                    sb.append(" + ").append(holder.itemView.getContext().getString(R.string.blueprint));
                }
                sb.append(")");
            }
            holder.mTitle.setText(sb.toString());

            holder.mIcon.setVisibility((position == getItemCount() - 1) ? View.GONE : View.VISIBLE);

            holder.mIcon.setImageResource(data.can_back ? R.drawable.ic_compare_arrows_24dp : R.drawable.ic_arrow_forward_24dp);

            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    TextView tv = holder.mTitle;
                    Rect bounds = new Rect();
                    Paint textPaint = tv.getPaint();
                    textPaint.getTextBounds(tv.getText().toString(), 0, tv.getText().length(), bounds);
                    if (bounds.width() > tv.getWidth()) {
                        if (mOnRequestSpanSizeListener != null) {
                            mOnRequestSpanSizeListener.onRequestSpanSize(holder.getAdapterPosition());
                        }
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(v, data.id);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
        }
    }
}
