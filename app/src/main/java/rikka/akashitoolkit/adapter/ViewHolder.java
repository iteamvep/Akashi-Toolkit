package rikka.akashitoolkit.adapter;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ChangeNavigationDrawerItemAction;
import rikka.akashitoolkit.receiver.ExpeditionAlarmReceiver;
import rikka.akashitoolkit.staticdata.EquipImprovementList;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.staticdata.ExpeditionList;
import rikka.akashitoolkit.staticdata.ShipList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.EquipDisplayActivity;
import rikka.akashitoolkit.ui.ImagesActivity;
import rikka.akashitoolkit.ui.widget.CountdownTextView;
import rikka.akashitoolkit.ui.widget.ExpandableLayout;
import rikka.akashitoolkit.utils.MySpannableFactory;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/12.
 */
public class ViewHolder {
    public static class Quest extends RecyclerView.ViewHolder {
        protected ExpandableLayout mExpandableLayout;
        protected TextView mName;
        protected TextView mDetail;
        protected TextView mNote;
        protected TextView mRewardText[] = new TextView[5];
        protected TextView mType[] = new TextView[2];
        protected LinearLayout mQuestContainer;


        public Quest(View itemView) {
            super(itemView);

            mExpandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);

            mType[0] = (TextView) itemView.findViewById(R.id.text_quest_type);
            mType[1] = (TextView) itemView.findViewById(R.id.text_quest_type2);
            mName = (TextView) itemView.findViewById(R.id.text_card_title);

            mDetail = (TextView) itemView.findViewById(R.id.text_quest_detail);
            mNote = (TextView) itemView.findViewById(R.id.text_quest_note);
            mRewardText[0] = (TextView) itemView.findViewById(R.id.text_number_0);
            mRewardText[1] = (TextView) itemView.findViewById(R.id.text_number_1);
            mRewardText[2] = (TextView) itemView.findViewById(R.id.text_number_2);
            mRewardText[3] = (TextView) itemView.findViewById(R.id.text_number_3);
            mRewardText[4] = (TextView) itemView.findViewById(R.id.text_quest_reward_4);
            mQuestContainer = (LinearLayout) itemView.findViewById(R.id.quest_container);

            /*for (TextView textView :
                    mType) {
                if (textView == null) {
                    continue;
                }

                textView.getBackground().setColorFilter(
                        ContextCompat.getColor(itemView.getContext(), R.color.colorAccent),
                        PorterDuff.Mode.SRC_ATOP);
            }*/
        }
    }

    public static class Expedition extends RecyclerView.ViewHolder {
        protected ExpandableLayout mExpandableLayout;
        protected TextView[] mRewardNumber;
        protected TextView[] mRequireNumber;
        protected TextView mTitle;
        protected TextView mTime;
        protected TextView mReward;
        protected TextView mFleetRequire;
        protected TextView mShipRequire;

        public Expedition(View itemView) {
            super(itemView);

            mExpandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mTime = (TextView) itemView.findViewById(R.id.text_time);

            mRewardNumber = new TextView[4];
            mRewardNumber[0] = (TextView) itemView.findViewById(R.id.text_number_0);
            mRewardNumber[1] = (TextView) itemView.findViewById(R.id.text_number_1);
            mRewardNumber[2] = (TextView) itemView.findViewById(R.id.text_number_2);
            mRewardNumber[3] = (TextView) itemView.findViewById(R.id.text_number_3);
            mReward = (TextView) itemView.findViewById(R.id.text_reward);
            mRequireNumber = new TextView[3];
            mRequireNumber[0] = (TextView) itemView.findViewById(R.id.text_number_4);
            mRequireNumber[1] = (TextView) itemView.findViewById(R.id.text_number_5);
            mRequireNumber[2] = (TextView) itemView.findViewById(R.id.text_number_6);

            mFleetRequire = (TextView) itemView.findViewById(R.id.text_fleet_require);
            mShipRequire = (TextView) itemView.findViewById(R.id.text_ship_require);
        }

        public void setRewardResource(String str, int i) {
            setRewardResource(Html.fromHtml(str), i);
        }

        public void setRewardResource(Spanned str, int i) {
            if (TextUtils.isEmpty(str)) {
                mRewardNumber[i].setText("0");
            } else {
                mRewardNumber[i].setText(str);
            }
        }

        public void setRequireResource(String str, int i) {
            if (TextUtils.isEmpty(str)) {
                mRequireNumber[i].setText("0");
            } else {
                mRequireNumber[i].setText(str);
            }
        }

        public void setRewardText(@Nullable String str1, @Nullable String str2) {
            StringBuilder sb = new StringBuilder();

            if (!TextUtils.isEmpty(str1)) {
                sb.append(str1);
            }

            if (!TextUtils.isEmpty(str2)) {
                if (sb.length() > 0) {
                    sb.append("<br>");
                }

                sb.append(str2);
            }

            setRewardNumber(Html.fromHtml(sb.toString()));
        }

        public void setRewardNumber(@Nullable Spanned str) {
            if (TextUtils.isEmpty(str)) {
                mReward.setVisibility(View.GONE);
            } else {
                mReward.setVisibility(View.VISIBLE);
                mReward.setText(str);
            }
        }

        @SuppressLint("DefaultLocale")
        public void setFleetRequire(int totalLevel, int flagshipLevel, int minShips) {
            StringBuilder sb = new StringBuilder();
            if (totalLevel != 0) {
                sb.append(String.format("舰队总等级 %d", totalLevel));
            }

            if (flagshipLevel != 0) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(String.format("旗舰等级 %d", flagshipLevel));
            }

            if (minShips != 0) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(String.format("最低舰娘数 %d", minShips));
            }

            mFleetRequire.setText(sb.toString());
        }

        public void setShipRequire(@Nullable String ship, @Nullable String bucket) {
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(ship)) {
                sb.append(ship.replace(" ", "<br>"));
            }

            if (!TextUtils.isEmpty(bucket)) {
                if (sb.length() > 0) {
                    sb.append("<br>");
                }
                sb.append(bucket);
            }

            mShipRequire.setText(Html.fromHtml(sb.toString()));
        }
    }

    public static class ItemImprovement extends RecyclerView.ViewHolder {
        protected TextView mName;
        protected TextView mShip;
        protected ImageView mImageView;

        public ItemImprovement(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(android.R.id.title);
            mShip = (TextView) itemView.findViewById(android.R.id.summary);
            mImageView = (ImageView) itemView.findViewById(android.R.id.icon);
        }
    }

    public static class Item extends RecyclerView.ViewHolder {
        protected View mLinearLayout;
        protected TextView mName;
        protected TextView mTitle;
        protected ImageView mImageView;

        public Item(View itemView) {
            super(itemView);

            mLinearLayout = itemView.findViewById(R.id.linearLayout);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mName = (TextView) itemView.findViewById(R.id.textView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public static class Ship extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mContent;
        protected ImageView mIcon;
        protected View mIconContainer;

        public Ship(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mContent = (TextView) itemView.findViewById(android.R.id.content);
            mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
            mIconContainer = itemView.findViewById(R.id.content_container);
        }
    }

    public static class Map extends RecyclerView.ViewHolder {
        protected ExpandableLayout mDetailContainer;
        protected TextView mTitle;
        protected TextView mTextView;
        protected Button mButton;

        public Map(View itemView) {
            super(itemView);

            mDetailContainer = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);
            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mTextView = (TextView) itemView.findViewById(android.R.id.content);
            mButton = (Button) itemView.findViewById(android.R.id.button1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDetailContainer.isExpanded()) {
                        mDetailContainer.setExpanded(false);
                        mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    } else {
                        mDetailContainer.setExpanded(true);
                        mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                }
            });
        }
    }

    public static class Message extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mSummary;
        protected TextView mContent;
        protected LinearLayout mGalleryContainer;
        protected Button mPositiveButton;
        protected Button mNegativeButton;

        protected CountDownTimer mCountDownTimer;

        public Message(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mSummary = (TextView) itemView.findViewById(android.R.id.summary);
            mContent = (TextView) itemView.findViewById(android.R.id.content);
            mGalleryContainer = (LinearLayout) itemView.findViewById(R.id.content_container);
            mPositiveButton = (Button) itemView.findViewById(android.R.id.button1);
            mNegativeButton = (Button) itemView.findViewById(android.R.id.button2);

            mContent.setSpannableFactory(MySpannableFactory.getInstance());
        }

        public void addImages(final List<String> urls) {
            mGalleryContainer.removeAllViews();
            mGalleryContainer.setVisibility(View.VISIBLE);

            int i = 0;
            for (String url : urls) {
                final int finalI = i;
                addImage(url, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagesActivity.start(itemView.getContext(), urls, finalI, null, false);
                    }
                });
                i++;
            }
        }

        public void addImage(final String url, View.OnClickListener listener) {
            ImageView imageView = new ImageView(itemView.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dpToPx(80), Utils.dpToPx(80));
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (mGalleryContainer.getChildCount() > 0) {
                lp.leftMargin = Utils.dpToPx(8);
            }

            Glide.with(imageView.getContext())
                    .load(Utils.getGlideUrl(url))
                    .crossFade()
                    .into(imageView);

            mGalleryContainer.addView(imageView);

            imageView.setOnClickListener(listener);
        }

        @SuppressLint("DefaultLocale")
        protected String formatTimeLeft(long time) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%d 天", time / DateUtils.DAY_IN_MILLIS));
            time = time % DateUtils.DAY_IN_MILLIS;


            sb.append(String.format(" %d 小时", time / DateUtils.HOUR_IN_MILLIS));
            time = time % DateUtils.HOUR_IN_MILLIS;

            sb.append(String.format(" %d 分钟", time / DateUtils.MINUTE_IN_MILLIS));
            time = time % DateUtils.MINUTE_IN_MILLIS;

            sb.append(String.format(" %d 秒", time / DateUtils.SECOND_IN_MILLIS));

            return sb.toString();
        }

    }

    public static class MessageEquip extends RecyclerView.ViewHolder {
        protected LinearLayout mContainer;
        protected TextView mSummary;

        protected BusEventListener mBusEventListener;

        protected static class BusEventListener {
            public boolean isRegistered;
        }

        public MessageEquip(View itemView) {
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
                                        sb.append(s.getName().get(context));
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
                                    String.format("%s (%s)", equip.getName().get(context), sb.toString()));

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(v.getContext(), EquipDisplayActivity.class);
                                    intent.putExtra(EquipDisplayActivity.EXTRA_ITEM_ID, equip.getId());

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

    public static class MessageExpedition extends RecyclerView.ViewHolder {
        protected LinearLayout mContainer;
        protected TextView mSummary;

        protected BusEventListener mBusEventListener;

        protected static class BusEventListener {
            public boolean isRegistered;
        }

        public MessageExpedition(View itemView) {
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
                ((TextView) view.findViewById(android.R.id.title)).setText(expedition.getName().get(context));
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

        private void setAlarm(Context context, long time, int id) {
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

        private void cancelAlarm(Context context, int id) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ExpeditionAlarmReceiver.class);
            intent.putExtra("ExpeditionAlarmReceiver_ID", id);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(alarmIntent);
        }
    }

    // TODO
    public static class MessageVote extends RecyclerView.ViewHolder {

        public MessageVote(View itemView) {
            super(itemView);
        }
    }

    public static class Subtitle extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected View mDivider;

        public Subtitle(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(android.R.id.title);
            mDivider = itemView.findViewById(R.id.divider);
        }
    }

    public static class Voice extends RecyclerView.ViewHolder {
        protected TextView mScene;
        protected TextView mTextJa;
        protected TextView mTextZh;

        public Voice(View itemView) {
            super(itemView);

            mScene = (TextView) itemView.findViewById(android.R.id.title);
            mTextJa = (TextView) itemView.findViewById(android.R.id.content);
            mTextZh = (TextView) itemView.findViewById(android.R.id.summary);
        }
    }
}
