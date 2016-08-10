package rikka.akashitoolkit.ui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/8/11.
 */
public abstract class BaseSearchActivity extends BaseActivity {

    private String mKeyword;
    private boolean mIsSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mKeyword = savedInstanceState.getString("KEYWORD");
            mIsSearching = savedInstanceState.getBoolean("SEARCHING");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        //searchView.setQueryHint(getSearchHint());

        if (mIsSearching) {
            String keyword = mKeyword;
            item.expandActionView();
            searchView.setQuery(keyword, false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("KEYWORD", mKeyword);
        outState.putBoolean("SEARCHING", mIsSearching);
    }

    public abstract void onSearchExpand();

    public abstract void onSearchCollapse();

    public abstract void onSearchTextChange(String newText);
}
