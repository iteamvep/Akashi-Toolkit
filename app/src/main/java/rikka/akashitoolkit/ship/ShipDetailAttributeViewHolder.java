package rikka.akashitoolkit.ship;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.AttributeEntity;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.utils.KCStringFormatter;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/17.
 */
public class ShipDetailAttributeViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<Ship> {

    public static final int SPAN_COUNT = 2;

    public RecyclerView mRecyclerView;

    public ShipDetailAttributeViewHolder(View itemView) {
        super(itemView);

        // really need it ?
        setIsRecyclable(false);

        mRecyclerView = (RecyclerView) itemView.findViewById(android.R.id.content);

        mRecyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), SPAN_COUNT));
        mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(new Adapter(itemView.getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int PADDING = Utils.dpToPx(16);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) % SPAN_COUNT == 0) {
                    outRect.right = PADDING;
                } else {
                    outRect.left = PADDING;
                }
            }
        });
    }

    @Override
    public void bind(Ship data, int position) {
        Context context = itemView.getContext();
        Adapter adapter = (Adapter) mRecyclerView.getAdapter();
        boolean is_enemy = data.getId() >= 500;

        AttributeEntity attr = data.getAttr();
        AttributeEntity attr_max = attr.plus(data.getAttrMax()).plus(data.getAttr99());

        if (!is_enemy) {
            adapter.add(R.string.attr_hp, attr.getHP(), attr_max.getHP());

            adapter.add(R.string.attr_firepower, attr.getFirepower(), attr_max.getFirepower());
            adapter.add(R.string.attr_aa, attr.getAA(), attr_max.getAA());
            adapter.add(R.string.attr_torpedo, attr.getTorpedo(), attr_max.getTorpedo());
            adapter.add(R.string.attr_armor, attr.getArmor(), attr_max.getArmor());

            adapter.add(R.string.attr_asw, attr.getASW(), attr_max.getASW());
            adapter.add(R.string.attr_evasion, attr.getEvasion(), attr_max.getEvasion());
            adapter.add(R.string.attr_los, attr.getLOS(), attr_max.getLOS());

            adapter.add(R.string.attr_speed, KCStringFormatter.getSpeed(context, attr.getSpeed()));
            adapter.add(R.string.attr_range, KCStringFormatter.getRange(context, attr.getRange()));

            adapter.add(R.string.attr_luck, attr.getLuck(), attr_max.getLuck());
        } else {
            adapter.add(R.string.attr_hp, attr.getHP());
            adapter.add(R.string.attr_firepower, attr.getFirepower());
            adapter.add(R.string.attr_aa, attr.getAA());
            adapter.add(R.string.attr_torpedo, attr.getTorpedo());
            adapter.add(R.string.attr_armor, attr.getArmor());
            adapter.add(R.string.attr_speed, KCStringFormatter.getSpeed(context, attr.getSpeed()));
            adapter.add(R.string.attr_range, KCStringFormatter.getRange(context, attr.getRange()));
        }
    }

    private static class Adapter extends RecyclerView.Adapter<TitleContentViewHolder> {

        private List<String[]> mList;
        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
            mList = new ArrayList<>();
        }

        public void clear() {
            mList.clear();
        }

        @SuppressLint("DefaultLocale")
        public void add(@StringRes int title, int value) {
            mList.add(new String[]{mContext.getString(title), String.format("%d", value)});
        }

        @SuppressLint("DefaultLocale")
        public void add(@StringRes int title, int min, int max) {
            mList.add(new String[]{mContext.getString(title), String.format("%d → %d", min, max)});
        }

        public void add(@StringRes int title, String content) {
            mList.add(new String[]{mContext.getString(title), content});
        }

        public void add(String title, String content) {
            mList.add(new String[]{title, content});
        }

        @Override
        public TitleContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TitleContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attribute, parent, false));
        }

        @Override
        public void onBindViewHolder(TitleContentViewHolder holder, int position) {
            holder.mTitle.setText(mList.get(position)[0]);
            holder.mContent.setText(mList.get(position)[1]);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
