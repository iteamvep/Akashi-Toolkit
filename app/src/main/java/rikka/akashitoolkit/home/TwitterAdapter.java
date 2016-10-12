package rikka.akashitoolkit.home;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.BaseRecyclerAdapter;
import rikka.akashitoolkit.gallery.ImagesActivity;
import rikka.akashitoolkit.model.Twitter;
import rikka.akashitoolkit.ui.widget.MySpannableFactory;
import rikka.akashitoolkit.utils.FlavorsUtils;
import rikka.akashitoolkit.utils.HtmlUtils;

import static rikka.akashitoolkit.support.ApiConstParam.TwitterContentLanguage.JP_AND_ZH;
import static rikka.akashitoolkit.support.ApiConstParam.TwitterContentLanguage.ONLY_JP;
import static rikka.akashitoolkit.support.ApiConstParam.TwitterContentLanguage.ONLY_ZH;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterAdapter extends BaseRecyclerAdapter<TwitterAdapter.ViewHolder, Twitter> {

    private static final String TAG = "TwitterAdapter";

    private int mMaxItem;
    private int mLanguage;
    private String mAvatarUrl;

    public interface Listener {
        void onMoreButtonClick(Twitter data);

        void onAvatarLongClick();
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        if (avatarUrl.length() > 0 && !avatarUrl.equals(mAvatarUrl)) {
            mAvatarUrl = avatarUrl;
            notifyDataSetChanged();
        }
    }

    public void setLanguage(int language) {
        mLanguage = language;
    }

    public void setMaxItem(int maxItem) {
        mMaxItem = maxItem;
    }

    public TwitterAdapter() {
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_twitter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mAvatarUrl != null) {
            Glide.with(holder.mAvatar.getContext())
                    .load(mAvatarUrl)
                    .crossFade()
                    .into(holder.mAvatar);

            holder.mAvatar.setOnClickListener(mAvatarOnClickListener);
            holder.mAvatar.setOnLongClickListener(mAvatarOnLongClickListener);
        }

        if (FlavorsUtils.shouldSafeCheck()) {
            holder.mAvatar.setVisibility(View.GONE);
        }

        holder.mName.setText("「艦これ」開発/運営");
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/kancolle_staff")));
            }
        });

        holder.mTvContent.setText(HtmlUtils.fromHtml(getItem(position).getJp()));

        String translated = getItem(position).getZh();
        if (TextUtils.isEmpty(translated)) {
            holder.mTvContentTranslated.setText(holder.mTvContentTranslated.getContext().getString(R.string.no_translated));
            holder.mTvContentTranslated.setEnabled(false);
        } else {
            holder.mTvContentTranslated.setText(HtmlUtils.fromHtml(translated));
            holder.mTvContentTranslated.setEnabled(true);
        }

        holder.mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
        holder.mTvContentTranslated.setMovementMethod(LinkMovementMethod.getInstance());

        switch (mLanguage) {
            case JP_AND_ZH:
                holder.mTvContent.setVisibility(View.VISIBLE);
                holder.mTvContentTranslated.setVisibility(View.VISIBLE);
                break;
            case ONLY_JP:
                holder.mTvContent.setVisibility(View.VISIBLE);
                holder.mTvContentTranslated.setVisibility(View.GONE);
                break;
            case ONLY_ZH:
                holder.mTvContent.setVisibility(View.GONE);
                holder.mTvContentTranslated.setVisibility(View.VISIBLE);
                break;
        }

        if (System.currentTimeMillis() - getItem(position).getTimestamp() < DateUtils.DAY_IN_MILLIS) {
            holder.mTime.setText(DateUtils.getRelativeTimeSpanString(
                    getItem(position).getTimestamp(),
                    System.currentTimeMillis(),
                    0));
        } else {
            holder.mTime.setText(DateUtils.formatDateTime(
                    holder.itemView.getContext(),
                    getItem(position).getTimestamp(),
                    DateUtils.FORMAT_SHOW_YEAR));
        }

        holder.mMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ShareDataModel shareData = new ShareDataModel();
                shareData.text = holder.mTvContent.getText().toString();
                shareData.translated = holder.mTvContentTranslated.getText().toString();
                shareData.url = mAvatarUrl;
                shareData.date = holder.mTime.getText().toString();
                shareData.title = "「艦これ」開発/運営";*/

                if (mListener != null) {
                    mListener.onMoreButtonClick(getItem(holder.getAdapterPosition()) /*shareData*/);
                }
            }
        });

        if (!TextUtils.isEmpty(getItem(position).getImg())) {
            holder.mImage.setVisibility(View.VISIBLE);

            Glide.with(holder.mImage.getContext())
                    .load(getItem(position).getImg())
                    .crossFade()
                    .into(holder.mImage);

            holder.mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagesActivity.start(v.getContext(), getItem(holder.getAdapterPosition()).getImg().replaceAll("-\\d+x\\d+", ""));
                }
            });
        } else {
            holder.mImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() > mMaxItem ? mMaxItem : super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mAvatar;
        protected TextView mName;
        protected TextView mTvContent;
        protected TextView mTvContentTranslated;
        protected TextView mTime;
        protected ImageView mImage;
        protected ImageView mMoreButton;

        public ViewHolder(View itemView) {
            super(itemView);

            mAvatar = (ImageView) itemView.findViewById(R.id.image_twitter_avatar);
            mName = (TextView) itemView.findViewById(R.id.text_twitter_name);
            mTvContent = (TextView) itemView.findViewById(R.id.text_twitter_content);
            mTvContentTranslated = (TextView) itemView.findViewById(R.id.text_twitter_content_translated);
            mTime = (TextView) itemView.findViewById(R.id.text_twitter_time);
            mImage = (ImageView) itemView.findViewById(R.id.image_twitter_content);
            mMoreButton = (ImageView) itemView.findViewById(R.id.more_button);

            mTvContent.setTextIsSelectable(true);
            mTvContentTranslated.setTextIsSelectable(true);
            mTvContent.setSpannableFactory(MySpannableFactory.getInstance());
            mTvContentTranslated.setSpannableFactory(MySpannableFactory.getInstance());
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        Glide.clear(holder.mAvatar);
        Glide.clear(holder.mImage);
    }

    private AvatarOnClickListener mAvatarOnClickListener = new AvatarOnClickListener();

    public class AvatarOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ImagesActivity.start(v.getContext(), mAvatarUrl);
        }
    }

    private AvatarOnLongClickListener mAvatarOnLongClickListener = new AvatarOnLongClickListener();

    public class AvatarOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            if (mListener != null) {
                mListener.onAvatarLongClick();
            }
            return true;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
