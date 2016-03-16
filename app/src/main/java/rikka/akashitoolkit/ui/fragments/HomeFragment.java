package rikka.akashitoolkit.ui.fragments;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.utils.UpdateCheck;
import rikka.akashitoolkit.widget.ButtonCardView;

/**
 * Created by Rikka on 2016/3/6.
 */
public class HomeFragment extends BaseFragmet {
    private static final int TAB_LAYOUT_VISIBILITY = View.GONE;

    private LinearLayout mLinearLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ButtonCardView mButtonCardView;

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onHide() {
        super.onHide();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onStop() {
        UpdateCheck.instance().recycle();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        if (savedInstanceState == null) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 500);
        }

        mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        mLinearLayout.addView(new ButtonCardView(getContext())
                        .setTitle("一些提示")
                        .addButton("知道啦")
                        .setMessage("现在主页下拉可以检查更新")
        );
        mLinearLayout.addView(new ButtonCardView(getContext())
                .setTitle("这条用来卖萌的")
                .setMessage("比如保存有没有点过“知道了”还没有做..")
                .addButton("我不是")
                .addButton("我是accent颜色", null, true, false)
        );




        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        /*if (Settings.instance(getContext()).getIntFromString(Settings.UPDATE_CHECK_PERIOD, 0) == 0) {
            refresh();
        }*/

        return view;
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        UpdateCheck.instance().check(getContext(), new Callback<CheckUpdate>() {
            @Override
            public void onResponse(Call<CheckUpdate> call, final Response<CheckUpdate> response) {
                int versionCode;
                try {
                    versionCode = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                if (response.body().getVersionCode() > versionCode || BuildConfig.DEBUG) {
                    if (mButtonCardView == null || mButtonCardView.getVisibility() != View.VISIBLE) {
                        mButtonCardView = new ButtonCardView(getContext());
                        mButtonCardView.addButton("才不要", true);
                        mButtonCardView.addButton("去下载", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getUrl()));
                                getContext().startActivity(intent);
                            }
                        }, true, false);
                        mLinearLayout.addView(mButtonCardView, 0);
                    }

                    mButtonCardView.setMessage(String.format("更新内容:\n%s", response.body().getChange()));
                    mButtonCardView.setTitle(String.format("有新版本啦 (%s - %d)", response.body().getVersionName(), response.body().getVersionCode()));
                }

                UpdateCheck.instance().recycle();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CheckUpdate> call, Throwable t) {
                UpdateCheck.instance().recycle();
                mSwipeRefreshLayout.setRefreshing(false);

                showSnackbar(R.string.refresh_fail, Snackbar.LENGTH_SHORT);
            }
        });
    }
}
