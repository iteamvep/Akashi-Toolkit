package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.res.AppCompatDrawableResource;

/**
 * Created by Rikka on 2016/10/8.
 */

public class CheckBoxCompat extends AppCompatCheckBox {
    public CheckBoxCompat(Context context) {
        this(context, null);
    }

    public CheckBoxCompat(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.checkboxStyle);
    }

    public CheckBoxCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CheckBoxCompat, defStyleAttr, 0);

        int resId;
        if (a.hasValue(R.styleable.CheckBoxCompat_buttonCompat)) {
            ColorStateList csl = null;
            if (a.hasValue(R.styleable.CheckBoxCompat_buttonDrawableTint)) {
                resId = a.getResourceId(R.styleable.CheckBoxCompat_buttonDrawableTint, 0);
                csl = AppCompatResources.getColorStateList(context, resId);
            }
            resId = a.getResourceId(R.styleable.CheckBoxCompat_buttonCompat, 0);
            Drawable dr = AppCompatDrawableResource.getDrawable(getContext(), resId, csl);
            setButtonDrawable(dr);
        }

        a.recycle();
    }

    @Override
    public void setButtonDrawable(@DrawableRes int resId) {
        setButtonDrawable(AppCompatDrawableResource.getDrawable(getContext(), resId, null));
    }
}
