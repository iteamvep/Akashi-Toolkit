package rikka.akashitoolkit.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import rikka.akashitoolkit.adapter.GalleryAdapter;
import rikka.akashitoolkit.model.Seasonal;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.ui.ImagesActivity;
import rikka.akashitoolkit.ui.MainActivity;
import rikka.akashitoolkit.support.MusicPlayer;
import rikka.akashitoolkit.utils.MySpannableFactory;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/4/30.
 */
public class SeasonalFragment extends Fragment {
    private static final int API_VERSION = 3;

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
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.windowBackground));

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
        Call<Seasonal> call = service.get(API_VERSION);

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
        class TitleHolder extends RecyclerView.ViewHolder {
            protected TextView mTitle;

            public TitleHolder(View itemView) {
                super(itemView);

                mTitle = (TextView) itemView.findViewById(android.R.id.title);
            }
        }

        class ImageHolder extends RecyclerView.ViewHolder {
            protected RecyclerView mContainer;

            public ImageHolder(View itemView) {
                super(itemView);
                setIsRecyclable(false);

                mContainer = (RecyclerView) itemView;
            }
        }

        class VoiceHolder extends RecyclerView.ViewHolder {
            protected TextView mScene;
            protected TextView mContent;
            protected TextView mContent2;

            public VoiceHolder(View itemView) {
                super(itemView);

                mScene = (TextView) itemView.findViewById(android.R.id.title);
                mContent = (TextView) itemView.findViewById(R.id.text_content);
                mContent2 = (TextView) itemView.findViewById(R.id.text_content2);
            }
        }

        class TextHolder extends RecyclerView.ViewHolder {
            protected TextView mText;

            public TextHolder(View itemView) {
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
                    return new TitleHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_display_cell, parent, false));
                case TYPE_ITEM_IMAGE:
                    return new ImageHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.illustrations_container, parent, false));
                case TYPE_ITEM_VOICE:
                    return new VoiceHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_voice_seasonal, parent, false));
                case TYPE_ITEM_TEXT:
                    return new TextHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_seasonal, parent, false));
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case TYPE_TITLE:
                    ((TitleHolder) holder).mTitle.setText((String) mData.get(position));
                    ((TitleHolder) holder).mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    ((TitleHolder) holder).mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    if (position != 0)
                        ((TitleHolder) holder).mTitle.getLayoutParams().height = Utils.dpToPx(48);
                    else
                        ((TitleHolder) holder).mTitle.getLayoutParams().height = Utils.dpToPx(32);
                    break;
                case TYPE_SUBTITLE:
                    ((TitleHolder) holder).mTitle.setText((String) mData.get(position));
                    ((TitleHolder) holder).mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    //((TitleHolder) holder).mTitle.getLayoutParams().height = Utils.dpToPx(32);
                    break;
                case TYPE_ITEM_TEXT:
                    Spanned htmlDescription = Html.fromHtml((String) mData.get(position));
                    String descriptionWithOutExtraSpace = htmlDescription.toString().trim();
                    ((TextHolder) holder).mText.setText(htmlDescription.subSequence(0, descriptionWithOutExtraSpace.length()));
                    break;
                case TYPE_ITEM_IMAGE:
                    setIllustrationContainer(
                            ((ImageHolder) holder).mContainer,
                            (Seasonal.DataEntity.ContentEntity) mData.get(position));
                    break;
                case TYPE_ITEM_VOICE:
                    final ShipVoice voice = (ShipVoice) mData.get(position);
                    ((VoiceHolder) holder).mContent.setText(voice.getJp());
                    ((VoiceHolder) holder).mContent2.setText(voice.getZh());

                    ((VoiceHolder) holder).mScene.setText(voice.getScene());

                    ((VoiceHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
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

    private void setIllustrationContainer(RecyclerView container, final Seasonal.DataEntity.ContentEntity content) {
        if (container.getChildCount() > 0) {
            return;
        }

        final List<String> urlList = new ArrayList<>();

        for (String name :
                content.getFile()) {
            urlList.add(Utils.getKCWikiFileUrl(name));
        }

        container.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                if (parent.getChildAdapterPosition(view) < parent.getAdapter().getItemCount() - 1) {
                    outRect.right = Utils.dpToPx(8);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        container.setLayoutManager(layoutManager);
        GalleryAdapter adapter = new GalleryAdapter() {
            @Override
            public void onItemClicked(View v, List<String> data, int position) {
                Context context = v.getContext();

                Intent intent = new Intent(context, ImagesActivity.class);
                intent.putStringArrayListExtra(ImagesActivity.EXTRA_URL, (ArrayList<String>) data);
                intent.putExtra(ImagesActivity.EXTRA_POSITION, position);
                intent.putExtra(ImagesActivity.EXTRA_TITLE, context.getString(R.string.app_name));
                context.startActivity(intent);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                ImageView imageView = (ImageView) holder.itemView;
                imageView.setLayoutParams(new LinearLayout.LayoutParams(Utils.dpToPx(content.getWidth()), Utils.dpToPx(content.getHeight())));
                imageView.setScaleType(ImageView.ScaleType.valueOf(content.getScaleType()));
            }
        };
        adapter.setData(urlList);
        container.setAdapter(adapter);
    }
}
