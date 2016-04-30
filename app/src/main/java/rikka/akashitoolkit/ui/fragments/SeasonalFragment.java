package rikka.akashitoolkit.ui.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import rikka.akashitoolkit.model.Seasonal;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.network.NetworkUtils;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.staticdata.ShipVoiceExtraList;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.ImageDisplayActivity;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/4/30.
 */
public class SeasonalFragment extends BaseFragment {
    private static final int TAB_LAYOUT_VISIBILITY = View.GONE;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    private MediaPlayer mMediaPlayer;
    private String mTitle;

    @Override
    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getSupportActionBar().setTitle(mTitle);
        activity.setRightDrawerLocked(true);

        Statistics.onFragmentStart("SeasonalFragment");
    }

    @Override
    public void onHide() {
        super.onHide();

        mSwipeRefreshLayout.setRefreshing(false);

        stopMusic();

        Statistics.onFragmentEnd("SeasonalFragment");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mTitle = getString(R.string.new_content);
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
        View view = inflater.inflate(R.layout.content_swipe_refresh, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPadding(0, Utils.dpToPx(4), 0, Utils.dpToPx(4));
        mRecyclerView.setClipToPadding(false);

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

        if (!isHiddenBeforeSaveInstanceState()) {
            onShow();
        }

        return view;
    }

    private void refresh() {
        Log.d("SeasonalFragment", "refreshing");

        mSwipeRefreshLayout.setRefreshing(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.minamion.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.SeasonalAPI service = retrofit.create(RetrofitAPI.SeasonalAPI.class);
        Call<Seasonal> call = service.get(1);

        call.enqueue(new Callback<Seasonal>() {
            @Override
            public void onResponse(Call<Seasonal> call, Response<Seasonal> response) {
                if (getContext() == null) {
                    return;
                }

                mSwipeRefreshLayout.setRefreshing(false);
                setAdapter(response.body());
            }

            @Override
            public void onFailure(Call<Seasonal> call, Throwable t) {
                Log.d("SeasonalFragment", "refresh failed");
                if (getContext() == null) {
                    return;
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter {
        class ViewHolderTitle extends RecyclerView.ViewHolder {
            protected TextView mTitle;

            public ViewHolderTitle(View itemView) {
                super(itemView);

                mTitle = (TextView) itemView.findViewById(android.R.id.title);
            }
        }

        class ViewHolderImage extends RecyclerView.ViewHolder {
            protected LinearLayout mContainer;

            public ViewHolderImage(View itemView) {
                super(itemView);
                setIsRecyclable(false);

                mContainer = (LinearLayout) itemView.findViewById(R.id.content_container);
            }
        }

        class ViewHolderVoice extends RecyclerView.ViewHolder {
            protected TextView mScene;
            protected TextView mContent;
            protected TextView mContent2;

            public ViewHolderVoice(View itemView) {
                super(itemView);

                mScene = (TextView) itemView.findViewById(android.R.id.title);
                mContent = (TextView) itemView.findViewById(R.id.text_content);
                mContent2 = (TextView) itemView.findViewById(R.id.text_content2);
            }
        }

        private static final int TYPE_TITLE = 0;
        private static final int TYPE_SUBTITLE = 1;
        private static final int TYPE_ITEM_IMAGE = 2;
        private static final int TYPE_ITEM_VOICE = 3;

        private List<Object> mData;
        private List<Integer> mType;
        private int count;

        public Adapter() {
            mData = new ArrayList<>();
            mType = new ArrayList<>();
            count = 0;
        }

        public void clearData() {
            mData.clear();
            mType.clear();
            notifyChanged();
        }

        public void addData(int type, Object data) {
            mType.add(type);
            mData.add(data);
        }

        public void notifyChanged() {
            count = mType.size();
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mType.get(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_SUBTITLE:
                case TYPE_TITLE:
                    return new ViewHolderTitle(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_display_cell, parent, false));
                case TYPE_ITEM_IMAGE:
                    return new ViewHolderImage(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.ship_illustrations_container, parent, false));
                case TYPE_ITEM_VOICE:
                    return new ViewHolderVoice(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_voice_seasonal, parent, false));
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_TITLE:
                    ((ViewHolderTitle) holder).mTitle.setText((String) mData.get(position));
                    ((ViewHolderTitle) holder).mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    ((ViewHolderTitle) holder).mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    if (position != 0)
                        ((ViewHolderTitle) holder).mTitle.getLayoutParams().height = Utils.dpToPx(48);
                    else
                        ((ViewHolderTitle) holder).mTitle.getLayoutParams().height = Utils.dpToPx(32);
                    break;
                case TYPE_SUBTITLE:
                    ((ViewHolderTitle) holder).mTitle.setText((String) mData.get(position));
                    ((ViewHolderTitle) holder).mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    //((ViewHolderTitle) holder).mTitle.getLayoutParams().height = Utils.dpToPx(32);
                    break;
                case TYPE_ITEM_IMAGE:
                    setIllustrationContainer(
                            ((ViewHolderImage) holder).mContainer,
                            (Seasonal.DataEntity.ContentEntity) mData.get(position));
                    break;
                case TYPE_ITEM_VOICE:
                    try {
                        final ShipVoice.DataEntity.VoiceEntity voice = (ShipVoice.DataEntity.VoiceEntity) mData.get(position);
                        ((ViewHolderVoice) holder).mContent.setText(URLDecoder.decode(voice.getJaSub(), "UTF-8"));
                        ((ViewHolderVoice) holder).mContent2.setText(URLDecoder.decode(voice.getZhSub(), "UTF-8"));

                        Ship ship = ShipList.findItemByWikiId(getContext(), voice.getIndex());
                        if (ship != null) {
                            ((ViewHolderVoice) holder).mScene.setText(ship.getName().get(getContext()));
                        } else {
                            ((ViewHolderVoice) holder).mScene.setText(voice.getIndex());
                        }

                        ((ViewHolderVoice) holder).itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    String url = URLDecoder.decode(voice.getUrl(), "UTF-8");
                                    Log.d("VoicePlay", "url " + url);
                                    Uri uri = Uri.parse(url);
                                    String filename = uri.getLastPathSegment();
                                    Log.d("VoicePlay", "filename " + filename);

                                    final String path = getContext().getCacheDir().getAbsolutePath() + "/" + filename;
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
            return count;
        }
    }

    private void setAdapter(Seasonal body) {
        mAdapter.clearData();

        MainActivity activity = ((MainActivity) getActivity());
        mTitle = body.getTitle();

        if (!isHidden()) {
            activity.getSupportActionBar().setTitle(mTitle);
        }

        for (Seasonal.DataEntity data: body.getData()) {

            mAdapter.addData(Adapter.TYPE_TITLE, data.getTitle());

            for (Seasonal.DataEntity.ContentEntity content :
                    data.getContent()) {
                switch (content.getType()) {
                    case 0:
                        mAdapter.addData(Adapter.TYPE_SUBTITLE, content.getTitle());
                        mAdapter.addData(Adapter.TYPE_ITEM_IMAGE, content);
                        break;
                }
            }
        }

        mAdapter.addData(Adapter.TYPE_TITLE, "2016年新增三周年语音");

        for (ShipVoice.DataEntity entity :
                ShipVoiceExtraList.get(getContext()).getData()) {

            try {
                mAdapter.addData(Adapter.TYPE_SUBTITLE, URLDecoder.decode(entity.getZh(), "UTF-8"));

                for (ShipVoice.DataEntity.VoiceEntity voice :
                        entity.getVoice()) {

                    mAdapter.addData(Adapter.TYPE_ITEM_VOICE, voice);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        mAdapter.notifyChanged();
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

    private void setIllustrationContainer(LinearLayout container, Seasonal.DataEntity.ContentEntity content) {
        if (container.getChildCount() > 0) {
            return;
        }

        final List<String> urlList = new ArrayList<>();

        for (String name :
                content.getFile()) {
            urlList.add(Utils.getKCWikiFileUrl(name));
        }

        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);

            Log.d(getClass().getSimpleName(), url);

            ImageView imageView = (ImageView) LayoutInflater.from(getContext())
                    .inflate(R.layout.item_illustrations, container, false)
                    .findViewById(R.id.imageView);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dpToPx(content.getWidth()), Utils.dpToPx(content.getHeight()));
            lp.rightMargin = Utils.dpToPx(8);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.valueOf(content.getSacle_type()));

            container.addView(imageView);

            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageDisplayActivity.class);
                    intent.putStringArrayListExtra(ImageDisplayActivity.EXTRA_URL, (ArrayList<String>) urlList);
                    intent.putExtra(ImageDisplayActivity.EXTRA_POSITION, finalI);
                    intent.putExtra(ImageDisplayActivity.EXTRA_TITLE, getString(R.string.app_name));
                    startActivity(intent);
                }
            });

            Glide.with(this)
                    .load(Utils.getGlideUrl(url))
                    .crossFade()
                    .into(imageView);
        }
    }
}
