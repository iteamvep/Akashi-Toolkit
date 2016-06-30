package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.model.ShipClass;
import rikka.akashitoolkit.staticdata.ShipClassList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.ShipDisplayActivity;
import rikka.akashitoolkit.utils.Utils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipAdapter extends BaseBookmarkRecyclerAdapter<ViewHolder.Ship> {
    private List<Ship> mData;
    private Activity mActivity;
    private int mShowVersion;
    private boolean mEnemy;
    private int mTypeFlag;
    private int mShowSpeed;
    private int mSort;
    private String mKeyword;
    private boolean mIsSearching;

    private Toast mToast;

    @Override
    public long getItemId(int position) {
        return (long) mData.get(position).getId();
    }

    public ShipAdapter(Activity activity, int showVersion, int typeFlag, int showSpeed, int sort, boolean bookmarked, boolean enemy) {
        super(bookmarked);

        mData = new ArrayList<>();
        mActivity = activity;

        setHasStableIds(true);

        mShowVersion = showVersion;
        mTypeFlag = typeFlag;
        mShowSpeed = showSpeed;
        mSort = sort;
        mEnemy = enemy;

        mActivity = activity;

        rebuildDataList();
    }

    public void setSearching(boolean searching) {
        mIsSearching = searching;
    }

    public void setSort(int sort) {
        mSort = sort;
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

    public void setShowVersion(int showVersion) {
        mShowVersion = showVersion;
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

                        if (mSort == 1) {
                            ShipList.sortByClass();
                        } else {
                            ShipList.sort();
                        }

                        for (Ship item :
                                o) {
                            if (check(item)) {
                                mData.add(item);
                            }
                        }

                        onDataListRebuilt(mData);
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
        if (mEnemy) {
            return item.getId() > 500;
        }

        if (!mEnemy && item.getId() > 500) {
            return false;
        }

        if (!mIsSearching) {
            switch (mShowVersion) {
                case 1:
                    if (item.getRemodel().getFromId() != 0) {
                        return false;
                    }
                    break;
                case 2:
                    if ((item.getRemodel().getToId() != 0 &&
                            item.getRemodel().getToId() != item.getRemodel().getFromId())) {
                        return false;
                    }
                    break;
            }
        }

        if (requireBookmarked() && (!mIsSearching) && !item.isBookmarked()) {
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

        if (mIsSearching && mKeyword.length() == 0) {
            return false;
        }

        if (mIsSearching && mKeyword != null &&
                !item.getName().getJa().contains(mKeyword) &&
                !item.getName().getZh_cn().contains(mKeyword) &&
                (item.getNameForSearch() == null || !item.getNameForSearch().contains(mKeyword))) {
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
    public void onBindViewHolder(final ViewHolder.Ship holder, int position) {
        Ship item = mData.get(position);

        boolean showDivider;
        boolean showTitle;
        String curType = null;
        int cType;

        if (mSort == 0) {
            curType = ShipList.shipType[item.getType()];

            showTitle = position <= 0 || !curType.equals(ShipList.shipType[mData.get(position - 1).getType()]);
            showDivider = position < mData.size() - 1
                    && curType.equals(ShipList.shipType[mData.get(position + 1).getType()]);
        } else {
            cType = item.getClassType();
            ShipClass shipClass = ShipClassList.findItemById(mActivity, cType);
            if (shipClass != null) {
                curType = shipClass.getName();
            }

            showTitle = position <= 0 || cType != mData.get(position - 1).getClassType();
            showDivider = position < mData.size() - 1
                    && cType == mData.get(position + 1).getClassType();
        }

        if (showTitle) {
            holder.mTitle.setText(curType);
            holder.mTitle.setVisibility(View.VISIBLE);
        } else {
            holder.mTitle.setVisibility(View.GONE);
        }

        holder.mDivider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView.setVisibility(!showDivider ? View.VISIBLE : View.GONE);
        holder.mDummyView2.setVisibility(showTitle && position != 0 ? View.VISIBLE : View.GONE);

        holder.mName.setText(String.format(item.isBookmarked() ? "%s ★" : "%s",
                item.getName().get(holder.mName.getContext())));

        if (!mEnemy) {
            ShipClass shipClass = ShipClassList.findItemById(mActivity, item.getClassType());

            if (shipClass != null) {
                String c;
                if (mSort == 0) {
                    c = String.format("%s%s号舰", shipClass.getName(), Utils.getChineseNumberString(item.getClassNum()));
                } else {
                    c = String.format("%s号舰", Utils.getChineseNumberString(item.getClassNum()));
                }
                holder.mName2.setText(c);
            } else {
                Log.d("ShipAdapter", "No ship class: " + item.getName().get(mActivity));
                holder.mName2.setText("");
            }
        }

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ship item = mData.get(holder.getAdapterPosition());

                Intent intent = new Intent(v.getContext(), ShipDisplayActivity.class);
                intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                intent.putExtra(ShipDisplayActivity.EXTRA_IS_ENEMY, mEnemy);

                int[] location = new int[2];
                holder.mLinearLayout.getLocationOnScreen(location);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_HEIGHT, holder.mLinearLayout.getHeight());

                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });

        if (!mEnemy) {
            holder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onLongClick(View v) {
                    Ship item = mData.get(holder.getAdapterPosition());

                    item.setBookmarked(!item.isBookmarked());

                    Settings.instance(mActivity)
                            .putBoolean(String.format("ship_%d_%d", item.getClassType(), item.getClassNum()), item.isBookmarked());

                    if (mToast != null) {
                        mToast.cancel();
                    }

                    mToast = Toast.makeText(mActivity, item.isBookmarked() ? mActivity.getString(R.string.bookmark_add) : mActivity.getString(R.string.bookmark_remove), Toast.LENGTH_SHORT);
                    mToast.show();

                    notifyItemChanged(holder.getAdapterPosition());

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
