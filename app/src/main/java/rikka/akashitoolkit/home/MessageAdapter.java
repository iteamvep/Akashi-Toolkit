package rikka.akashitoolkit.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;
import rikka.akashitoolkit.adapter.Listener;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.otto.BookmarkItemChanged;
import rikka.akashitoolkit.otto.BusProvider;

import static rikka.akashitoolkit.support.ApiConstParam.Message.ACTION_VIEW_BUTTON;
import static rikka.akashitoolkit.support.ApiConstParam.Message.COUNT_DOWN;
import static rikka.akashitoolkit.support.ApiConstParam.Message.HTML_CONTENT;
import static rikka.akashitoolkit.support.ApiConstParam.Message.NOT_DISMISSIBLE;

/**
 * Created by Rikka on 2016/6/11.
 */
public class MessageAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder, Object> {

    private static final String TAG = "MessageAdapter";

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_MESSAGE_UPDATE = 1;
    public static final int TYPE_DAILY_EQUIP = 2;
    public static final int TYPE_EXPEDITION_NOTIFY = 3;
    public static final int TYPE_VOTE = 4;

    private Listener mListener;

    public MessageAdapter() {
        setHasStableIds(true);
    }

    @Override
    public void rebuildDataList() {

    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void addItem(int type, Object object) {
        addItem(type, object, -1);
    }

    public void addItem(int type, Object object, int position) {
        if (position == -1) {
            addItem(generateItemId(type, object), type, object);
        } else {
            addItem(generateItemId(type, object), type, object, position);
        }

        //notifyDataSetChanged();
    }

    private long generateItemId(int type, Object object) {
        switch (type) {
            case TYPE_MESSAGE:
                CheckUpdate.MessagesEntity _data = (CheckUpdate.MessagesEntity) object;
                return _data.getId();
            default:
                return -type;
        }
    }

    public void remove(int position) {
        if (mListener != null) {
            mListener.OnRemove(position, getItem(position));
        }

        removeItem(position);

        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_MESSAGE:
                return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false));
            case TYPE_MESSAGE_UPDATE:
                return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false));
            case TYPE_DAILY_EQUIP:
                return new MessageEquipViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_equip, parent, false));
            case TYPE_EXPEDITION_NOTIFY:
                return new MessageExpeditionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_expedition, parent, false));
        }
        return null;
    }

    private void bindViewHolder(final MessageViewHolder holder, int position) {
        CheckUpdate.MessagesEntity data = (CheckUpdate.MessagesEntity) getItem(position);

        // title
        holder.mTitle.setText(data.getTitle());

        // summary
        holder.mSummary.setVisibility(View.GONE);

        // negative button
        if (!((data.getType() & NOT_DISMISSIBLE) > 0)) {
            holder.mNegativeButton.setVisibility(View.VISIBLE);
            holder.mNegativeButton.setText(R.string.got_it);
            holder.mNegativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(holder.getAdapterPosition());
                }
            });
        } else {
            holder.mNegativeButton.setVisibility(View.GONE);
        }

        // positive button
        if ((data.getType() & ACTION_VIEW_BUTTON) > 0) {
            holder.mPositiveButton.setVisibility(View.VISIBLE);

            if (data.getActionName() != null)
                holder.mPositiveButton.setText(data.getActionName());
            else
                holder.mPositiveButton.setText(R.string.open_link);

            holder.mPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckUpdate.MessagesEntity data = (CheckUpdate.MessagesEntity) getItem(holder.getAdapterPosition());
                    v.getContext()
                            .startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getLink())));
                }
            });
        } else {
            holder.mPositiveButton.setVisibility(View.GONE);
        }

        // content
        boolean isHtml = false;
        if ((data.getType() & HTML_CONTENT) > 0) {
            isHtml = true;
        }

        if ((data.getType() & COUNT_DOWN) > 0) {
            if (holder.mCountDownTimer != null) {
                holder.mCountDownTimer.cancel();
            }

            final String format = data.getMessage();
            holder.mCountDownTimer = new CountDownTimer(
                    data.getTime() * DateUtils.SECOND_IN_MILLIS - System.currentTimeMillis(), 1000) {

                public void onTick(long millisUntilFinished) {
                    holder.mContent.setText(String.format(format, holder.formatTimeLeft(millisUntilFinished)));
                }

                public void onFinish() {
                    holder.itemView.post(new Runnable() {
                        @Override
                        public void run() {
                            remove(holder.getAdapterPosition());
                        }
                    });
                }
            }.start();
        } else {
            if (isHtml) {
                holder.mContent.setText(Html.fromHtml(data.getMessage()));
                holder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                holder.mContent.setText(data.getMessage());
                holder.mContent.setMovementMethod(null);
            }
        }

        // images
        if (data.getImages() != null) {
            holder.addImages(data.getImages());
        } else {
            holder.mGalleryContainer.setVisibility(View.GONE);
        }
    }

    private void bindViewHolder(final MessageViewHolder holder, int position, Context context) {
        final CheckUpdate.UpdateEntity data = (CheckUpdate.UpdateEntity) getItem(position);

        holder.mTitle.setText(R.string.new_version_available);
        holder.mSummary.setText(String.format(context.getString(R.string.new_version_summary), data.getVersionName(), data.getVersionCode()));
        holder.mContent.setText(String.format(context.getString(R.string.new_version_content), data.getChange()));
        holder.mPositiveButton.setText(R.string.download);
        holder.mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl()));
                v.getContext().startActivity(intent);
            }
        });
        holder.mNegativeButton.setText(R.string.ignore_update);
        holder.mGalleryContainer.setVisibility(View.GONE);

        holder.mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
            }
        });
    }

    private void bindViewHolder(final MessageEquipViewHolder holder, int position) {
        if (holder.mBusEventListener == null) {
            holder.mBusEventListener = new MessageEquipViewHolder.BusEventListener() {
                @Subscribe
                public void bookmarkedChanged(BookmarkItemChanged.ItemImprovement event) {
                    holder.setContent();
                }
            };
        }

        if (!holder.mBusEventListener.isRegistered) {
            BusProvider.instance().register(holder.mBusEventListener);
            holder.mBusEventListener.isRegistered = true;
        }

        holder.setContent();
    }

    private void bindViewHolder(final MessageExpeditionViewHolder holder, int position) {
        if (holder.mBusEventListener == null) {
            holder.mBusEventListener = new MessageExpeditionViewHolder.BusEventListener() {
                @Subscribe
                public void bookmarkedChanged(BookmarkItemChanged.Expedition event) {
                    holder.setContent();
                }
            };
        }

        if (!holder.mBusEventListener.isRegistered) {
            BusProvider.instance().register(holder.mBusEventListener);
            holder.mBusEventListener.isRegistered = true;
        }

        holder.setContent();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_MESSAGE:
                bindViewHolder((MessageViewHolder) holder, position);
                break;
            case TYPE_MESSAGE_UPDATE:
                bindViewHolder((MessageViewHolder) holder, position, holder.itemView.getContext());
                break;
            case TYPE_DAILY_EQUIP:
                bindViewHolder((MessageEquipViewHolder) holder, position);
                break;
            case TYPE_EXPEDITION_NOTIFY:
                bindViewHolder((MessageExpeditionViewHolder) holder, position);
                break;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder _holder) {
        if (_holder instanceof MessageViewHolder) {
            MessageViewHolder holder = (MessageViewHolder) _holder;
            if (holder.mCountDownTimer != null) {
                holder.mCountDownTimer.cancel();
            }
        } else if (_holder instanceof MessageEquipViewHolder) {
            MessageEquipViewHolder holder = (MessageEquipViewHolder) _holder;
            if (holder.mBusEventListener != null) {
                BusProvider.instance().unregister(holder.mBusEventListener);
                holder.mBusEventListener.isRegistered = false;
            }
        } else if (_holder instanceof MessageExpeditionViewHolder) {
            MessageExpeditionViewHolder holder = (MessageExpeditionViewHolder) _holder;
            if (holder.mBusEventListener != null) {
                BusProvider.instance().unregister(holder.mBusEventListener);
                holder.mBusEventListener.isRegistered = false;
            }
        }
    }
}
