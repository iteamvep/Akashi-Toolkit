package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import rikka.akashitoolkit.utils.MySpannableFactory;

/**
 * Created by Rikka on 2016/4/9.
 */
public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSpannableFactory(MySpannableFactory.getInstance());
    }
}
