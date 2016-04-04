package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/4/4.
 */
public abstract class BaseSearchFragment extends BaseFragmet {
    private String mKeyword;
    private boolean mIsSearching;

    @Override
    public void onHide() {
        super.onHide();

        mIsSearching = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mKeyword = savedInstanceState.getString("KEYWORD");
            mIsSearching = savedInstanceState.getBoolean("SEARCHING");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("KEYWORD", mKeyword);
        outState.putBoolean("SEARCHING", mIsSearching);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        onSearchExpand();

                        mIsSearching = true;
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        onSearchCollapse();
                        return true;
                    }
                });

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearchTextChange(newText);

                mKeyword = newText;
                return false;
            }
        });
        searchView.setQueryHint(getSearchHint());

        if (mIsSearching) {
            String keyword = mKeyword;
            item.expandActionView();
            searchView.setQuery(keyword, false);
        }
    }

    public String getSearchHint() {
        return getResources().getString(R.string.abc_search_hint);
    }

    public boolean isSearching() {
        return mIsSearching;
    }

    public abstract void onSearchExpand();

    public abstract void onSearchCollapse();

    public abstract void onSearchTextChange(String newText);
}
