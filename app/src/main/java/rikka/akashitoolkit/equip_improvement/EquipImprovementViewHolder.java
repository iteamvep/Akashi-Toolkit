package rikka.akashitoolkit.equip_improvement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.util.StateSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.equip.EquipDetailActivity;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.otto.BookmarkItemChanged;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/8/7.
 */
public class EquipImprovementViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<EquipImprovementAdapter.Data> {

    protected EquipImprovementAdapter mAdapter;

    public TextView mName;
    public TextView mShip;
    public ImageView mImageView;
    public CheckBox mCheckBox;

    public EquipImprovementViewHolder(View itemView) {
        super(itemView);

        mName = (TextView) itemView.findViewById(android.R.id.title);
        mShip = (TextView) itemView.findViewById(android.R.id.summary);
        mImageView = (ImageView) itemView.findViewById(android.R.id.icon);
        mCheckBox = (CheckBox) itemView.findViewById(android.R.id.checkbox);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = AppCompatResources.getDrawable(itemView.getContext(), R.drawable.btn_bookmark_material_anim_32dp);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                ColorStateList colorStateList = AppCompatResources.getColorStateList(itemView.getContext(), R.color.control_checkable_material);
                drawable.setTintList(colorStateList);
            }

            mCheckBox.setButtonDrawable(drawable);
        } else {
            Context context = itemView.getContext();
            StateListDrawable drawable = new StateListDrawable();
            ColorStateList colorStateList = AppCompatResources.getColorStateList(context, R.color.control_checkable_material);
            Drawable _drawable;
            _drawable = AppCompatResources.getDrawable(context, R.drawable.ic_bookmark_checked_32dp);
            DrawableCompat.setTintList(_drawable, colorStateList);
            drawable.addState(new int[]{android.R.attr.state_checked}, _drawable);
            _drawable = AppCompatResources.getDrawable(context, R.drawable.ic_bookmark_unchecked_32dp);
            DrawableCompat.setTintList(_drawable, colorStateList);
            drawable.addState(StateSet.WILD_CARD, _drawable);
            mCheckBox.setButtonDrawable(drawable);
        }
    }

    @Override
    public void bind(EquipImprovementAdapter.Data data, int position) {
        Context context = itemView.getContext();

        EquipImprovement item = data.data;
        Equip equip = EquipList.findItemById(context, item.getId());

        if (equip == null) {
            mName.setText(String.format(context.getString(R.string.equip_not_found), item.getId()));
            return;
        }

        mName.setText(equip.getName().get());
        mShip.setText(data.ship);

        if (mCheckBox.isChecked() != item.isBookmarked()) {
            mCheckBox.setChecked(item.isBookmarked());
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = itemView.getContext();

                EquipImprovement item = mAdapter.getItem(getAdapterPosition()).data;

                Intent intent = new Intent(v.getContext(), EquipDetailActivity.class);
                intent.putExtra(EquipDetailActivity.EXTRA_ITEM_ID, item.getId());

                int[] location = new int[2];
                itemView.getLocationOnScreen(location);
                intent.putExtra(EquipDetailActivity.EXTRA_START_Y, location[1]);
                intent.putExtra(EquipDetailActivity.EXTRA_START_HEIGHT, itemView.getHeight());
                intent.putExtra(EquipDetailActivity.EXTRA_EQUIP_IMPROVE_ID, item.getId());

                BaseItemDisplayActivity.start(context, intent);
            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                Context context = itemView.getContext();
                EquipImprovement item = mAdapter.getItem(getAdapterPosition()).data;
                Settings.instance(context)
                        .putBoolean(String.format("equip_improve_%d", item.getId()), checked);
                item.setBookmarked(checked);
                mAdapter.showToast(context, item.isBookmarked());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public boolean onLongClick(View v) {
                Context context = itemView.getContext();

                EquipImprovement item = mAdapter.getItem(getAdapterPosition()).data;

                item.setBookmarked(!item.isBookmarked());

                Settings.instance(context)
                        .putBoolean(String.format("equip_improve_%d", item.getId()), item.isBookmarked());

                mAdapter.showToast(context, item.isBookmarked());

                mAdapter.notifyItemChanged(getAdapterPosition());

                BusProvider.instance().post(new BookmarkItemChanged.ItemImprovement());

                return true;
            }
        });

        EquipTypeList.setIntoImageView(mImageView, equip.getIcon());
    }
}