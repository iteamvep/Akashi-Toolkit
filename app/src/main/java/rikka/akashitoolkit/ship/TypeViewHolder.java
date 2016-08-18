package rikka.akashitoolkit.ship;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.StateSet;
import android.view.View;

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.viewholder.SimpleTitleDividerViewHolder;

/**
 * Created by Rikka on 2016/8/19.
 */
public class TypeViewHolder extends SimpleTitleDividerViewHolder {

    public TypeViewHolder(View itemView) {
        super(itemView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = itemView.getContext().getDrawable(R.drawable.btn_expand_material_anim);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mTitle, null, null, drawable, null);
        } else {
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_selected}, AppCompatResources.getDrawable(itemView.getContext(), R.drawable.ic_expand_less_24dp));
            drawable.addState(StateSet.WILD_CARD, AppCompatResources.getDrawable(itemView.getContext(), R.drawable.ic_expand_more_24dp));
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mTitle, null, null, drawable, null);
        }
    }
}
