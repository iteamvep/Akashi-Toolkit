package rikka.akashitoolkit.expedition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.fragments.BaseDrawerItemFragment;
import rikka.akashitoolkit.ui.widget.RadioButtonGroup;
import rikka.akashitoolkit.ui.widget.SimpleDrawerView;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/14.
 */
public class ExpeditionDisplayFragment extends BaseDrawerItemFragment implements RadioButtonGroup.OnCheckedChangeListener {
    private ExpeditionAdapter mAdapter;

    @Override
    public void onShow() {
        super.onShow();

        mActivity.getSupportActionBar().setTitle(getString(R.string.expedition));
        mActivity.getRightDrawerContent().removeAllViews();

        Statistics.onFragmentStart("ExpeditionDisplayFragment");

        NestedScrollView scrollView = new NestedScrollView(getContext());
        scrollView.setLayoutParams(new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.setClipToPadding(false);

        SimpleDrawerView body = new SimpleDrawerView(getContext());

        body.setOrientation(LinearLayout.VERTICAL);
        body.setLayoutParams(new SimpleDrawerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        body.setPadding(0, Utils.dpToPx(4), 0, Utils.dpToPx(4));

        RadioButtonGroup radioButtonGroup;
        radioButtonGroup = new RadioButtonGroup(getContext());
        radioButtonGroup.addItem(R.string.all); // 0
        radioButtonGroup.addDivider();
        radioButtonGroup.addTitle("在线时推荐远征组合");
        radioButtonGroup.addItem("综合类1"); // 1
        radioButtonGroup.addItem("综合类2"); // 2
        radioButtonGroup.addItem("综合类3"); // 3
        radioButtonGroup.addItem("桶最大化"); // 4
        radioButtonGroup.addItem("油最大化"); // 5
        radioButtonGroup.addItem("弹最大化"); // 6
        radioButtonGroup.addItem("钢最大化"); // 7
        radioButtonGroup.addItem("铝最大化"); // 8
        radioButtonGroup.addDivider();
        radioButtonGroup.addTitle("不在线/睡觉时推荐远征组合");
        radioButtonGroup.addItem("3小时综合"); // 9
        radioButtonGroup.addItem("9小时综合"); // 10
        radioButtonGroup.addItem("9小时油最大化"); // 11
        radioButtonGroup.addItem("9小时弹最大化"); // 12
        radioButtonGroup.addItem("9小时钢最大化"); // 13
        radioButtonGroup.addItem("9小时铝最大化"); // 14
        radioButtonGroup.addItem("経験値最大化"); // 15
        radioButtonGroup.addItem("维护跑油"); // 16
        radioButtonGroup.addItem("维护跑铝"); // 17
        radioButtonGroup.setOnCheckedChangeListener(this);
        radioButtonGroup.setChecked(0);

        body.addView(radioButtonGroup);

        scrollView.addView(body);

        mActivity.getRightDrawerContent().addTitle(R.string.action_filter);
        mActivity.getRightDrawerContent().addDividerHead();
        mActivity.getRightDrawerContent().addView(scrollView);

        mActivity.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean checked) {
                mAdapter.setBookmarked(checked);
                mAdapter.rebuildDataList();
            }
        });
        mActivity.getSwitch().setChecked(false);
    }

    @Override
    public void onCheckedChanged(View view, int checked) {
        List<Integer> list = new ArrayList<>();
        switch (checked) {
            case 0:
                break;
            case 1:
                list.add(5);
                list.add(37);
                list.add(38);
                break;
            case 2:
                list.add(2);
                list.add(5);
                list.add(38);
                break;
            case 3:
                list.add(21);
                list.add(37);
                list.add(38);
                break;
            case 4:
                list.add(2);
                list.add(4);
                list.add(13);
                break;
            case 5:
                list.add(5);
                list.add(21);
                list.add(38);
                break;
            case 6:
                list.add(2);
                list.add(5);
                list.add(37);
                break;
            case 7:
                list.add(3);
                list.add(37);
                list.add(38);
                break;
            case 8:
                list.add(5);
                list.add(6);
                list.add(11);
                break;
            case 9:
                list.add(21);
                list.add(37);
                list.add(38);
                break;
            case 10:
                list.add(12);
                list.add(35);
                list.add(36);
                break;
            case 11:
                list.add(9);
                list.add(24);
                list.add(36);
                break;
            case 12:
                list.add(12);
                list.add(13);
                list.add(37);
                break;
            case 13:
                list.add(12);
                list.add(14);
                list.add(18);
                break;
            case 14:
                list.add(11);
                list.add(35);
                list.add(36);
                break;
            case 15:
                list.add(22);
                list.add(23);
                break;
            case 16:
                list.add(9);
                list.add(21);
                list.add(38);
                break;
            case 17:
                list.add(11);
                list.add(35);
                list.add(40);
                break;
        }

        mAdapter.setFilter(list);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.expedition, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                mActivity.getDrawerLayout().openDrawer(GravityCompat.END);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHide() {
        super.onHide();

        Statistics.onFragmentEnd("ExpeditionDisplayFragment");
    }

    @Override
    protected boolean getRightDrawerLock() {
        return false;
    }

    @Override
    protected boolean getSwitchVisible() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_recycler, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new ExpeditionAdapter(getContext(), mActivity.getSwitch().isChecked());
        recyclerView.setAdapter(mAdapter);
        /*recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(getContext()) {
            @Override
            public boolean canDraw(RecyclerView parent, View child, int childCount, int position) {
                return mAdapter.getItemViewType(position - 1) super.canDraw(parent, child, childCount, position);
            }
        });*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.cardBackground));

        /*if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }*/

        return view;
    }
}
