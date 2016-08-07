package rikka.akashitoolkit.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Locale;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.ExpCalcActivity;
import rikka.akashitoolkit.fleet_editor.FleetListActivity;
import rikka.akashitoolkit.ui.MainActivity;

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

        view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(v.getContext(), ExpCalcActivity.class);
            }
        });

        view.findViewById(android.R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, Locale.getDefault().equals(Locale.SIMPLIFIED_CHINESE)
                        ? "该功能尚在开发中" : "This feature is still in development.", Toast.LENGTH_SHORT).show();
                startActivity(v.getContext(), FleetListActivity.class);
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

    private static void startActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);

        boolean newTask = Settings.instance(context).getBoolean(Settings.OPEN_IN_NEW_DOCUMENT, false);
        if (newTask && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        context.startActivity(intent);
    }
}
