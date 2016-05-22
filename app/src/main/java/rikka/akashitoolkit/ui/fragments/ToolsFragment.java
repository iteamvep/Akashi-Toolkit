package rikka.akashitoolkit.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.ExpCalcActivity;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.ui.RankingActivity;

/**
 * Created by Rikka on 2016/4/9.
 */
public class ToolsFragment extends BaseDrawerItemFragment {
    @Override
    public void onShow() {
        super.onShow();

        MainActivity activity = ((MainActivity) getActivity());
        activity.getSupportActionBar().setTitle(getString(R.string.tools));

        Statistics.onFragmentStart("ToolsFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("ToolsFragment");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_tools, container, false);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), ExpCalcActivity.class));
            }
        });

        /*view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), RankingActivity.class));
            }
        });*/

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }
}
