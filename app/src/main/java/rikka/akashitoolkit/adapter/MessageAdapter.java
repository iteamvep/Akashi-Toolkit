package rikka.akashitoolkit.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.model.Equip;
import rikka.akashitoolkit.model.EquipImprovement;
import rikka.akashitoolkit.otto.BookmarkAction;
import rikka.akashitoolkit.otto.BookmarkItemChanged;
import rikka.akashitoolkit.otto.BusProvider;
import rikka.akashitoolkit.otto.ChangeNavigationDrawerItemAction;
import rikka.akashitoolkit.otto.DataListRebuiltFinished;
import rikka.akashitoolkit.staticdata.EquipImprovementList;
import rikka.akashitoolkit.staticdata.EquipList;
import rikka.akashitoolkit.staticdata.EquipTypeList;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.EquipDisplayActivity;
import rikka.akashitoolkit.ui.MainActivity;

import static rikka.akashitoolkit.support.ApiConstParam.Message.ACTION_VIEW_BUTTON;
import static rikka.akashitoolkit.support.ApiConstParam.Message.COUNT_DOWN;
import static rikka.akashitoolkit.support.ApiConstParam.Message.HTML_CONTENT;
import static rikka.akashitoolkit.support.ApiConstParam.Message.NOT_DISMISSIBLE;

/**
 * Created by Rikka on 2016/6/11.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Data> mData;
    private Listener mListener;

    private static class Data {
        protected Object data;
        protected int type;
        private long id;

        public Data(Object data, int type, long id) {
            this.data = data;
            this.type = type;
            this.id = id;
        }
    }

    public MessageAdapter() {
        setHasStableIds(true);

        mData = new ArrayList<>();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void clear() {
        mData.clear();

        notifyDataSetChanged();
    }

    public void add(int type, Object object) {
        add(type, object, -1);
    }

    public void add(int type, Object object, int position) {
        Data data = new Data(object, type, RecyclerView.NO_ID);
        if (position == -1) {
            mData.add(data);
        } else {
            mData.add(position, data);
            notifyItemInserted(position);
        }

        switch (type) {
            case 0:
                CheckUpdate.MessagesEntity _data = (CheckUpdate.MessagesEntity) object;
                data.id = _data.getId();
                break;
            default:
        }
    }

    public void remove(int position) {
        if (mListener != null) {
            mListener.OnRemove(position, mData.get(position).data);
        }

        mData.remove(position);

        notifyItemRemoved(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder.Message(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false));
            case 1:
                return new ViewHolder.Message(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false));
            case 2:
                return new ViewHolder.MessageEquip(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_equip, parent, false));
            case 3:
                return new ViewHolder.MessageExpedition(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_expedition, parent, false));
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder _holder, int position) {
        Context context = _holder.itemView.getContext();

        if (getItemViewType(position) == 0) {
            final ViewHolder.Message holder = (ViewHolder.Message) _holder;
            CheckUpdate.MessagesEntity data = (CheckUpdate.MessagesEntity) mData.get(position).data;

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
                        remove(_holder.getAdapterPosition());
                    }
                });
            } else {
                holder.mNegativeButton.setVisibility(View.GONE);
            }

            // positive button
            if ((data.getType() & ACTION_VIEW_BUTTON) > 0) {
                holder.mPositiveButton.setVisibility(View.VISIBLE);

                if (data.getAction_name() != null)
                    holder.mPositiveButton.setText(data.getAction_name());
                else
                    holder.mPositiveButton.setText(R.string.open_link);

                holder.mPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckUpdate.MessagesEntity data = (CheckUpdate.MessagesEntity) mData.get(_holder.getAdapterPosition()).data;
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
        } else if (getItemViewType(position) == 1) {
            final ViewHolder.Message holder = (ViewHolder.Message) _holder;
            final CheckUpdate.UpdateEntity data = (CheckUpdate.UpdateEntity) mData.get(position).data;

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
                    remove(_holder.getAdapterPosition());
                }
            });
        } else if (getItemViewType(position) == 2) {
            final ViewHolder.MessageEquip holder = (ViewHolder.MessageEquip) _holder;

            if (holder.mBusEventListener == null) {
                holder.mBusEventListener = new ViewHolder.MessageEquip.BusEventListener() {
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
        } else if (getItemViewType(position) == 3) {
            final ViewHolder.MessageExpedition holder = (ViewHolder.MessageExpedition) _holder;

            if (holder.mBusEventListener == null) {
                holder.mBusEventListener = new ViewHolder.MessageExpedition.BusEventListener() {
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
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder _holder) {
        if (_holder instanceof ViewHolder.Message) {
            ViewHolder.Message holder = (ViewHolder.Message) _holder;
            if (holder.mCountDownTimer != null) {
                holder.mCountDownTimer.cancel();
            }
        } else if (_holder instanceof ViewHolder.MessageEquip) {
            ViewHolder.MessageEquip holder = (ViewHolder.MessageEquip) _holder;
            if (holder.mBusEventListener != null) {
                BusProvider.instance().unregister(holder.mBusEventListener);
                holder.mBusEventListener.isRegistered = false;
            }
        } else if (_holder instanceof ViewHolder.MessageExpedition) {
            ViewHolder.MessageExpedition holder = (ViewHolder.MessageExpedition) _holder;
            if (holder.mBusEventListener != null) {
                BusProvider.instance().unregister(holder.mBusEventListener);
                holder.mBusEventListener.isRegistered = false;
            }
        }
    }
}
