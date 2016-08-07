package rikka.akashitoolkit.ui.widget;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
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
 * Created by Rikka on 2016/3/16.
 */
public class MessageView extends FrameLayout {
    private LinearLayout mButtonContainer;
    private LinearLayout mGalleryContainer;
    private TextView mTextViewTitle;
    private TextView mTextViewBody;
    private View mDivider;

    private String mTitle;
    private String mBody;

    public MessageView(Context context) {
        this(context, null);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageView setLayout(@LayoutRes int layout) {
        inflate(getContext(), layout, this);

        mButtonContainer = (LinearLayout) findViewById(R.id.content_container);
        mGalleryContainer = (LinearLayout) findViewById(R.id.gallery_container);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mTextViewBody = (TextView) findViewById(R.id.text_body);
        mDivider = findViewById(R.id.divider);

        return this;
    }

    public MessageView setTitle(int textId) {
        setTitle(getContext().getString(textId));
        return this;
    }

    public MessageView setTitle(CharSequence text) {
        mTitle = text.toString();
        mTextViewTitle.setText(text);
        return this;
    }

    public MessageView setMessage(int textId) {
        setMessage(getContext().getString(textId));
        return this;
    }

    public MessageView setMessage(CharSequence text) {
        mTitle = text.toString();
        return setMessage(text, false);
    }

    public MessageView setMessage(CharSequence text, boolean isHtml) {
        if (isHtml) {
            mTextViewBody.setText(Html.fromHtml(text.toString()));
            mTextViewBody.setMovementMethod(LinkMovementMethod.getInstance());
            mTextViewBody.setClickable(true);
            mTextViewBody.setSpannableFactory(MySpannableFactory.getInstance());
        } else {
            mTextViewBody.setText(text);
        }
        return this;
    }

    public void clear() {
        mButtonContainer.removeAllViews();
        mGalleryContainer.removeAllViews();
        mDivider.setVisibility(GONE);
    }

    public MessageView addPositiveButton(int textId, @Nullable OnClickListener listener) {
        return addPositiveButton(getContext().getString(textId), listener);
    }

    public MessageView addPositiveButton(CharSequence text, @Nullable OnClickListener listener) {
        return addButton(text, listener, true, false);
    }

    public MessageView addNegativeButton(int textId, @Nullable OnClickListener listener) {
        return addNegativeButton(getContext().getString(textId), listener);
    }

    public MessageView addNegativeButton(CharSequence text, @Nullable OnClickListener listener) {
        return addButton(text, listener, false, true);
    }

    public MessageView addButton(CharSequence text, @Nullable OnClickListener listener, boolean useAccentColor, boolean clickHide) {
        Button button = (Button) inflate(getContext(), R.layout.button_borderless, null);

        button.setText(text);
        if (listener != null) {
            button.setOnClickListener(listener);
        }

        if (useAccentColor) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getContext().getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);

            button.setTextColor(typedValue.data);
        }

        mButtonContainer.addView(button);
        mDivider.setVisibility(VISIBLE);

        if (clickHide && listener == null) {
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
        }
        return this;
    }

    public void addImages(final List<String> urls) {
        int i = 0;
        for (String url : urls) {
            final int finalI = i;
            addImage(url, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagesActivity.start(getContext(), urls, finalI, null, false);
                }
            });
            i++;
        }
    }

    public void addImage(final String url, OnClickListener listener) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dpToPx(80), Utils.dpToPx(80));
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (mGalleryContainer.getChildCount() > 0) {
            lp.leftMargin = Utils.dpToPx(8);
        }

        Glide.with(getContext())
                .load(Utils.getGlideUrl(url))
                .crossFade()
                .into(imageView);

        mGalleryContainer.addView(imageView);

        imageView.setOnClickListener(listener);
    }

    public void hide() {
        animate()
                .translationX(getWidth() * 1.2f)
                .setDuration(150)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                setVisibility(View.GONE);
                            }
                        });

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }
}
