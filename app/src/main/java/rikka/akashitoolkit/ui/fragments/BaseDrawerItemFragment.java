package rikka.akashitoolkit.ui.fragments;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import rikka.akashitoolkit.ui.MainActivity;

/**
 * Created by Rikka on 2016/3/10.
 */
public abstract class BaseDrawerItemFragment extends Fragment {
    private static final String HIDDEN = "HIDDEN";
    private boolean mHidden;

    protected MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mHidden = savedInstanceState.getBoolean(HIDDEN);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            outState.putBoolean(HIDDEN, mHidden);
        }
    }

    public boolean isHiddenBeforeSaveInstanceState() {
        return mHidden;
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        mHidden = hidden;

        if (hidden) {
            onHide();
        } else {
            onShow();
        }
    }

    public void onShow() {
        MainActivity activity = ((MainActivity) getActivity());
        setTabLayoutVisible();
        activity.setRightDrawerLocked(getRightDrawerLock());
        activity.getSwitch().setVisibility(getSwitchVisible() ? View.VISIBLE : View.GONE);
    }

    public void onHide() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public void setTabLayoutVisibleWithAnim() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setLayoutTransition(new LayoutTransition());
        activity.getTabLayout().setVisibility(getTabLayoutVisible() ? View.VISIBLE : View.GONE);
    }

    public void setTabLayoutVisible() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.getTabLayout().setVisibility(getTabLayoutVisible() ? View.VISIBLE : View.GONE);
    }

    public void showSnackbar(CharSequence text, @Snackbar.Duration int duration) {
        if (getActivity() == null) {
            return;
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.showSnackbar(text, duration);
    }

    public void showSnackbar(@StringRes int resId, @Snackbar.Duration int duration) {
        if (getActivity() == null) {
            return;
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.showSnackbar(resId, duration);
    }

    protected boolean getRightDrawerLock() {
        return true;
    }

    protected boolean getTabLayoutVisible() {
        return false;
    }

    protected boolean getSwitchVisible() {
        return false;
    }
}
