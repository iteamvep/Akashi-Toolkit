package rikka.akashitoolkit.ui.fragments;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import rikka.akashitoolkit.MainActivity;

/**
 * Created by Rikka on 2016/3/10.
 */
public abstract class BaseDrawerItemFragment extends BaseShowHideFragment {

    protected MainActivity mActivity;

    public void onShow() {
        super.onShow();

        MainActivity activity = ((MainActivity) getActivity());
        setTabLayoutVisible();
        activity.setRightDrawerLocked(getRightDrawerLock());
        activity.getSwitch().setVisibility(getSwitchVisible() ? View.VISIBLE : View.GONE);
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
        mActivity.getTabLayout().setLayoutTransition(new LayoutTransition());
        mActivity.getTabLayout().setVisibility(getTabLayoutVisible() ? View.VISIBLE : View.GONE);
    }

    public void setTabLayoutVisible() {
        mActivity.getTabLayout().setVisibility(getTabLayoutVisible() ? View.VISIBLE : View.GONE);
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

    public void setToolbarTitle(@StringRes int resId) {
        setToolbarTitle(getString(resId));
    }

    public void setToolbarTitle(String title) {
        if (mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle(title);
        }
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
