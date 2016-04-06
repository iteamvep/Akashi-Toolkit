package rikka.akashitoolkit.widget;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.MySpannableFactory;

/**
 * Created by Rikka on 2016/3/16.
 */
public class ButtonCardView extends FrameLayout {
    private LinearLayout mButtonContainer;
    private LinearLayout mButtonContainerOuter;
    private TextView mTitle;
    private TextView mBody;

    public ButtonCardView(Context context) {
        this(context, null);
    }

    public ButtonCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.button_card_view, this);
        mButtonContainer = (LinearLayout) findViewById(R.id.buttonCardViewButtonContainer);
        mButtonContainerOuter = (LinearLayout) findViewById(R.id.buttonCardViewButtonContainerOuter);
        mButtonContainerOuter.setVisibility(ButtonCardView.GONE);
        mTitle = (TextView) findViewById(R.id.text_title);
        mBody = (TextView) findViewById(R.id.text_body);
    }

    public ButtonCardView setTitle(int textId) {
        setTitle(getContext().getString(textId));
        return this;
    }

    public ButtonCardView setTitle(CharSequence text) {
        mTitle.setText(text);
        return this;
    }

    public ButtonCardView setMessage(int textId) {
        setMessage(getContext().getString(textId));
        return this;
    }

    public ButtonCardView setMessage(CharSequence text) {
        mBody.setText(text);
        return this;
    }

    public ButtonCardView setMessageHtml(String text) {
        mBody.setText(Html.fromHtml(text));
        mBody.setMovementMethod(LinkMovementMethod.getInstance());
        mBody.setClickable(true);
        mBody.setSpannableFactory(MySpannableFactory.getInstance());
        return this;
    }

    public ButtonCardView addButton(int textId) {
        addButton(getContext().getString(textId), null, false, true);
        return this;
    }

    public ButtonCardView addButton(CharSequence text) {
        addButton(text, null, false, true);
        return this;
    }

    public ButtonCardView addButton(int textId, boolean clickHide) {
        addButton(getContext().getString(textId), null, false, clickHide);
        return this;
    }

    public ButtonCardView addButton(CharSequence text, boolean clickHide) {
        addButton(text, null, false, clickHide);
        return this;
    }

    public ButtonCardView addButton(int textId, OnClickListener listener, boolean useAccentColor, boolean clickHide) {
        addButton(getContext().getString(textId), listener, useAccentColor, clickHide);
        return this;
    }

    public ButtonCardView addButton(CharSequence text, OnClickListener listener, boolean useAccentColor, boolean clickHide) {
        Button button = (Button) inflate(getContext(), R.layout.button_card_view_button, null);
        //Button button = new Button(getContext(), null, R.attr.borderlessButtonStyle);
        button.setText(text);
        if (listener != null) {
            button.setOnClickListener(listener);
        }
        //button.setMinWidth(0);

        if (useAccentColor) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getContext().getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);

            button.setTextColor(typedValue.data);
        }

        mButtonContainer.addView(button);
        mButtonContainerOuter.setVisibility(ButtonCardView.VISIBLE);

        if (clickHide) {
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
        }
        return this;
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
}
