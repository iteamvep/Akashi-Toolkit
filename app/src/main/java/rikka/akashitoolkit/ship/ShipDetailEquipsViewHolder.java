package rikka.akashitoolkit.ship;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.equip.EquipDisplayActivity;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.ui.BaseItemDisplayActivity;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/17.
 */
public class ShipDetailEquipsViewHolder extends RecyclerView.ViewHolder implements IBindViewHolder<Ship.EquipEntity> {

    public RecyclerView mRecyclerView;
    public Adapter mAdapter;

    public ShipDetailEquipsViewHolder(View itemView) {
        super(itemView);

        // really need it ?
        setIsRecyclable(false);

        mRecyclerView = (RecyclerView) itemView.findViewById(android.R.id.content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPadding(0, 0, 0, 0);
    }

    @Override
    public void bind(Ship.EquipEntity data, int position) {
        mAdapter.setEquipEntity(data);
    }

    private class Adapter extends RecyclerView.Adapter<TitleContentViewHolder> {

        private Ship.EquipEntity mEquipEntity;

        public void setEquipEntity(Ship.EquipEntity equipEntity) {
            mEquipEntity = equipEntity;
        }

        @Override
        public TitleContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TitleContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ship_detail_item_equip, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(TitleContentViewHolder holder, int position) {
            Context context = holder.itemView.getContext();
            int id = mEquipEntity.getId()[position];
            if (id > 0) {
                final Equip item = EquipList.findItemById(context, id);

                if (item == null) {
                    holder.mTitle.setText(String.format(context.getString(R.string.equip_not_found), id));
                    holder.mTitle.setEnabled(false);
                } else {
                    holder.mTitle.setText(item.getName().get());

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(itemView.getContext(), EquipDisplayActivity.class);
                            intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID, item.getId());
                            BaseItemDisplayActivity.start(itemView.getContext(), intent);
                        }
                    });
                }
            } else {
                holder.mTitle.setText(context.getString(R.string.not_equipped));
                holder.mTitle.setEnabled(false);
            }

            holder.mContent.setText(Integer.toString(mEquipEntity.getSpace()[position]));
        }

        @Override
        public int getItemCount() {
            return mEquipEntity == null ? 0 : mEquipEntity.getSlots();
        }
    }
}
