package rikka.akashitoolkit.ui.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Seasonal;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.support.Statistics;
import rikka.akashitoolkit.ui.ImageDisplayActivity;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.support.MusicPlayer;
import rikka.akashitoolkit.utils.MySpannableFactory;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/4/30.
 */
public class SeasonalFragment extends Fragment {
    private static final int TYPE_GALLERY = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_VOICE = 2;
    private static final int TYPE_SUBTITLE = 3;

    private static final String JSON_NAME = "/json/seasonal.json";
    private String CACHE_FILE;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        CACHE_FILE = getContext().getCacheDir().getAbsolutePath() + JSON_NAME;
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

        loadFromCache();

        return view;
    }

    private void loadFromCache() {
        Seasonal data;
        try {
            Gson gson = new Gson();
            data = gson.fromJson(
                    new FileReader(CACHE_FILE),
                    Seasonal.class);

            updateData(data);
        } catch (FileNotFoundException ignored) {
        }
    }

    private void updateData(Seasonal data) {
        mSwipeRefreshLayout.setRefreshing(false);
        setAdapter(data);
    }

    private void refresh() {
        Log.d("SeasonalFragment", "refreshing");

        mSwipeRefreshLayout.setRefreshing(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.minamion.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.SeasonalAPI service = retrofit.create(RetrofitAPI.SeasonalAPI.class);
        Call<Seasonal> call = service.get(2);

        call.enqueue(new Callback<Seasonal>() {
            @Override
            public void onResponse(Call<Seasonal> call, Response<Seasonal> response) {
                if (getContext() == null) {
                    return;
                }

                updateData(response.body());

                Gson gson = new Gson();
                Utils.saveStreamToFile(new ByteArrayInputStream(gson.toJson(response.body()).getBytes()),
                        CACHE_FILE);
            }

            @Override
            public void onFailure(Call<Seasonal> call, Throwable t) {
                Log.d("SeasonalFragment", "refresh failed");
                if (getContext() == null) {
                    return;
                }

                mSwipeRefreshLayout.setRefreshing(false);
                //Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_SHORT).show();
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

        class ViewHolderText extends RecyclerView.ViewHolder {
            protected TextView mText;

            public ViewHolderText(View itemView) {
                super(itemView);

                mText = (TextView) itemView.findViewById(android.R.id.title);

                mText.setMovementMethod(new LinkMovementMethod());
                mText.setClickable(true);
                mText.setSpannableFactory(MySpannableFactory.getInstance());
            }
        }

        private static final int TYPE_TITLE = 0;
        private static final int TYPE_SUBTITLE = 1;
        private static final int TYPE_ITEM_IMAGE = 2;
        private static final int TYPE_ITEM_VOICE = 3;
        private static final int TYPE_ITEM_TEXT = 4;

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
                case TYPE_ITEM_TEXT:
                    return new ViewHolderText(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_seasonal, parent, false));
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
                case TYPE_ITEM_TEXT:
                    Spanned htmlDescription = Html.fromHtml((String) mData.get(position));
                    String descriptionWithOutExtraSpace = htmlDescription.toString().trim();
                    ((ViewHolderText) holder).mText.setText(htmlDescription.subSequence(0, descriptionWithOutExtraSpace.length()));
                    break;
                case TYPE_ITEM_IMAGE:
                    setIllustrationContainer(
                            ((ViewHolderImage) holder).mContainer,
                            (Seasonal.DataEntity.ContentEntity) mData.get(position));
                    break;
                case TYPE_ITEM_VOICE:
                    final ShipVoice voice = (ShipVoice) mData.get(position);
                    ((ViewHolderVoice) holder).mContent.setText(voice.getJp());
                    ((ViewHolderVoice) holder).mContent2.setText(voice.getZh());

                    ((ViewHolderVoice) holder).mScene.setText(voice.getScene());

                    ((ViewHolderVoice) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Log.d("VoicePlay", "url " + voice.getUrl());
                                MusicPlayer.play(voice.getUrl());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

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

        /*if (!isHidden()) {
            activity.getSupportActionBar().setTitle(mTitle);
        }*/

        for (Seasonal.DataEntity data: body.getData()) {

            mAdapter.addData(Adapter.TYPE_TITLE, data.getTitle());

            for (Seasonal.DataEntity.ContentEntity content :
                    data.getContent()) {
                switch (content.getType()) {
                    case TYPE_GALLERY:
                        mAdapter.addData(Adapter.TYPE_SUBTITLE, content.getTitle());
                        mAdapter.addData(Adapter.TYPE_ITEM_IMAGE, content);
                        break;
                    case TYPE_TEXT:
                        mAdapter.addData(Adapter.TYPE_SUBTITLE, content.getTitle());
                        mAdapter.addData(Adapter.TYPE_ITEM_TEXT, content.getText());
                        break;
                    case TYPE_SUBTITLE:
                        mAdapter.addData(Adapter.TYPE_SUBTITLE, content.getText());
                        break;
                    case TYPE_VOICE:
                        ShipVoice item = new ShipVoice();
                        item.setJp(content.getJp());
                        item.setZh(content.getZh());
                        item.setScene(content.getText());
                        item.setUrl(content.getUrl());
                        mAdapter.addData(Adapter.TYPE_ITEM_VOICE, item);
                        break;
                }
            }
        }

        mAdapter.notifyChanged();
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
                    .inflate(R.layout.ship_illustrations, container, false)
                    .findViewById(R.id.imageView);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dpToPx(content.getWidth()), Utils.dpToPx(content.getHeight()));
            lp.rightMargin = Utils.dpToPx(8);
            imageView.setPadding(Utils.dpToPx(8), Utils.dpToPx(8), Utils.dpToPx(8), Utils.dpToPx(8));
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.valueOf(content.getScale_type()));

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
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(imageView);
        }
    }
}
