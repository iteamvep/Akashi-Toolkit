package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.TwitterAdapter;
import rikka.akashitoolkit.model.Twitter;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterFragment extends Fragment {
    private static final String TAG = "TwitterFragment";

    private static final int TAB_LAYOUT_VISIBILITY = View.GONE;
    private static final String TWITTER_API = "http://t.kcwiki.moe";

    private RecyclerView mRecyclerView;
    private TwitterAdapter mTwitterAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_swipe_refresh, container, false);

        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(TAB_LAYOUT_VISIBILITY);
        activity.getSupportActionBar().setTitle(getString(R.string.official_twitter));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mTwitterAdapter = new TwitterAdapter();
        mRecyclerView.setAdapter(mTwitterAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //layoutManager.setAutoMeasureEnabled(false);
        mRecyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(1, 30);
            }
        });
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                refresh(1, 30);
            }
        }, 500);


        return view;
    }

    // TODO save result to local, load local file first
    private void refresh(int json, int count) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TWITTER_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI.TwitterService service = retrofit.create(RetrofitAPI.TwitterService.class);
        Call<Twitter> call = service.get(json, count);

        call.enqueue(new Callback<Twitter>() {
            @Override
            public void onResponse(Call<Twitter> call, Response<Twitter> response) {
                mSwipeRefreshLayout.setRefreshing(false);

                List<TwitterAdapter.DataModel> data = new ArrayList<>();

                for (Twitter.PostsEntity entity:
                        response.body().getPosts()) {
                    TwitterAdapter.DataModel item = new TwitterAdapter.DataModel();
                    String content = entity.getContent();

                    Pattern r = Pattern.compile("<p>[\\w\\W]+?<br />");
                    Matcher m = r.matcher(content);

                    int i = 0;
                    while (m.find()) {
                        if (i == 0) {
                            item.setText(cutString(m.group(), "<p>", "<br />"));
                        } else {
                            item.setTranslated(cutString(m.group(), "<p>", "<br />"));
                        }
                        i ++;
                    }

                    item.setDate(entity.getDate());

                    data.add(item);
                }
                mTwitterAdapter.setData(data);
            }

            @Override
            public void onFailure(Call<Twitter> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private String cutString(String string, String a, String b) {
        int len = string.length();
        return string.substring(a.length(), len - b.length());
    }
}
