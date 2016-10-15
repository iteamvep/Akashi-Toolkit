package rikka.akashitoolkit.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.home.IRefresh;
import rikka.akashitoolkit.ui.fragments.DrawerFragment;
import rikka.akashitoolkit.ui.widget.RecyclerView;

/**
 * Created by Rikka on 2016/8/31.
 */
public abstract class BaseEventFragment<T> extends DrawerFragment implements IRefresh<T> {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        if (mRecyclerView == null || mSwipeRefreshLayout == null) {
            throw new RuntimeException("view R.id.recyclerView or R.id.swipeRefreshLayout not found");
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BaseEventFragment.this.onRefresh(false);
            }
        });

        if (savedInstanceState == null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    onRefresh(false);
                }
            });
        }
    }

    final public void onRefresh(boolean force_cache) {
        onRefresh(null, force_cache);
    }

    @Override
    public void onRefresh(Call<T> call, boolean force_cache) {
        if (call == null) {
            throw new NullPointerException();
        }

        if (!force_cache) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (getContext() == null) {
                    //onFailure(call, new RuntimeException("context == null"));
                    return;
                }

                if (response.code() >= 400) {
                    onFailure(call, null);
                }

                if (response.body() == null) {
                    onFailure(call, null);
                }

                BaseEventFragment.this.onSuccess(response.body());

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (getContext() == null) {
                    return;
                }

                BaseEventFragment.this.onFailure(call, t);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    onRefresh(false);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(@NonNull T data) {

    }
}
