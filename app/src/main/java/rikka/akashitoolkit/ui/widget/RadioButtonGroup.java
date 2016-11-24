package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/2/16.
 */
public class RadioButtonGroup extends LinearLayout {

    private int mItemLayoutResId;
    private List<CompoundButton> mRadioButtonList;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    private int mCheckedIndex;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, int checked);
    }

    public RadioButtonGroup(Context context) {
        this(context, -1);
    }

    public RadioButtonGroup(Context context, int checkedIndex) {
        super(context);
        setOrientation(VERTICAL);

        mRadioButtonList = new ArrayList<>();
        mItemLayoutResId = R.layout.drawer_item_radio_button;
        mCheckedIndex = checkedIndex;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void addTitle(String title) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.draw_itme_subtitle, this, false);

        ((TextView) view.findViewById(android.R.id.title)).setText(title);

        addView(view);
    }

    public void addItem(@StringRes int resId) {
        addItem(getContext().getString(resId));
    }

    public void addItem(String title) {
        View view = LayoutInflater.from(getContext())
                .inflate(mItemLayoutResId, this, false);

        ((TextView) view.findViewById(android.R.id.text1)).setText(title);

        final int index = mRadioButtonList.size();

        final CompoundButton radioButton = (CompoundButton) view.findViewById(R.id.radio);
        radioButton.setChecked(false);
        radioButton.setId(NO_ID);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                check(index);

                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChanged(v, index);
                }
            }
        });

        addView(view);
        mRadioButtonList.add(radioButton);
    }

    public void addDivider() {
        View view;
        view = new View(getContext());
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dpToPx(4)));
        addView(view);

        view = new View(getContext());
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.preference_list_divider_material));
        } else {
            view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.preference_list_divider_material));
        }
        addView(view);

        view = new View(getContext());
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dpToPx(4)));
        addView(view);
    }

    private void check(int index) {
        mCheckedIndex = index;

        for (int i = 0; i < mRadioButtonList.size(); i++) {
            mRadioButtonList.get(i).setChecked(i == index);
        }
    }

    public void setChecked(int index) {
        check(index);

        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, index);
        }
    }

    public int getCheckedIndex() {
        return mCheckedIndex;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.index = mCheckedIndex;

        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.index != -1) {
            setChecked(ss.index);
        }
    }

    public static class SavedState extends BaseSavedState {
        int index;

        public SavedState(Parcel source) {
            super(source);
            index = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(index);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
