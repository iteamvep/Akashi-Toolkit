package rikka.akashitoolkit.ship_detail;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.model.ShipClass;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.staticdata.ShipClassList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.staticdata.Subtitle;
import rikka.akashitoolkit.support.MusicPlayer;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.support.StaticData;
import rikka.akashitoolkit.tools.SendReportActivity;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;
import rikka.akashitoolkit.ui.widget.LinearLayoutManager;
import rikka.akashitoolkit.utils.FlavorsUtils;
import rikka.akashitoolkit.utils.NetworkUtils;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipDetailActivity extends BaseItemDisplayActivity {

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private static final String TAG = "ShipDetailActivity";

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mRecyclerView;
    private Ship mItem;
    private int mId;
    private ShipDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = -1;
        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            id = intent.getIntExtra(EXTRA_ITEM_ID, 0);
        } else if (intent.getData() != null) {
            try {
                id = Integer.parseInt(intent.getData().toString().split("/")[3]);
            } catch (Exception ignored) {
            }
        }

        if (getIntent().getBooleanExtra(EXTRA_FROM_NOTIFICATION, false)) {
            String extra = getIntent().getStringExtra(EXTRA_EXTRA);
            if (extra != null) {
                try {
                    id = Integer.parseInt(extra);
                } catch (Exception ignored) {
                }
            }
        }

        setContentView(R.layout.activity_ship_display);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = Utils.dpToPx(16);
                }
            }
        });

        setItem(id, null);

        setupAppbar();

        //mCoordinatorLayout.setBackgroundColor(ContextCompat.getColor(ShipDetailActivity.this, R.color.windowBackground));
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(ShipDetailActivity.this, R.color.background));
        mAppBarLayout.setBackgroundColor(ContextCompat.getColor(ShipDetailActivity.this, R.color.background));

    }

    private void setItem(int id, int[] location) {
        if (id == mId) {
            return;
        }

        Ship item = ShipList.findItemById(this, id);

        if (item == null) {
            Log.d("ShipDetailActivity", "Ship not found? id=" + Integer.toString(mId));
            finish();
            return;
        }

        setItem(item, location);
    }

    @SuppressLint("DefaultLocale")
    private void setToolbarTitle() {
        if (mItem.getName() != null) {
            ShipClass shipClass = ShipClassList.findItemById(this, mItem.getClassType());

            String c = "";
            if (shipClass != null) {
                c = String.format("%s%s号舰", shipClass.getName(), Utils.getChineseNumberString(mItem.getClassNum()));
            } else {
                Log.d("ShipDetailActivity", "No ship class: " + mItem.getName().get());
            }
            ((TextView) mToolbar.findViewById(android.R.id.title)).setText(mItem.getName().get());
            ((TextView) mToolbar.findViewById(android.R.id.summary)).setText(String.format("No.%s %s",
                    mItem.getWikiId(),
                    c/*,
                    ShipList.shipType[mItem.getType()]*/));
        }
    }

    private static final int FADE_IN = 150;
    private static final int FADE_OUT = 250;

    private void setItem(Ship item, final int[] location) {
        if (mAdapter == null) {
            mAdapter = new ShipDetailAdapter(this, item, new ShipDetailRemodelViewHolder.OnItemClickListener() {
                @Override
                public void onClick(View v, int shipId) {
                    int[] location = new int[2];
                    v.getLocationInWindow(location);
                    location[0] += v.getWidth() / 2;
                    location[1] += v.getHeight() / 2;
                    setItem(shipId, location);
                }
            });

            mRecyclerView.setAdapter(mAdapter);
        }

        if (item.getId() == mId) {
            return;
        }

        mItem = item;
        mId = item.getId();

        downloadShipVoice();

        if (location == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setToolbarTitle();
        }

        if (location != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlphaAnimation animation = new AlphaAnimation(1, 0);
                animation.setDuration(FADE_IN);
                animation.setInterpolator(new FastOutSlowInInterpolator());
                mAppBarLayout.startAnimation(animation);

                if (!StaticData.instance(this).isTablet)
                    getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ShipDetailActivity.this, R.color.colorItemDisplayStatusBar)));
            }

            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(FADE_IN);
            animation.setFillAfter(true);
            animation.setInterpolator(new FastOutSlowInInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation _animation) {
                    _animation.cancel();

                    if (mItem.getName() != null) {
                        setToolbarTitle();
                    }

                    mAdapter.setItem(ShipDetailActivity.this, mItem);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        animateRevealColorFromCoordinates(mCoordinatorLayout, R.color.background, location[0], location[1], FADE_OUT, true);

                        AlphaAnimation animation = new AlphaAnimation(0, 1);
                        animation.setFillBefore(true);
                        animation.setDuration(FADE_OUT);
                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                        mRecyclerView.startAnimation(animation);
                        mAppBarLayout.startAnimation(animation);
                    } else {
                        AlphaAnimation animation = new AlphaAnimation(0, 1);
                        animation.setDuration(FADE_IN);
                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                        mRecyclerView.startAnimation(animation);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mRecyclerView.startAnimation(animation);
        }


    }

    private void downloadShipVoice() {
        if (FlavorsUtils.shouldSafeCheck() && FlavorsUtils.isPlay()) {
            return;
        }

        if (mItem.isEnemy()) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kcwiki.moe")
                .client(NetworkUtils.getClient(Subtitle.shouldUseCache()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d(TAG, "use cache " + Subtitle.shouldUseCache());

        RetrofitAPI.SubtitleAPI service = retrofit.create(RetrofitAPI.SubtitleAPI.class);
        Call<List<ShipVoice>> call = service.getDetail(mItem.getId());

        call.enqueue(new Callback<List<ShipVoice>>() {
            @Override
            public void onResponse(Call<List<ShipVoice>> call, Response<List<ShipVoice>> response) {
                if (response.code() >= 400) {
                    onFailure(call, new Throwable("" + response.code()));
                    return;
                }

                if (response.body() == null) {
                    return;
                }

                if (isFinishing()) {
                    return;
                }

                int start;
                for (start = 0; start < mAdapter.getItemCount(); start++) {
                    if (mAdapter.getItemViewType(start) == ShipDetailAdapter.TYPE_VOICE) {
                        break;
                    }
                }

                for (int i = start; i < mAdapter.getItemCount(); i++) {
                    mAdapter.removeItem(start);
                    mAdapter.notifyItemRemoved(start);
                }

                start = mAdapter.getItemCount();
                for (ShipVoice item : response.body()) {
                    mAdapter.addItem(ShipDetailAdapter.TYPE_VOICE, item);
                }

                mAdapter.notifyItemRangeChanged(start, response.body().size());
            }

            @Override
            public void onFailure(Call<List<ShipVoice>> call, Throwable t) {
                Toast.makeText(ShipDetailActivity.this, "get voice: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        MusicPlayer.stop();

        super.onStop();
    }

    @Override
    protected String getTaskDescriptionLabel() {
        return mItem.getName().get();
    }

    private void setupAppbar() {
        setToolBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (mItem.isEnemy()) {
            mToolbar.findViewById(android.R.id.icon).setVisibility(View.GONE);
            return;
        }

        mToolbar.findViewById(R.id.content_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(ShipDetailActivity.this, v);

                Ship cur = mItem;
                while (cur.getRemodel().getFromId() != 0) {
                    cur = ShipList.findItemById(v.getContext(), cur.getRemodel().getFromId());
                }
                popupMenu.getMenu().add(0, cur.getId(), 0, cur.getName().get());

                int i = 1;
                while (cur.getRemodel().getToId() != 0 &&
                        cur.getRemodel().getToId() != cur.getRemodel().getFromId()) {
                    cur = ShipList.findItemById(v.getContext(), cur.getRemodel().getToId());
                    popupMenu.getMenu().add(0, cur.getId(), i, cur.getName().get());
                    i++;
                }
                popupMenu.setGravity(Gravity.TOP);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int[] position = new int[]{Utils.dpToPx(48 + 4) + v.getWidth() / 2, mAppBarLayout.getHeight() / 2 + Utils.dpToPx(24)};
                        setItem(item.getItemId(), position);
                        return false;
                    }
                });
            }
        });
    }

    Toast mToast;

    @SuppressLint("DefaultLocale")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_bookmark:
                mItem.setBookmarked(!mItem.isBookmarked());

                Settings.instance(this)
                        .putBoolean(String.format("ship_%d_%d", mItem.getClassType(), mItem.getClassNum()), mItem.isBookmarked());

                if (mToast != null) {
                    mToast.cancel();
                }

                mToast = Toast.makeText(this, mItem.isBookmarked() ? getString(R.string.bookmark_add) : getString(R.string.bookmark_remove), Toast.LENGTH_SHORT);
                mToast.show();

                item.setIcon(
                        AppCompatDrawableManager.get().getDrawable(this, mItem.isBookmarked() ? R.drawable.ic_bookmark_24dp : R.drawable.ic_bookmark_border_24dp));

                break;
            case R.id.action_feedback:
                SendReportActivity.sendEmail(this,
                        "Akashi Toolkit 舰娘数据反馈",
                        String.format("应用版本: %d\n舰娘名称: %s\n\n请写下您的建议或是指出错误的地方。\n\n",
                                StaticData.instance(this).versionCode,
                                getTaskDescriptionLabel()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected ViewGroup getRootView() {
        return mCoordinatorLayout;
    }

    @Override
    protected View[] getAnimFadeViews() {
        return new View[] {
                mAppBarLayout,
                mRecyclerView
        };
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, @ColorRes int color, int x, int y, long duration, boolean expand) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim;
        if (expand) {
            anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        } else {
            anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, finalRadius, 0);
        }

        viewRoot.setBackgroundColor(ContextCompat.getColor(this, color));
        anim.setDuration(duration);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ship_display, menu);

        if (!mItem.isEnemy()) {
            menu.findItem(R.id.action_bookmark).setIcon(
                    AppCompatDrawableManager.get().getDrawable(this, mItem.isBookmarked() ? R.drawable.ic_bookmark_24dp : R.drawable.ic_bookmark_border_24dp));
        } else {
            menu.findItem(R.id.action_bookmark).setVisible(false);
        }

        return true;
    }
}
