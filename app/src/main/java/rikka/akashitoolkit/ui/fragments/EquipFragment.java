package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import rikka.akashitoolkit.adapter.EquipAdapter;

/**
 * Created by Rikka on 2016/3/23.
 */
public class EquipFragment extends BaseDisplayFragment<EquipAdapter> {
    private int mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt("TYPE");
        }

        setAdapter(new EquipAdapter(getActivity(), mType));
    }

    /*private class ItemDecoration extends BaseRecyclerViewItemDecoration {
        public ItemDecoration(Context context) {
            super(context);
        }

        @Override
        public boolean canDraw(RecyclerView parent, View child, int childCount, int position) {
            View view = parent.getChildAt(position + 1);
            boolean result = view != null && child.findViewById(android.R.id.title) != null && child.findViewById(android.R.id.title).getVisibility() == View.VISIBLE;
            Log.d("QAQ", Integer.toString(position) + " " + Boolean.toString(result));
            return result;
        }
    }*/
}
