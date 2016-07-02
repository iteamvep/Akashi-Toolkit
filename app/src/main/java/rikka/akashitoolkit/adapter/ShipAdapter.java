package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.model.ShipClass;
import rikka.akashitoolkit.staticdata.ShipClassList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.ShipDisplayActivity;
import rikka.akashitoolkit.ui.widget.LinearLayoutManager;
import rikka.akashitoolkit.utils.Utils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipAdapter extends BaseBookmarkRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<Data> mData;
    private Map<Long, Boolean> mExpanded;

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
        return mData.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
    }

    public ShipAdapter(Activity activity, int showVersion, int typeFlag, int showSpeed, int sort, boolean bookmarked, boolean enemy) {
        super(bookmarked);

        mData = new ArrayList<>();
        mExpanded = new LinkedHashMap<>();
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
        mExpanded.clear();
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

    @Override
    public void setBookmarked(boolean bookmarked) {
        for (Map.Entry<Long, Boolean> entry :
                mExpanded.entrySet()) {
            entry.setValue(bookmarked);
        }
        super.setBookmarked(bookmarked);
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

                        String type = null;

                        for (Ship item : o) {
                            if (check(item)) {


                                String curType = null;
                                long id;
                                if (mSort == 0) {
                                    id = item.getType() * 10;
                                    curType = ShipList.shipType[item.getType()];
                                } else {
                                    ShipClass shipClass = ShipClassList.findItemById(mActivity, item.getClassType());
                                    if (shipClass != null) {
                                        curType = shipClass.getName();
                                    }
                                    id = -item.getClassType() * 10;
                                }

                                if (curType != null && !curType.equals(type)) {
                                    type = curType;
                                    if (mExpanded.get(id) == null) {
                                        mExpanded.put(id, requireBookmarked());
                                    }

                                    mData.add(new Data(type, 1, id));
                                }

                                if (mExpanded.get(id))
                                    mData.add(new Data(item, 0, item.getId() * 10000));
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

        if (item.getId() > 500) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder.Ship(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship, parent, false));
            case 1:
                return new ViewHolder.Subtitle(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_subtitle, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                bindViewHolder((ViewHolder.Ship) holder, position);
                break;
            case 1:
                bindViewHolder((ViewHolder.Subtitle) holder, position);
                break;
        }
    }

    private void bindViewHolder(final ViewHolder.Ship holder, int position) {
        Ship item = (Ship) mData.get(position).data;

        holder.mTitle.setText(String.format(item.isBookmarked() ? "%s ★" : "%s",
                item.getName().get(holder.mTitle.getContext())));

        if (!mEnemy) {
            ShipClass shipClass = ShipClassList.findItemById(mActivity, item.getClassType());

            if (shipClass != null) {
                String c;
                if (mSort == 0) {
                    c = String.format("%s%s号舰", shipClass.getName(), Utils.getChineseNumberString(item.getClassNum()));
                } else {
                    c = String.format("%s号舰", Utils.getChineseNumberString(item.getClassNum()));
                }
                holder.mContent.setText(c);
            } else {
                Log.d("ShipAdapter", "No ship class: " + item.getName().get(mActivity));
                holder.mContent.setText("");
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() < 0) {
                    return;
                }

                Ship item = (Ship) mData.get(holder.getAdapterPosition()).data;

                Intent intent = new Intent(v.getContext(), ShipDisplayActivity.class);
                intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                intent.putExtra(ShipDisplayActivity.EXTRA_IS_ENEMY, mEnemy);

                int[] location = new int[2];
                holder.itemView.getLocationOnScreen(location);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(ShipDisplayActivity.EXTRA_START_HEIGHT, holder.itemView.getHeight());

                v.getContext().startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });

        if (!mEnemy) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public boolean onLongClick(View v) {
                    Ship item = (Ship) mData.get(holder.getAdapterPosition()).data;

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

    private ViewHolder.Subtitle mLastHolder;

    private int mFirstVisibleItemPosition = -1;
    private long mItemId = -1;

    private void bindViewHolder(final ViewHolder.Subtitle holder, int position) {
        Context context = holder.itemView.getContext();

        boolean expanded = mExpanded.get(getItemId(position));
        boolean showDivider = position != 0 && expanded || position != 0 && (getItemViewType(position - 1) != getItemViewType(position));

        holder.mDivider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
        holder.mTitle.setText((String) mData.get(position).data);

        if (holder.itemView.isSelected() != expanded
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) context.getDrawable(expanded ? R.drawable.ic_expand_more_to_less_24dp : R.drawable.ic_expand_less_to_more_24dp);
            drawable.start();
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(holder.mTitle, null, null, drawable, null);

            mItemId = -1;
        } else {
            Drawable drawable = AppCompatDrawableManager.get()
                    .getDrawable(context, expanded ? R.drawable.ic_expand_less_24dp : R.drawable.ic_expand_more_24dp);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(holder.mTitle, null, null, drawable, null);

        }

        holder.itemView.setSelected(expanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemId = getItemId(holder.getAdapterPosition());
                Boolean expanded = mExpanded.get(mItemId);
                expanded = !expanded;
                mExpanded.put(mItemId, expanded);

                if (expanded) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mFirstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    //mRecyclerView.scrollBy(0, Utils.dpToPx(9));
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            int position = holder.getAdapterPosition();
                            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                            layoutManager.smoothScrollToPositionWithOffset(
                                    mRecyclerView,
                                    position,
                                    LinearSmoothScroller.SNAP_TO_START,
                                    position != 0 ? -Utils.dpToPx(5) : 0);
                            mLastHolder = holder;
                        }
                    });
                } else {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mFirstVisibleItemPosition < 0) {
                                mFirstVisibleItemPosition = 0;
                            }
                            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                            layoutManager.smoothScrollToPosition(mRecyclerView, mFirstVisibleItemPosition, LinearSmoothScroller.SNAP_TO_ANY);
                        }
                    });
                }

                rebuildDataList();
            }
        });
    }

    public boolean collapseLastType() {
        if (requireBookmarked() || mLastHolder == null) {
            return false;
        }

        if (getItemCount() == 0) {
            return false;
        }

        mLastHolder.itemView.performClick();
        mLastHolder = null;

        return true;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = null;
        mLastHolder = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
