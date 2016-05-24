package rikka.akashitoolkit.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import static rikka.akashitoolkit.support.ApiConstParam.DATA_JSON_NAME;
import static rikka.akashitoolkit.support.ApiConstParam.DATA_JSON_VERSION;

import static rikka.akashitoolkit.support.ApiConstParam.Message.*;

/**
 * Created by Rikka on 2016/3/6.
 */
public class HomeFragment extends BaseDrawerItemFragment {
    private static final String JSON_NAME = "/json/home.json";
    private String CACHE_FILE;

    private LinearLayout mLinearLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ButtonCardView mUpdateCardView;
    private ButtonCardView mDataUpdateCardView;
    private Map<Integer, ButtonCardView> mMessageCardView;
    private MessageReadStatus mMessageReadStatus;
    private int mUpdateVersionCode;

    private CountDownTimer mCountDownTimer;

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
        mCountDownTimer.cancel();
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.instance().register(this);
        setHasOptionsMenu(true);
        CACHE_FILE = getContext().getCacheDir().getAbsolutePath() + JSON_NAME;
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
        loadFromCache();

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private void saveReadStatus() {
        if (mMessageReadStatus == null) {
            return;
        }

        MessageReadStatus messageReadStatus = mMessageReadStatus;
        List<Integer> list = messageReadStatus.getMessageId();
        if (messageReadStatus.getMessageId() == null) {
            return;
        }

        if (mUpdateCardView != null && mUpdateCardView.getVisibility() == View.GONE) {
            messageReadStatus.setVersionCode(mUpdateVersionCode);
        }

        for (Map.Entry<Integer, ButtonCardView> card:
                mMessageCardView.entrySet()) {
            if (card.getValue().getVisibility() == View.GONE
                    && list.indexOf(card.getKey()) == -1) {
                list.add(card.getKey());
            }
        }

        Settings.instance(getContext())
                .putGSON(Settings.MESSAGE_READ_STATUS, messageReadStatus);
    }

    private boolean isIdRead(int id) {
        for (int i : mMessageReadStatus.getMessageId()) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    private void addLocalCard() {
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
                            "关注我们的最新消息 微博@kcwiki舰娘百科");
        }

        addCard(card);
    }

    private boolean addCard(ButtonCardView card) {
        return addCard(card, (card.getTitle() + card.getBody()).hashCode());
    }

    private boolean addCard(ButtonCardView card, int id) {
        return addCard(card, id, -1);
    }

    private boolean addCard(ButtonCardView card, int id, int position) {
        if (isIdRead(id)) {
            return false;
        }

        if (mMessageCardView.get(id) == null) {
            mLinearLayout.addView(card, position);
            mMessageCardView.put(id, card);

            return true;
        }
        return false;
    }

    private void loadFromCache() {
        CheckUpdate data;
        try {
            Gson gson = new Gson();
            data = gson.fromJson(
                    new FileReader(CACHE_FILE),
                    CheckUpdate.class);

            updateData(data);
        } catch (FileNotFoundException ignored) {
        }
    }

    private void updateData(CheckUpdate data) {
        if (data == null) {
            return;
        }

        addMessageCard(data.getMessages());
        addUpdateCard(data.getUpdate());
        checkDataUpdate(data.getData());
    }

    private void refresh() {
        saveReadStatus();

        mSwipeRefreshLayout.setRefreshing(true);

        UpdateCheck.instance().check(getContext(), new Callback<CheckUpdate>() {
            @Override
            public void onResponse(Call<CheckUpdate> call, final Response<CheckUpdate> response) {
                if (getContext() == null) {
                    Log.d("HomeFragment", "onResponse context == null");
                    return;
                }

                if (response.body() == null) {
                    onFailure(call, new NullPointerException());
                }

                updateData(response.body());

                UpdateCheck.instance().recycle();
                mSwipeRefreshLayout.setRefreshing(false);

                Gson gson = new Gson();
                Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(response.body()).getBytes()),
                        CACHE_FILE);
            }

            @Override
            public void onFailure(Call<CheckUpdate> call, Throwable t) {
                UpdateCheck.instance().recycle();
                mSwipeRefreshLayout.setRefreshing(false);

                showSnackbar(R.string.refresh_fail, Snackbar.LENGTH_SHORT);
            }
        });
    }

    private void checkDataUpdate(List<CheckUpdate.DataEntity> data) {
        if (data == null) {
            return;
        }

        final StringBuilder sb = new StringBuilder();

        if (mDataUpdateCardView == null) {
            mDataUpdateCardView = new ButtonCardView(getContext());
            mDataUpdateCardView.setTitle("数据更新");
            mDataUpdateCardView.addButton(R.string.got_it);
        }

        Observable
                .from(data)
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<CheckUpdate.DataEntity>() {
                    @Override
                    public void onNext(CheckUpdate.DataEntity dataEntity) {
                        File file = new File(getContext().getFilesDir().getAbsolutePath() + "/json/" + dataEntity.getName());

                        long savedVersion = Settings
                                .instance(getContext())
                                .getLong(dataEntity.getName(), DATA_JSON_VERSION.get(dataEntity.getName()));
                        long builtInVersion = DATA_JSON_VERSION.get(dataEntity.getName());
                        long latestVersion = dataEntity.getVersion();

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
                                    .putLong(dataEntity.getName(), dataEntity.getVersion());

                            String name = dataEntity.getName().replace(".json", "");
                            Class<?> c;
                            c = Class.forName(String.format(
                                    "rikka.akashitoolkit.staticdata.%sList",
                                    name));
                            c.getMethod("clear").invoke(null);
                            //c.getMethod("get").invoke(null, getContext());

                            BusProvider.instance().post(new DataChangedAction(name + "Fragment"));

                            sb.append(DATA_JSON_NAME.get(dataEntity.getName())).append("已更新 (").append(dataEntity.getData()).append(") \n");

                            Log.d("DownloadData", dataEntity.getName() + " version: " + Long.toString(dataEntity.getVersion()));
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
    }

    private void addMessageCard(List<CheckUpdate.MessagesEntity> list) {
        if (list == null) {
            return;
        }

        for (final CheckUpdate.MessagesEntity entity :
                list) {
            int id = (entity.getTitle() + entity.getMessage()).hashCode();

            if (isIdRead(id)) {
                continue;
            }

            ButtonCardView card = mMessageCardView.get(id);

            if (card == null) {
                card = new ButtonCardView(getContext());
            } else {
                card.removeButtons();
            }

            card.setTitle(entity.getTitle());

            if (!((entity.getType() & NOT_DISMISSIBLE) > 0)) {
                card.addButton(R.string.got_it);
            }

            boolean isHtml = false;
            if ((entity.getType() & HTML_CONTENT) > 0) {
                isHtml = true;
            }

            if ((entity.getType() & COUNT_DOWN) > 0) {
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }

                final ButtonCardView finalCard = card;
                final boolean finalIsHtml = isHtml;
                final String format = entity.getMessage();
                mCountDownTimer = new CountDownTimer(
                        entity.getTime() * DateUtils.SECOND_IN_MILLIS - System.currentTimeMillis(), 1000) {

                    public void onTick(long millisUntilFinished) {
                        finalCard.setMessage(String.format(format, formatLeftTime(millisUntilFinished)), finalIsHtml);
                    }

                    public void onFinish() {
                        finalCard.setMessage("done!");
                    }
                }.start();
            } else {
                card.setMessage(entity.getMessage(), isHtml);
            }

            if ((entity.getType() & ACTION_VIEW_BUTTON) > 0) {
                card.addButton(entity.getAction_name() != null ? entity.getAction_name() : getContext().getString(R.string.open_link), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getLink())));
                    }
                }, true, false);
            }

            Log.d("HomeFragment", String.format("[add card] title: %s id: %d type: %d", entity.getTitle(), id, entity.getType()));
            addCard(card, id);
        }
    }

    private void addUpdateCard(final CheckUpdate.UpdateEntity entity) {
        if (BuildConfig.isGooglePlay) {
            return;
        }

        if (entity == null) {
            return;
        }

        int versionCode = StaticData.instance(getContext()).versionCode;

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
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatLeftTime(long time) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d 天", time / DateUtils.DAY_IN_MILLIS));
        time = time % DateUtils.DAY_IN_MILLIS;


        sb.append(String.format(" %d 小时", time / DateUtils.HOUR_IN_MILLIS));
        time = time % DateUtils.HOUR_IN_MILLIS;

        sb.append(String.format(" %d 分钟", time / DateUtils.MINUTE_IN_MILLIS));
        time = time % DateUtils.MINUTE_IN_MILLIS;

        sb.append(String.format(" %d 秒", time / DateUtils.SECOND_IN_MILLIS));

        return sb.toString();
    }

    @Subscribe
    public void readStatusReset(ReadStatusResetAction action) {
        /*int count = mLinearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mLinearLayout.getChildAt(i);
            if (view instanceof ButtonCardView) {
                view.setVisibility(View.VISIBLE);
                view.setTranslationX(0);
            }
        }*/

        if (mMessageReadStatus != null) {
            mMessageReadStatus.setMessageId(new ArrayList<Integer>());
        }

        refresh();
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
            mDataUpdateCardView = null;
            mMessageCardView.clear();

            addLocalCard();
            refresh();
        }
    }
}
