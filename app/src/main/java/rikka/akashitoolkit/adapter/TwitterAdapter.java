package rikka.akashitoolkit.adapter;

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

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.ImagesActivity;

import static rikka.akashitoolkit.support.ApiConstParam.TwitterContentLanguage.JP_AND_ZH;
import static rikka.akashitoolkit.support.ApiConstParam.TwitterContentLanguage.ONLY_JP;
import static rikka.akashitoolkit.support.ApiConstParam.TwitterContentLanguage.ONLY_ZH;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder> {
    private static final String TAG = "TwitterAdapter";

    public static class DataModel {
        public int id;
        public String text;
        public String translated;
        public String date;
        public String modified;
        public long time;

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public String getTranslated() {
            return translated;
        }

        public String getDate() {
            return date;
        }

        public String getModified() {
            return modified;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setTranslated(String translated) {
            this.translated = translated;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    public static class ShareDataModel {
        public String text;
        public String translated;
        public String url;
        public String title;
        public String date;

        public ShareDataModel() {
        }

        public ShareDataModel(String text, String translated, String url, String title) {
            this.text = text;
            this.translated = translated;
            this.url = url;
            this.title = title;
        }
    }

    private List<DataModel> mData;
    private int mMaxItem;
    private int mLanguage;
    private String mAvatarUrl;

    public interface Listener {
        void onMoreButtonClick(ShareDataModel data);

        void onAvatarLongClick();
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
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

    public List<DataModel> getData() {
        return mData;
    }

    public void setData(List<DataModel> data) {
        mData = data;
        //notifyDataSetChanged();
    }

    public TwitterAdapter() {
        mData = new ArrayList<>();

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
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

        holder.mName.setText("「艦これ」開発/運営");
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/kancolle_staff")));
            }
        });

        holder.mTvContent.setText(
                Html.fromHtml(
                        mData.get(position).getText()
                ));
        holder.mTvContent.setMovementMethod(LinkMovementMethod.getInstance());

        String translated = mData.get(position).getTranslated();
        if (TextUtils.isEmpty(translated)) {
            holder.mTvContentTranslated.setText(holder.mTvContentTranslated.getContext().getString(R.string.no_translated));
            holder.mTvContentTranslated.setEnabled(false);
        } else {
            holder.mTvContentTranslated.setText(
                    Html.fromHtml(
                            translated,
                            new ImageLoader(holder.mImage),
                            null
                    ));
            holder.mTvContentTranslated.setEnabled(true);
        }
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

        if (System.currentTimeMillis() - mData.get(position).getTime() < DateUtils.DAY_IN_MILLIS) {
            holder.mTime.setText(DateUtils.getRelativeTimeSpanString(
                    mData.get(position).getTime(),
                    System.currentTimeMillis(),
                    0));
        } else {
            holder.mTime.setText(DateUtils.formatDateTime(
                    holder.itemView.getContext(),
                    mData.get(position).getTime(),
                    DateUtils.FORMAT_SHOW_YEAR));
        }

        holder.mTvContent.setTextIsSelectable(true);
        holder.mTvContentTranslated.setTextIsSelectable(true);

        holder.mMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDataModel shareData = new ShareDataModel();
                shareData.text = holder.mTvContent.getText().toString();
                shareData.translated = holder.mTvContentTranslated.getText().toString();
                shareData.url = mAvatarUrl;
                shareData.date = holder.mTime.getText().toString();
                shareData.title = "「艦これ」開発/運営";

                if (mListener != null) {
                    mListener.onMoreButtonClick(shareData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = mData != null ? mData.size() : 0;
        return count > mMaxItem ? mMaxItem : count;
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
        }
    }

    protected class ImageLoader implements Html.ImageGetter, View.OnClickListener {
        private ImageView mImageView;
        private String mSource;

        public ImageLoader(ImageView target) {
            this.mImageView = target;
            mImageView.setOnClickListener(this);
        }

        @Override
        public Drawable getDrawable(String source) {
            mImageView.setVisibility(View.VISIBLE);

            mSource = source;
            Glide.with(mImageView.getContext())
                    .load(mSource)
                    .crossFade()
                    .into(mImageView);

            return new ColorDrawable(0);
        }

        @Override
        public void onClick(View v) {
            if (v == mImageView) {
                //ImageDialogFragment.showDialog(mFragmentManager, mSource);
                if (mSource != null) {
                    ImagesActivity.start(v.getContext(), mSource.replaceAll("-\\d+x\\d+", ""));
                }
            }
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
