package rikka.akashitoolkit.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.model.MessageReadStatus;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.DataChangedAction;
import rikka.akashitoolkit.otto.PreferenceChangedAction;
import rikka.akashitoolkit.otto.ReadStatusResetAction;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.utils.UpdateCheck;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.widget.ButtonCardView;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static rikka.akashitoolkit.support.ApiConstParam.JSON_NAME;
import static rikka.akashitoolkit.support.ApiConstParam.JSON_VERSION;
/**
 * Created by Rikka on 2016/3/6.
 */
public class HomeFragment extends BaseDrawerItemFragment {
    private LinearLayout mLinearLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ButtonCardView mUpdateCardView;
    private ButtonCardView mDataUpdateCardView;
    private Map<Integer, ButtonCardView> mMessageCardView;
    private MessageReadStatus mMessageReadStatus;
    private int mUpdateVersionCode;

    @Override
    public void onShow() {
        super.onShow();

        MainActivity activity = ((MainActivity) getActivity());
        activity.getSupportActionBar().setTitle(getString(R.string.app_name));

        Statistics.onFragmentStart("HomeFragment");
    }

    @Override
    public void onHide() {
        super.onHide();
        mSwipeRefreshLayout.setRefreshing(false);
        saveReadStatus();

        Statistics.onFragmentEnd("HomeFragment");
    }

    @Override
    public void onStop() {
        UpdateCheck.instance().recycle();
        saveReadStatus();
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.instance().register(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        BusProvider.instance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.twitter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    refresh();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            }, 200);
        }

        mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        mMessageReadStatus = Settings
                .instance(getContext())
                .getGSON(Settings.MESSAGE_READ_STATUS, MessageReadStatus.class);

        if (mMessageReadStatus == null) {
            mMessageReadStatus = new MessageReadStatus();
        }

        if (mMessageReadStatus.getMessageId() == null) {
            mMessageReadStatus.setMessageId(new ArrayList<Integer>());
        }

        mMessageCardView = new HashMap<>();
        addLocalCard();

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        /*if (Settings.instance(getContext()).getIntFromString(Settings.UPDATE_CHECK_PERIOD, 0) == 0) {
            refresh();
        }*/

        return view;
    }

    private void saveReadStatus() {
        MessageReadStatus messageReadStatus = new MessageReadStatus();
        List<Integer> list = new ArrayList<>();
        messageReadStatus.setMessageId(list);
        messageReadStatus.setVersionCode(0);

        if (mUpdateCardView != null && mUpdateCardView.getVisibility() == View.GONE) {
            messageReadStatus.setVersionCode(mUpdateVersionCode);
        }

        for (Map.Entry<Integer, ButtonCardView> card:
                mMessageCardView.entrySet()) {
            if (card.getValue().getVisibility() == View.GONE) {
                list.add(card.getKey());
            }
        }
        Settings.instance(getContext())
                .putGSON(Settings.MESSAGE_READ_STATUS, messageReadStatus);
    }

    private boolean checkId(int id) {
        for (int i:
        mMessageReadStatus.getMessageId()){
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    private void addLocalCard() {
        if (checkId(-1)) {
            return;
        }

        if (mMessageCardView.get(-1) != null) {
            return;
        }

        ButtonCardView card;

        if (!BuildConfig.isGooglePlay) {
            card = new ButtonCardView(getContext())
                    .setTitle("欢迎使用Akashi Toolkit！")
                    .addButton(R.string.got_it)
                    .setMessage("Akashi Toolkit是一个舰队Collection的wiki类手机App，目前由Yūbari Kaigun Kokusho开发，kcwiki舰娘百科提供数据支持。\n" +
                            "目前应用的各项功能正在逐渐添加和完善中，我们会在每周六晚发布一个Akashi Toolkit的正式版本，保证每周的更新。\n" +
                            "如果您想体验测试版，在设置-更新通道中选择测试版。\n" +
                            "关注我们的最新消息 微博@kcwiki舰娘百科");
        } else {
            card = new ButtonCardView(getContext())
                    .setTitle("欢迎使用Akashi Toolkit！")
                    .addButton(R.string.got_it)
                    .setMessage("Akashi Toolkit是一个舰队Collection的wiki类手机App，目前由Yūbari Kaigun Kokusho开发，kcwiki舰娘百科提供数据支持。\n" +
                            "目前应用的各项功能正在逐渐添加和完善中。\n" +
                            "如果您想体验测试版，在主页的加入测试卡片（如果有）进入链接后选择加入，稍后您收到测试版本更新。\n" +
                            "关注我们的最新消息 微博@kcwiki舰娘百科");
        }

        mLinearLayout.addView(card);
        mMessageCardView.put(-1, card);
    }

    private void refresh() {
        saveReadStatus();

        mSwipeRefreshLayout.setRefreshing(true);

        UpdateCheck.instance().check(getContext(), new Callback<CheckUpdate>() {
            @Override
            public void onResponse(Call<CheckUpdate> call, final Response<CheckUpdate> response) {
                if (getContext() == null) {
                    return;
                }

                int versionCode;
                String versionName;

                versionCode = StaticData.instance(getContext()).versionCode;
                versionName = StaticData.instance(getContext()).versionName;


                if (response.body() == null
                        || response.body().getMessages() == null) {
                    onFailure(call, new NullPointerException());
                }

                for (final CheckUpdate.MessagesEntity entity :
                        response.body().getMessages()) {
                    if (checkId(entity.getId())) {
                        continue;
                    }

                    ButtonCardView card = mMessageCardView.get(entity.getId());

                    if (card == null) {
                        card = new ButtonCardView(getContext());
                        card.setTitle(entity.getTitle())
                                .setMessage(entity.getMessage())
                                .addButton(R.string.got_it);

                        switch (entity.getType()) {
                            case 1:
                                card.addButton(entity.getAction_name() != null ? entity.getAction_name() : getContext().getString(R.string.open_link), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getLink())));
                                    }
                                }, true, false);
                                break;
                            case 2:
                                card.setMessage(String.format(
                                                getContext().getString(R.string.todo_card_format),
                                                versionName,
                                                entity.getMessage()
                                        )
                                );
                                break;
                            case 3:
                                card.setMessageHtml(entity.getMessage());
                                if (entity.getLink() != null) {
                                    card.addButton(entity.getAction_name() != null ? entity.getAction_name() : getContext().getString(R.string.open_link), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getLink())));
                                        }
                                    }, true, false);
                                }
                                break;
                        }

                        mMessageCardView.put(entity.getId(), card);
                        mLinearLayout.addView(card);
                    }
                }

                final StringBuilder sb = new StringBuilder();
                if (!BuildConfig.isGooglePlay) {
                    final CheckUpdate.UpdateEntity entity = response.body().getUpdate();
                    mUpdateVersionCode = entity.getVersionCode();

                    if (mMessageReadStatus.getVersionCode() < mUpdateVersionCode && (mUpdateVersionCode > versionCode || BuildConfig.DEBUG)) {
                        if (mUpdateCardView == null || mUpdateCardView.getVisibility() != View.VISIBLE) {
                            mUpdateCardView = new ButtonCardView(getContext());
                            mUpdateCardView.addButton(R.string.ignore_update, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMessageReadStatus.setVersionCode(mUpdateVersionCode);
                                }
                            }, false, true);
                            mUpdateCardView.addButton(R.string.download, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getUrl()));
                                    getContext().startActivity(intent);
                                }
                            }, true, false);
                            mLinearLayout.addView(mUpdateCardView, 0);
                        }

                        mUpdateCardView.setMessage(String.format("更新内容:\n%s", entity.getChange()));
                        mUpdateCardView.setTitle(String.format("有新版本啦 (%s - %d)", entity.getVersionName(), entity.getVersionCode()));
                    } else {
                        //sb.append("应用已是最新版本").append('\n');
                    }
                }

                if (mDataUpdateCardView == null) {
                    mDataUpdateCardView = new ButtonCardView(getContext());
                    mDataUpdateCardView.setTitle("数据更新");
                    mDataUpdateCardView.addButton(R.string.got_it);
                }

                Observable
                        .from(response.body().getData())
                        .observeOn(Schedulers.io())
                        .subscribe(new Subscriber<CheckUpdate.DataEntity>() {
                            @Override
                            public void onNext(CheckUpdate.DataEntity dataEntity) {
                                File file = new File(getContext().getFilesDir().getAbsolutePath() + "/json/" + dataEntity.getName());

                                int savedVersion = Settings
                                        .instance(getContext())
                                        .getInt(dataEntity.getName(), JSON_VERSION.get(dataEntity.getName()));
                                int builtInVersion = JSON_VERSION.get(dataEntity.getName());
                                int latestVersion = dataEntity.getVersion();

                                if (!file.exists()) {
                                    savedVersion = 0;
                                }

                                if (builtInVersion > savedVersion) {
                                    file.delete();
                                    return;
                                } else if (savedVersion >= latestVersion || builtInVersion >= latestVersion) {
                                    return;
                                }

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://www.minamion.com/")
                                        .build();

                                RetrofitAPI.CheckUpdateService service = retrofit.create(RetrofitAPI.CheckUpdateService.class);
                                try {
                                    Utils.saveStreamToFile(
                                            service.download(dataEntity.getName()).execute().body().byteStream(),
                                            getContext().getFilesDir().getAbsolutePath() + "/json/" + dataEntity.getName());

                                    Settings.instance(getContext())
                                            .putInt(dataEntity.getName(), dataEntity.getVersion());

                                    String name = dataEntity.getName().replace(".json", "");
                                    Class<?> c;
                                    c = Class.forName(String.format(
                                            "rikka.akashitoolkit.staticdata.%sList",
                                            name));
                                    c.getMethod("clear").invoke(null);
                                    //c.getMethod("get").invoke(null, getContext());

                                    BusProvider.instance().post(new DataChangedAction(name + "Fragment"));

                                    sb.append(JSON_NAME.get(dataEntity.getName())).append("已更新 (").append(dataEntity.getData()).append(") \n");

                                    Log.d("DownloadData", dataEntity.getName() + " version: "+ Integer.toString(dataEntity.getVersion()));
                                } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCompleted() {
                                Handler mainHandler = new Handler(getContext().getMainLooper());
                                mainHandler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                if (sb.length() > 0) {
                                                    mDataUpdateCardView.setMessage(sb.toString().trim());
                                                    if (mDataUpdateCardView.getParent() == null) {
                                                        mLinearLayout.addView(mDataUpdateCardView, 0);
                                                    } else if (mDataUpdateCardView.getVisibility() == View.GONE) {
                                                        mDataUpdateCardView.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        }
                                );
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });

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

    @Subscribe
    public void readStatusReset(ReadStatusResetAction action) {
        int count = mLinearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mLinearLayout.getChildAt(i);
            if (view instanceof ButtonCardView) {
                view.setVisibility(View.VISIBLE);
                view.setTranslationX(0);
            }
        }
    }

    @Subscribe
    public void preferenceChanged(PreferenceChangedAction action) {
        if (action.getKey().equals(Settings.UPDATE_CHECK_CHANNEL)) {
            mLinearLayout.removeAllViews();

            mMessageReadStatus = new MessageReadStatus();
            mMessageReadStatus.setMessageId(new ArrayList<Integer>());

            Settings
                    .instance(getContext())
                    .putGSON(Settings.MESSAGE_READ_STATUS, mMessageReadStatus);

            mUpdateCardView = null;
            mMessageCardView.clear();

            addLocalCard();
            refresh();
        }
    }
}
