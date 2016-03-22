package rikka.akashitoolkit.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
    private List<RadioButton> mRadioButtonList;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, int checked);
    }

    public RadioButtonGroup(Context context) {
        super(context);
        setOrientation(VERTICAL);

        mRadioButtonList = new ArrayList<>();
        mItemLayoutResId = R.layout.drawer_item_radio_button;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void addItem(String title) {
        View view = LayoutInflater.from(getContext())
                .inflate(mItemLayoutResId, this, false);

        ((TextView) view.findViewById(R.id.textView)).setText(title);

        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radio);
        radioButton.setChecked(false);

        final int index = mRadioButtonList.size();
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
            view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        } else {
            view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        }
        addView(view);

        view = new View(getContext());
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dpToPx(4)));
        addView(view);
    }

    private void check(int index) {
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
}
