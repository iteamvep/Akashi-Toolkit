package rikka.akashitoolkit.fleet_editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.SimpleAdapter;
import rikka.akashitoolkit.adapter.SimpleSelectableAdapter;
import rikka.akashitoolkit.model.Fleet;
import rikka.akashitoolkit.ui.widget.BottomSheetDialog;

/**
 * Created by Rikka on 2016/8/7.
 */
public class EquipAttributeDialog extends BottomSheetDialog {

    private static final int SPAN_COUNT = 6;
    private static final int ITEM_DECORATION_DP = 4;

    public interface Listener {
        void onImprovementChanged(int i);

        void onRankChanged(int i);
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private SimpleSelectableAdapter<String> mImprovementAdapter;
    private SimpleSelectableAdapter<String> mRankAdapter;

    @SuppressLint("DefaultLocale")
    public EquipAttributeDialog(@NonNull Context context, int star, int rank, boolean improvable, boolean rankupable) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.content_fleet_equip_attr, null);

        List<String> list;
        list = new ArrayList<>();
        list.add("+0");
        for (int i = 1; i <= 10; i++) {
            list.add(String.format("+%d", i));
        }
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) view.findViewById(R.id.equip_improvement_container);
        mImprovementAdapter = new SimpleSelectableAdapter<>(R.layout.item_dialog_equip_improvement, true);
        mImprovementAdapter.setSelection(star);
        mImprovementAdapter.setItemList(list);
        recyclerView.setAdapter(mImprovementAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) >= SPAN_COUNT) {
                    outRect.top = Math.round(ITEM_DECORATION_DP * getContext().getResources().getDisplayMetrics().density);
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.equip_rank_container);
        mRankAdapter = new SimpleSelectableAdapter<>(R.layout.item_dialog_equip_improvement, true);
        mRankAdapter.setSelection(rank);
        mRankAdapter.setItemList(Fleet.equipRank);
        recyclerView.setAdapter(mRankAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) >= SPAN_COUNT) {
                    outRect.top = Math.round(ITEM_DECORATION_DP * getContext().getResources().getDisplayMetrics().density);
                }
            }
        });

        mImprovementAdapter.setOnItemClickListener(new SimpleAdapter.Listener() {
            @Override
            public void OnClick(int position) {
                if (mListener != null) {
                    mListener.onImprovementChanged(position);
                }
            }
        });

        mRankAdapter.setOnItemClickListener(new SimpleAdapter.Listener() {
            @Override
            public void OnClick(int position) {
                if (mListener != null) {
                    mListener.onRankChanged(position);
                }
            }
        });

        if (!improvable) {
            view.findViewById(R.id.equip_improvement_container).setVisibility(View.GONE);
            view.findViewById(android.R.id.text1).setVisibility(View.GONE);
        }

        if (!rankupable) {
            view.findViewById(R.id.equip_rank_container).setVisibility(View.GONE);
            view.findViewById(android.R.id.text2).setVisibility(View.GONE);
        }

        setContentView(view);
    }
}
