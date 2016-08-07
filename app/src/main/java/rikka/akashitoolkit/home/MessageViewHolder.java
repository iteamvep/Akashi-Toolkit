package rikka.akashitoolkit.home;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.gallery.ImagesActivity;
import rikka.akashitoolkit.utils.MySpannableFactory;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/8/7.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public TextView mSummary;
    public TextView mContent;
    public LinearLayout mGalleryContainer;
    public Button mPositiveButton;
    public Button mNegativeButton;

    public CountDownTimer mCountDownTimer;

    public MessageViewHolder(View itemView) {
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
    public String formatTimeLeft(long time) {
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
