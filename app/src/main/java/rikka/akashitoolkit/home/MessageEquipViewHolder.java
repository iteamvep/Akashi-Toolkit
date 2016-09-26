package rikka.akashitoolkit.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.equip.EquipDetailActivity;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ChangeNavigationDrawerItemAction;
import rikka.akashitoolkit.staticdata.EquipImprovementList;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/8/7.
 */
public class MessageEquipViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout mContainer;
    public TextView mSummary;

    public BusEventListener mBusEventListener;

    public static class BusEventListener {
        public boolean isRegistered;
    }

    public MessageEquipViewHolder(View itemView) {
        super(itemView);

        mSummary = (TextView) itemView.findViewById(android.R.id.summary);
        mContainer = (LinearLayout) itemView.findViewById(android.R.id.content);
    }

    @SuppressLint("DefaultLocale")
    public void setContent() {
        Context context = itemView.getContext();
        mContainer.removeAllViews();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+9:00"));
        int type = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        int count = 0;
        for (EquipImprovement item :
                EquipImprovementList.get(context)) {

            boolean add = false;
            StringBuilder sb = new StringBuilder();
            List<Integer> ids = new ArrayList<>();
            for (java.util.Map.Entry<Integer, List<Integer>> entry : item.getData().entrySet()) {
                int flag = entry.getKey();
                List<Integer> ship = entry.getValue();

                if ((flag & 1 << type) > 0) {
                    add = true;

                    for (int id : ship) {
                        if (ids.size() > 0) {
                            sb.append(" / ");
                        }

                        if (ids.indexOf(id) == -1) {
                            ids.add(id);

                            if (id == 0) {
                                sb.append(context.getString(R.string.improvement_any));
                            } else {
                                rikka.akashitoolkit.model.Ship s = ShipList.findItemById(context, id);
                                if (s != null)
                                    sb.append(s.getName().get());
                            }
                        }
                    }
                }
            }

            if (add) {
                item.setBookmarked(Settings.instance(context)
                        .getBoolean(String.format("equip_improve_%d", item.getId()), false));

                if (item.isBookmarked()) {
                    final Equip equip = EquipList.findItemById(context, item.getId());
                    if (equip != null) {
                        count++;

                        View view = LayoutInflater.from(context).inflate(R.layout.item_message_equip, mContainer, false);
                        EquipTypeList.setIntoImageView((ImageView) view.findViewById(android.R.id.icon), equip.getIcon());
                        ((TextView) view.findViewById(android.R.id.title)).setText(
                                String.format("%s (%s)", equip.getName().get(), sb.toString()));

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), EquipDetailActivity.class);
                                intent.putExtra(EquipDetailActivity.EXTRA_ITEM_ID, equip.getId());

                                v.getContext().startActivity(intent);
                            }
                        });

                        mContainer.addView(view);
                    }
                }
            }
        }

        if (count == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_text, mContainer, false);
            ((TextView) view.findViewById(android.R.id.title)).setText(R.string.bookmarked_items_no);
            view.findViewById(android.R.id.title).setEnabled(false);
            mContainer.addView(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_message_more, mContainer, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(R.string.all_equip_improve_item);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.instance().post(new ChangeNavigationDrawerItemAction(R.id.nav_item_improve));
            }
        });
        mContainer.addView(view);
    }
}
