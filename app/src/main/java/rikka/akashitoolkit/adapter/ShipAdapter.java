package rikka.akashitoolkit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.ui.ShipDisplayActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipAdapter extends RecyclerView.Adapter<ViewHolder.Ship> {
    private List<Ship> mData;
    private Activity mActivity;
    private boolean mShowOnlyFinalVersion;
    private int mTypeFlag;
    private int mShowSpeed;
    private String mKeyword;

    public ShipAdapter(Activity activity) {
        mData = new ArrayList<>();
        mActivity = activity;
    }

    public ShipAdapter(Activity activity, boolean showOnlyFinalVersion, int typeFlag, int showSpeed) {
        this(activity);

        mShowOnlyFinalVersion = showOnlyFinalVersion;
        mTypeFlag = typeFlag;
        mShowSpeed = showSpeed;
        mActivity = activity;
    }

    public void setShowSpeed(int showSpeed) {
        mShowSpeed = showSpeed;
    }

    public int getTypeFlag() {
        return mTypeFlag;
    }

    public void setTypeFlag(int typeFlag) {
        mTypeFlag = typeFlag;
    }

    public void setKeyword(String keyword) {
        if (mKeyword != null && mKeyword.equals(keyword)) {
            return;
        }

        mKeyword = keyword;
    }

    public void setShowOnlyFinalVersion(boolean showOnlyFinalVersion) {
        mShowOnlyFinalVersion = showOnlyFinalVersion;
    }

    public void rebuildDataList() {
        Observable
                .create(new Observable.OnSubscribe<List<Ship>>() {
                    @Override
                    public void call(Subscriber<? super List<Ship>> subscriber) {
                        subscriber.onNext(ShipList.get(mActivity));
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Ship>>() {
                    @Override
                    public void onNext(List<Ship> o) {
                        mData.clear();

                        for (Ship item :
                                o) {
                            if (check(item)) {
                                mData.add(item);
                            }
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private boolean check(Ship item) {
        if (mShowOnlyFinalVersion && (item.getRemodel().getId_to() != 0 &&
                item.getRemodel().getId_to() != item.getRemodel().getId_from())) {
            return false;
        }

        if (mTypeFlag != 0 && (1 << item.getType() & mTypeFlag) == 0) {
            return false;
        }

        if (mShowSpeed != 0 &&
                !((mShowSpeed & 1) > 0 && item.getAttr().getSpeed() == 5 ||
                (mShowSpeed & 1 << 1) > 0 && item.getAttr().getSpeed() == 10)) {
            return false;
        }

        if (mKeyword != null && mKeyword.length() == 0) {
            return false;
        }

        if (mKeyword != null &&
                !item.getName().getJa().contains(mKeyword) &&
                !item.getName().getZh_cn().contains(mKeyword) &&
                (item.getName_for_search() == null || !item.getName_for_search().contains(mKeyword))) {
            return false;
        }

        return true;
    }

    @Override
    public ViewHolder.Ship onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ship, parent, false);
        return new ViewHolder.Ship(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder.Ship holder, final int position) {
        Ship item = mData.get(position);
        String curType = ShipList.shipType[item.getType()];
        boolean showTitle = position <= 0 || !curType.equals(ShipList.shipType[mData.get(position - 1).getType()]);

        if (showTitle) {
            holder.mTitle.setText(curType);
            holder.mTitle.setVisibility(View.VISIBLE);
        } else {
            holder.mTitle.setVisibility(View.GONE);
        }

        boolean showDivider = position < mData.size() - 1
                && curType.equals(ShipList.shipType[mData.get(position + 1).getType()]);

        holder.mDivider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView.setVisibility(!showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView2.setVisibility(showTitle && position != 0 ? View.VISIBLE : View.GONE);

        holder.mName.setText(mData.get(position).getName().getZh_cn());

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShipDisplayActivity.class);
                intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, mData.get(position).getId());

                int[] location = new int[2];
                holder.mLinearLayout.getLocationOnScreen(location);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_HEIGHT, holder.mLinearLayout.getHeight());

                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
