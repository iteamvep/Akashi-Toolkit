package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ItemAdapter;

/**
 * Created by Rikka on 2016/3/23.
 */
public class ItemFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_recycler, container, false);

        int type = 0;
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt("TYPE");
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new ItemAdapter(getActivity(), type));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new ItemDecoration(getContext()));

        return view;
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
