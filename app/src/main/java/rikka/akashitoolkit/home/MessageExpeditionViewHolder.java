package rikka.akashitoolkit.home;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.akashitoolkit.BuildConfig;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ChangeNavigationDrawerItemAction;
import rikka.akashitoolkit.receiver.ExpeditionAlarmReceiver;
import rikka.akashitoolkit.staticdata.ExpeditionList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.widget.CountdownTextView;

/**
 * Created by Rikka on 2016/8/7.
 */
public class MessageExpeditionViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout mContainer;
    public TextView mSummary;

    public BusEventListener mBusEventListener;

    public static class BusEventListener {
        public boolean isRegistered;
    }

    public MessageExpeditionViewHolder(View itemView) {
        super(itemView);

        mSummary = (TextView) itemView.findViewById(android.R.id.summary);
        mContainer = (LinearLayout) itemView.findViewById(android.R.id.content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mContainer.getLayoutTransition().disableTransitionType(LayoutTransition.APPEARING);
        }
    }

    @SuppressLint("DefaultLocale")
    public void setContent() {
        Context context = itemView.getContext();
        mContainer.removeAllViews();

        int count = 0;
        for (final rikka.akashitoolkit.model.Expedition expedition : ExpeditionList.get(context)) {
            if (!expedition.isBookmarked())
                continue;

            View view = LayoutInflater.from(context).inflate(R.layout.item_message_expedition, mContainer, false);
            ((TextView) view.findViewById(android.R.id.title)).setText(expedition.getName().get());
            final CountdownTextView textView = (CountdownTextView) view.findViewById(android.R.id.content);

            textView.setFinishText(context.getString(R.string.finished));

            long time = Settings.instance(context).getLong(String.format("expedition_time_%d", expedition.getId()), -1);
            if (time > 0) {
                textView.setEndTime(time);
                setAlarm(context, time, expedition.getId());
            } else {
                textView.setText(time == -1 ? context.getString(R.string.not_started) : context.getString(R.string.finished));
            }

            view.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    if (!textView.isCounting()) {
                        long time = System.currentTimeMillis() + (expedition.getTime() - 1) * 1000 * 60;
                        if (BuildConfig.DEBUG) {
                            time = System.currentTimeMillis() + (expedition.getTime() - 1) * 1000;
                        }
                        textView.setEndTime(time);
                        Settings.instance(context).putLong(String.format("expedition_time_%d", expedition.getId()), time);
                        setAlarm(context, time, expedition.getId());
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    textView.setText(view.getContext().getString(R.string.not_started));
                    textView.setEndTime(-1);
                    Settings.instance(view.getContext()).putLong(String.format("expedition_time_%d", expedition.getId()), -1);
                    cancelAlarm(view.getContext(), expedition.getId());
                    return true;
                }
            });
            mContainer.addView(view);

            count++;
        }

        if (count == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_text, mContainer, false);
            ((TextView) view.findViewById(android.R.id.title)).setText(R.string.bookmarked_items_no);
            view.findViewById(android.R.id.title).setEnabled(false);
            mContainer.addView(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_message_more, mContainer, false);
        ((TextView) view.findViewById(android.R.id.title)).setText(R.string.all_expedition_item);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.instance().post(new ChangeNavigationDrawerItemAction(R.id.nav_expedition));
            }
        });
        mContainer.addView(view);
    }

    public static void setAlarm(Context context, long time, int id) {
        // avoid same alarm?
        cancelAlarm(context, id);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ExpeditionAlarmReceiver.class);
        intent.putExtra("ExpeditionAlarmReceiver_ID", id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, alarmIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, alarmIntent);
        }
    }

    private static void cancelAlarm(Context context, int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ExpeditionAlarmReceiver.class);
        intent.putExtra("ExpeditionAlarmReceiver_ID", id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(alarmIntent);
    }
}
