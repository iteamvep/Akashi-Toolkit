package rikka.akashitoolkit.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Rikka on 2016/10/4.
 * 会在 onSaveInstanceState 里保存是否可见
 */

public class SaveVisibilityFragment extends Fragment {

    private static final String VISIBLE = "VISIBLE";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(VISIBLE, isVisible());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState == null || savedInstanceState.getBoolean(VISIBLE)) {
            onShow();
        }
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            onHide();
        } else {
            onShow();
        }
    }

    public void onShow() {

    }

    public void onHide() {

    }
}
