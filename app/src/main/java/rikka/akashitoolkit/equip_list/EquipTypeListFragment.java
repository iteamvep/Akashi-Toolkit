package rikka.akashitoolkit.equip_list;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.SimpleAdapter;
import rikka.akashitoolkit.model.EquipTypeParent;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;
import rikka.akashitoolkit.staticdata.EquipTypeParentList;
import rikka.akashitoolkit.ui.fragments.DrawerFragment;
import rikka.akashitoolkit.ui.fragments.IBackFragment;
import rikka.akashitoolkit.ui.widget.IconSwitchCompat;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.SimpleTitleViewHolder;

/**
 * Created by Rikka on 2016/10/4.
 */

public class EquipTypeListFragment extends DrawerFragment implements SimpleAdapter.Listener, IBackFragment {

    private RecyclerView mRecyclerView;
    private SimpleAdapter<String> mAdapter;

    private static final int ICON[] = new int[]{
            R.drawable.system_icon_03_24dp,
            R.drawable.system_icon_05_24dp,
            R.drawable.system_icon_06_24dp,
            R.drawable.system_icon_11_24dp,
            R.drawable.system_icon_15_24dp,
            R.drawable.system_icon_17_24dp,
            R.drawable.system_icon_26_24dp
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_recycler_in_frame, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SimpleAdapter<String>(R.layout.item_ship_type) {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Drawable dr = ContextCompat.getDrawable(holder.itemView.getContext(), ICON[position]);
                dr.setBounds(0, 0, Utils.dpToPx(24), Utils.dpToPx(24));
                DrawableCompat.setTint(dr, ContextCompat.getColor(holder.itemView.getContext(), R.color.text));
                TextViewCompat.setCompoundDrawablesRelative(((SimpleTitleViewHolder) holder).mTitle, dr, null, null, null);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        setAdapterData(mAdapter);

        mAdapter.setOnItemClickListener(this);

        BusProvider.instance().register(this);
    }

    public void setAdapterData(SimpleAdapter<String> adapter) {
        List<String> list = new ArrayList<>();
        for (EquipTypeParent item : EquipTypeParentList.getList()) {
            list.add(item.getName().get());
        }
        adapter.setItemList(list);
    }

    @Override
    public void OnClick(int position) {
        Fragment fragment = new EquipListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", EquipTypeParentList.getList().get(position).getName().get());
        bundle.putIntegerArrayList(EquipAdapter.ARG_TYPE_IDS, (ArrayList<Integer>) EquipTypeParentList.getList().get(position).getChild());
        fragment.setArguments(bundle);

        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.dir_enter, R.anim.dir_leave, R.anim.dir_enter, R.anim.dir_leave)
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(150);
        animation.setFillAfter(true);
        animation.setInterpolator(new FastOutSlowInInterpolator());
        mRecyclerView.clearAnimation();
        mRecyclerView.startAnimation(animation);
    }

    @Override
    public boolean onBackPressed() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();

            Animation animation = new AlphaAnimation(0, 1);
            animation.setDuration(350);
            animation.setFillAfter(true);
            animation.setInterpolator(new FastOutSlowInInterpolator());
            mRecyclerView.clearAnimation();
            mRecyclerView.startAnimation(animation);

            setToolbarTitle(R.string.equip);

            return true;
        }
        return false;
    }

    @Override
    public void onShow() {
        super.onShow();

        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            if (getChildFragmentManager().getFragments().get(0) != null) {
                setToolbarTitle(getChildFragmentManager().getFragments().get(0).getArguments().getString("TITLE"));
            }
        } else {
            setToolbarTitle(R.string.equip);
        }
    }

    @Override
    public void onDestroyView() {
        BusProvider.instance().unregister(this);
        super.onDestroyView();
    }

    @Override
    protected boolean onSetSwitch(IconSwitchCompat switchView) {
        return true;
    }

    @Subscribe
    public void dataChanged(DataChangedAction action) {
        if (action.getClassName().equals("any")) {
            setAdapterData(mAdapter);
        }
    }
}
