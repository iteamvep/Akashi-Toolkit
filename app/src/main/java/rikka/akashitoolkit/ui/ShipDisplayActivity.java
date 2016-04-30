package rikka.akashitoolkit.ui;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.ExtraIllustration;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.network.NetworkUtils;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.ExtraIllustrationList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.staticdata.ShipVoiceExtraList;
import rikka.akashitoolkit.utils.KCStringFormatter;
import rikka.akashitoolkit.utils.MySpannableFactory;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/30.
 */
public class ShipDisplayActivity extends BaseItemDisplayActivity {
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mRecyclerView;
    private Ship mItem;
    private int mId;
    private Adapter mAdapter;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mId = -1;
        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM_ID)) {
            mId = intent.getIntExtra(EXTRA_ITEM_ID, 0);
        } else if (intent.getData() != null) {
            try {
                mId = Integer.parseInt(intent.getData().toString().split("/")[3]);
            } catch (Exception ignored) {
            }
        }

        mItem = ShipList.findItemById(this, mId);
        if (mItem == null) {
            Log.d("ShipDisplayActivity", "Ship not found? id=" + Integer.toString(mId));
            finish();
            return;
        }

        setContentView(R.layout.activity_ship_display);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.2ds.tv:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.Voice service = retrofit.create(RetrofitAPI.Voice.class);
        Call<ShipVoice> call = service.get(mItem.getId());

        Log.d("ShipDisplayActivity", "JKancolle/voice.do " + Integer.toString(mItem.getId()));

        call.enqueue(new Callback<ShipVoice>() {
            @Override
            public void onResponse(Call<ShipVoice> call, Response<ShipVoice> response) {
                mAdapter.setData(response.body().getData());
            }

            @Override
            public void onFailure(Call<ShipVoice> call, Throwable t) {

            }
        });

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        setViews();
    }

    private class Adapter extends RecyclerView.Adapter {
        class ViewHolderHead extends RecyclerView.ViewHolder {
            public ViewHolderHead(View itemView) {
                super(itemView);
            }
        }

        class ViewHolderHead2 extends RecyclerView.ViewHolder {
            protected TextView mTitle;
            public ViewHolderHead2(View itemView) {
                super(itemView);

                mTitle = (TextView) itemView.findViewById(android.R.id.title);
            }
        }

        class ViewHolderItem extends RecyclerView.ViewHolder {
            protected TextView mScene;
            protected TextView mTitle;
            protected TextView mContent;
            protected TextView mContent2;
            protected LinearLayout mLinearLayout;

            public ViewHolderItem(View itemView) {
                super(itemView);

                mTitle = (TextView) itemView.findViewById(R.id.textView);
                mScene = (TextView) itemView.findViewById(android.R.id.title);
                mContent = (TextView) itemView.findViewById(R.id.text_content);
                mContent2 = (TextView) itemView.findViewById(R.id.text_content2);
                mLinearLayout = (LinearLayout) itemView.findViewById(R.id.content_container);
            }
        }

        class Voice {
            String type;
            ShipVoice.DataEntity.VoiceEntity voice;

            public Voice(String type, ShipVoice.DataEntity.VoiceEntity voice) {
                this.type = type;
                this.voice = voice;
            }
        }

        private static final int TYPE_HEAD = 0;
        private static final int TYPE_HEAD2 = 1;
        private static final int TYPE_ITEM = 2;

        private List<Voice> mData;

        public Adapter() {
            mData = new ArrayList<>();
        }

        public void setData(List<ShipVoice.DataEntity> data) {
            mData.clear();

            // so bad
            Ship cur = mItem;
            while (cur.getRemodel().getId_from() != 0) {
                cur = ShipList.findItemById(ShipDisplayActivity.this, cur.getRemodel().getId_from());
            }

            ShipVoice v = ShipVoiceExtraList.get(ShipDisplayActivity.this);
            for (ShipVoice.DataEntity entity : v.getData()) {
                for (ShipVoice.DataEntity.VoiceEntity voice : entity.getVoice()) {
                    if (entity.getZh().equals("%E8%8F%8A%E6%B0%B4%E7%89%B9%E6%94%BB") || entity.getZh().equals("%E6%9C%80%E5%90%8E%E4%B8%80%E6%AC%A1%E5%87%BA%E5%87%BB")) {
                        continue;
                    }

                    if (cur.getWiki_id().equals(voice.getIndex())) {
                        voice.setScene("");
                        mData.add(new Voice("2016年三周年纪念", voice));
                    }
                }
            }

            for (ShipVoice.DataEntity entity : data) {
                for (ShipVoice.DataEntity.VoiceEntity voice : entity.getVoice()) {
                    mData.add(new Voice(entity.getZh(), voice));
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                    return TYPE_HEAD;
                case 1:
                    return TYPE_HEAD2;
                default:
                    return TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_HEAD:
                    LinearLayout linearLayout = new LinearLayout(parent.getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    return new ViewHolderHead(linearLayout);
                case TYPE_HEAD2:
                    return new ViewHolderHead2(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_display_cell, parent, false));
                default:
                    return new ViewHolderItem(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_voice, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_HEAD:
                    setView((LinearLayout) holder.itemView);
                    break;
                case TYPE_HEAD2:
                    ((ViewHolderHead2) holder).mTitle.getLayoutParams().height = Utils.dpToPx(48);
                    ((ViewHolderHead2) holder).mTitle.setText(R.string.voice);
                    break;
                default:
                    try {
                        final Voice item = mData.get(position - 2);

                        if (position == 2 || !mData.get(position - 3).type.equals(item.type)) {
                            ((ViewHolderItem) holder).mTitle.setVisibility(View.VISIBLE);
                            ((ViewHolderItem) holder).mTitle.setText(
                                    URLDecoder.decode(item.type, "UTF-8")
                            );
                        } else {
                            ((ViewHolderItem) holder).mTitle.setVisibility(View.GONE);
                        }

                        /*if (item.voice.getScene().length() == 0) {
                            ((ViewHolderItem) holder).mScene.setVisibility(View.GONE);
                        } else {
                            ((ViewHolderItem) holder).mScene.setVisibility(View.VISIBLE);
                        }*/

                        ((ViewHolderItem) holder).mScene.setText(
                                URLDecoder.decode(item.voice.getScene(), "UTF-8"));
                        ((ViewHolderItem) holder).mContent.setText(
                                URLDecoder.decode(item.voice.getJaSub(), "UTF-8"));
                        ((ViewHolderItem) holder).mContent2.setText(
                                URLDecoder.decode(item.voice.getZhSub(), "UTF-8"));

                        ((ViewHolderItem) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    String url = URLDecoder.decode(item.voice.getUrl(), "UTF-8");
                                    Log.d("VoicePlay", "url " + url);
                                    Uri uri = Uri.parse(url);
                                    String filename = uri.getLastPathSegment();
                                    Log.d("VoicePlay", "filename " + filename);

                                    final String path = getCacheDir().getAbsolutePath() + "/" + filename;
                                    File file = new File(path);

                                    if (file.exists()) {
                                        Log.d("VoicePlay", "play exists file " + filename);
                                        playMusic(path);
                                    } else {
                                        Log.d("VoicePlay", "download file " + filename);

                                        NetworkUtils.get(url, new okhttp3.Callback() {
                                            @Override
                                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                                Log.d("VoicePlay", "download file finished");

                                                Utils.saveStreamToFile(response.body().byteStream(), path);

                                                playMusic(path);
                                            }

                                            @Override
                                            public void onFailure(okhttp3.Call call, IOException e) {

                                            }
                                        });
                                    }

                                    /*MediaPlayer mp = new MediaPlayer();
                                    mp.setDataSource(url);
                                    mp.prepare();
                                    mp.start();*/
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mData.size() == 0 ? 1 : mData.size() + 2;
        }
    }

    private String lastPlayed;

    private void playMusic(String path) throws IOException {
        stopMusic();

        if (lastPlayed != null && path.equals(lastPlayed)) {
            lastPlayed = null;
            return;
        }

        lastPlayed = path;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                lastPlayed = null;
            }
        });
    }

    private void stopMusic() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }

            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        stopMusic();

        super.onStop();
    }

    @Override
    protected String getTaskDescriptionLabel() {
        return mItem.getName().get(this);
    }

    private void setViews() {
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle(
                String.format("No.%s %s",
                        mItem.getWiki_id(),
                        ShipList.shipType[mItem.getType()]));

        if (Utils.isNightMode(getResources())) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp);
            mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
            mToolbar.setSubtitleTextColor(Color.parseColor("#DEFFFFFF"));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_24dp_dark);
            mToolbar.setTitleTextColor(Color.parseColor("#000000"));
            mToolbar.setSubtitleTextColor(Color.parseColor("#ff757575"));
        }

        if (mItem.getName() != null) {
            getSupportActionBar().setTitle(mItem.getName().get(this));
        }
    }

    private void setView(LinearLayout linearLayout) {
        if (linearLayout.getChildCount() > 0) {
            return;
        }

        TabLayout tabLayout = new TabLayout(this);
        linearLayout.addView(tabLayout);

        ViewPager viewPager = (ViewPager) LayoutInflater.from(this).inflate(R.layout.content_viewpager, linearLayout, true).findViewById(R.id.view_pager);
        //ViewPager viewPager = new ViewPager(this);
        viewPager.setPadding(0, Utils.dpToPx(4), 0, 0);
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(32) * 6 + Utils.dpToPx(16)));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", position);
                bundle.putInt("ITEM", mId);
                return bundle;
            }
        };
        adapter.addFragment(AttrFragment.class, getString(R.string.initial));
        adapter.addFragment(AttrFragment.class, "Lv.99");
        adapter.addFragment(AttrFragment.class, "LV.155");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimaryItemActivity));

        addEquip(linearLayout);
        addRemodel(linearLayout);
        addIllustration(linearLayout);
    }

    private void addIllustration(ViewGroup parent) {
        parent = addCell(parent, R.string.illustration);

        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_illustrations_container, parent);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.content_container);

        final List<String> urlList = new ArrayList<>();
        urlList.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sIllust.png", mItem.getWiki_id().replace("a", ""))));
        urlList.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sDmgIllust.png", mItem.getWiki_id().replace("a", ""))));

        ExtraIllustration extraIllustration = ExtraIllustrationList.findItemById(this, mItem.getWiki_id());
        if (extraIllustration != null) {
            for (String name :
                    extraIllustration.getImage()) {
                urlList.add(Utils.getKCWikiFileUrl(name));
            }
        }

        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);

            Log.d(getClass().getSimpleName(), url);

            ImageView imageView = (ImageView) LayoutInflater.from(this)
                    .inflate(R.layout.item_illustrations, container, false)
                    .findViewById(R.id.imageView);
            container.addView(imageView);

            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShipDisplayActivity.this, ImageDisplayActivity.class);
                    intent.putStringArrayListExtra(ImageDisplayActivity.EXTRA_URL, (ArrayList<String>) urlList);
                    intent.putExtra(ImageDisplayActivity.EXTRA_POSITION, finalI);
                    intent.putExtra(ImageDisplayActivity.EXTRA_TITLE, getTaskDescriptionLabel());
                    startActivity(intent);
                }
            });

            /*if (Settings.instance(this).getBoolean(Settings.DOWNLOAD_WIFI_ONLY, false)) {
                Glide.with(this)
                        .using(GlideHelper.cacheOnlyStreamLoader)
                        .load(url)
                        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                        .crossFade()
                        .into(imageView);
            } else */{
                Glide.with(this)
                        .load(Utils.getGlideUrl(url))
                        .crossFade()
                        .into(imageView);
            }

        }
    }

    private void addRemodel(ViewGroup parent) {
        if (mItem.getRemodel() != null) {
            parent = addCell(parent, R.string.remodel);
            GridLayout gridLayout = new GridLayout(this);
            gridLayout.setColumnCount(2);

            //StringBuilder sb = new StringBuilder();
            Ship cur = mItem;
            while (cur.getRemodel().getId_from() != 0) {
                cur = ShipList.findItemById(this, cur.getRemodel().getId_from());
            }

            while (true) {
                //sb.append(KCStringFormatter.getLinkShip(cur.getId(), cur.getName().get(this)));
                StringBuilder sb = new StringBuilder();
                sb.append(cur.getName().get(this));
                ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_remodel_item, null);
                view.setLayoutParams(
                        new GridLayout.LayoutParams(
                                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                                GridLayout.spec(GridLayout.UNDEFINED, 1f)
                        )
                );
                gridLayout.addView(view);

                final Ship finalCur = cur;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShipDisplayActivity.this, ShipDisplayActivity.class);
                        intent.putExtra(ShipDisplayActivity.EXTRA_ITEM_ID, finalCur.getId());
                        startActivity(intent);
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("akashitoolkit://ship/" + Integer.toString(finalCur.getId()))));
                    }
                });

                if (cur.getRemodel().getId_from() != 0) {
                    Ship prev = ShipList.findItemById(this, cur.getRemodel().getId_from());
                    if (prev.getRemodel().getBlueprint() == 0) {
                        sb.append(String.format(" (%d)", prev.getRemodel().getLevel()));
                    } else {
                        sb.append(String.format(" (%d + 改装设计图)", prev.getRemodel().getLevel()));
                    }
                }

                ((TextView) view.findViewById(android.R.id.title)).setText(sb.toString());

                if (cur.getRemodel().getId_to() == 0 ||
                        cur.getRemodel().getId_from() == cur.getRemodel().getId_to()) {

                    view.findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
                    break;
                }

                cur = ShipList.findItemById(this, cur.getRemodel().getId_to());
                if (cur.getRemodel().getId_from() != cur.getRemodel().getId_to()) {
                    //sb.append(" → ");
                } else {
                    //sb.append(" ↔ ");
                    ((ImageView) view.findViewById(R.id.imageView)).setImageResource(R.drawable.ic_compare_arrows_black_24dp);
                }


            }
            //addTextView(parent, Html.fromHtml(sb.toString()));
            parent.addView(gridLayout);
        }
    }

    private void addEquip(ViewGroup parent) {
        parent = addCell(parent, R.string.equip_and_load);

        List<Integer> equipId = mItem.getEquip().get(0);
        List<Integer> equipSlot = mItem.getEquip().get(1);

        for (int i = 0; i < mItem.getSlot(); i++) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.ship_equip, parent, false);

            if (equipId.get(i) > 0) {
                final Equip item = EquipList.findItemById(this, equipId.get(i));
                if (item == null) {
                    ((TextView) view.findViewById(android.R.id.title)).setText(String.format(getString(R.string.equip_not_found), equipId.get(i)));
                    view.findViewById(android.R.id.title).setEnabled(false);
                } else {
                    ((TextView) view.findViewById(android.R.id.title)).setText(item.getName().get(this));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ShipDisplayActivity.this, EquipDisplayActivity.class);
                            intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                            startActivity(intent);
                        }
                    });
                }
            } else {
                ((TextView) view.findViewById(android.R.id.title)).setText(getString(R.string.not_equipped));
                view.findViewById(android.R.id.title).setEnabled(false);
            }

            ((TextView) view.findViewById(R.id.textView)).setText(Integer.toString(equipSlot.get(i)));

            parent.addView(view);
        }
    }

    private ViewGroup addCell(ViewGroup parent, String title) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.content_item_display_cell, parent, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(title);
        parent.addView(view);
        return view;
    }

    private ViewGroup addCell(ViewGroup parent, int ResId) {
        return addCell(parent, getString(ResId));
    }

    private TextView addTextView(ViewGroup parent, String text, int size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        parent.addView(textView);
        return textView;
    }

    private TextView addTextView(ViewGroup parent, String text) {
        return addTextView(parent, text, 16);
    }

    private TextView addTextView(ViewGroup parent, Spanned text, int size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        textView.setMovementMethod(new LinkMovementMethod());
        textView.setClickable(true);
        textView.setSpannableFactory(MySpannableFactory.getInstance());
        //textView.setLinkTextColor(getColor(R.color.material_red_300));
        parent.addView(textView);
        return textView;
    }

    private TextView addTextView(ViewGroup parent, Spanned text) {
        return addTextView(parent, text, 16);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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

    public static class AttrFragment extends Fragment {
        private Ship mItem;
        private LinearLayout mLinearLayout;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int i = args.getInt("TYPE");
            int id = args.getInt("ITEM");

            mLinearLayout = new LinearLayout(getContext());
            mLinearLayout.setOrientation(LinearLayout.VERTICAL);
            mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLinearLayout.setPadding(0, Utils.dpToPx(4), 0, 0);
            mItem = ShipList.findItemById(getContext(), id);
            addAttrView(mLinearLayout, "耐久", mItem.getAttr().getHp(), i == 2 ? 2 : 0, R.drawable.item_attr_hp);
            addAttrView(mLinearLayout, "火力", mItem.getAttr().getFire(), i, R.drawable.item_attr_fire);
            addAttrView(mLinearLayout, "对空", mItem.getAttr().getAa(), i, R.drawable.item_attr_aa);
            addAttrView(mLinearLayout, "雷装", mItem.getAttr().getTorpedo(), i, R.drawable.item_attr_torpedo);
            addAttrView(mLinearLayout, "装甲", mItem.getAttr().getArmor(), i, R.drawable.item_attr_armor);
            addAttrView(mLinearLayout, "对潜", mItem.getAttr().getAsw(), i, R.drawable.item_attr_asw);
            addAttrView(mLinearLayout, "回避", mItem.getAttr().getEvasion(), i, R.drawable.item_attr_dodge);
            addAttrView(mLinearLayout, "索敌", mItem.getAttr().getSearch(), i, R.drawable.item_attr_search);
            addAttrView(mLinearLayout, "航速", mItem.getAttr().getSpeed(), R.drawable.item_attr_speed);
            addAttrView(mLinearLayout, "射程", mItem.getAttr().getRange(), R.drawable.item_attr_range);
            addAttrView(mLinearLayout, "运", mItem.getAttr().getLuck(), i, R.drawable.item_attr_luck);
            /*attr = 0;
            mCurAttrLinearLayout = null;
            addAttrView(mLinearLayout, "燃料消耗", mItem.getRemodel().getCost().get(0), 0);
            addAttrView(mLinearLayout, "弹药消耗", mItem.getRemodel().getCost().get(1), 0);*/

            return mLinearLayout;
        }

        private void addAttrView(ViewGroup parent, String title, List<Integer> value, int i, int icon) {
            if (value.size() <= i) {
                i = value.size() - 1;
            }

            if (value.size() == 2) {
                addAttrView(parent, title, String.format("%d / %d", value.get(0), value.get(1)), icon);
            } else {
                addAttrView(parent, title, Integer.toString(value.get(i)), icon);
            }
        }

        private void addAttrView(ViewGroup parent, String title, int value, int icon) {
            if (value == 0) {
                return;
            }
            if (icon == R.drawable.item_attr_range) {
                addAttrView(parent, title, KCStringFormatter.getRange(value), icon);
            } else if (icon == R.drawable.item_attr_speed) {
                addAttrView(parent, title, KCStringFormatter.getSpeed(value), icon);
            } else {
                addAttrView(parent, title, Integer.toString(value), icon);
            }
        }

        private LinearLayout mCurAttrLinearLayout;
        private int attr = 0;

        private void addAttrView(ViewGroup parent, String title, String value, int icon) {
            attr ++;

            if (mCurAttrLinearLayout == null) {
                mCurAttrLinearLayout = new LinearLayout(getContext());
                mCurAttrLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                mCurAttrLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dpToPx(32)));
                mCurAttrLinearLayout.setBaselineAligned(false);
                mCurAttrLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
                LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_attr_cell, mCurAttrLinearLayout);
                parent.addView(mCurAttrLinearLayout);
            }

            View cell = mCurAttrLinearLayout
                    .findViewById((attr % 2 == 0) ? R.id.item_attr_cell2 : R.id.item_attr_cell);

            cell.setVisibility(View.VISIBLE);

            ((TextView) cell.findViewById(R.id.textView)).setText(title);
            ((TextView) cell.findViewById(R.id.textView2)).setText(value);
            //((ImageView) cell.findViewById(R.id.imageView)).setImageDrawable(ContextCompat.getDrawable(getContext(), icon));

            if (attr % 2 == 0) {
                mCurAttrLinearLayout = null;
            }
        }
    }
}
