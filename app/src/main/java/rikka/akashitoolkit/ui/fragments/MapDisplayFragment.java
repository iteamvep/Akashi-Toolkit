package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.model.MapType;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;
import rikka.akashitoolkit.staticdata.MapTypeList;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/4/9.
 */
public class MapDisplayFragment extends BaseFragment {
    private ViewPager mViewPager;

    protected Object mBusEventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBusEventListener = new Object() {
            @Subscribe
            public void dataChanged(final DataChangedAction event) {
                MapDisplayFragment.this.dataChanged(event);
            }
        };

        BusProvider.instance().register(mBusEventListener);
    }

    @Override
    public void onDestroyView() {
        BusProvider.instance().unregister(mBusEventListener);
        super.onDestroyView();
    }

    @Override
    protected boolean getTabLayoutVisible() {
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();

        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setupWithViewPager(mViewPager);
        activity.getSupportActionBar().setTitle(getString(R.string.maps));

        Statistics.onFragmentStart("MapDisplayFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("MapDisplayFragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_viewpager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(getAdapter());

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private ViewPagerAdapter getAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", MapTypeList.get(getContext()).get(position).getType());
                return bundle;
            }
        };

        for (MapType type :
                MapTypeList.get(getContext())) {
            adapter.addFragment(MapFragment.class, type.getName().get(getContext()));
        }

        return adapter;
    }

    @Subscribe
    public void dataChanged(DataChangedAction action) {
        if (action.getClassName().equals("any")
                || action.getClassName().equals(this.getClass().getSimpleName())) {
            mViewPager.setAdapter(getAdapter());
            ((MainActivity) getActivity()).getTabLayout().setupWithViewPager(mViewPager);
        }
    }
}
