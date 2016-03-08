package rikka.akashitoolkit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Twitter;

/**
 * Created by Rikka on 2016/3/6.
 */
public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder> {
    public static class DataModel {
        public String text;
        public String translated;
        public String date;

        public String getText() {
            return text;
        }

        public String getTranslated() {
            return translated;
        }

        public String getDate() {
            return date;
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
    }
    private List<DataModel> mData;

    public void setData(List<DataModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_twitter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.mAvatar.getContext())
                .load("http://static.kcwiki.moe/KanColleStaffAvatar.png")
                .crossFade()
                .into(holder.mAvatar);

        holder.mName.setText("「艦これ」開発/運営");
        holder.mText.setText(mData.get(position).getText());
        holder.mTextTranslate.setText(mData.get(position).getTranslated());
        holder.mTime.setText(mData.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mAvatar;
        protected TextView mName;
        protected TextView mText;
        protected TextView mTextTranslate;
        protected TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);

            mAvatar = (ImageView) itemView.findViewById(R.id.image_twitter_avatar);
            mName = (TextView) itemView.findViewById(R.id.text_twitter_name);
            mText = (TextView) itemView.findViewById(R.id.text_twitter_content);
            mTextTranslate = (TextView) itemView.findViewById(R.id.text_twitter_content_translated);
            mTime = (TextView) itemView.findViewById(R.id.text_twitter_time);
        }
    }
}
