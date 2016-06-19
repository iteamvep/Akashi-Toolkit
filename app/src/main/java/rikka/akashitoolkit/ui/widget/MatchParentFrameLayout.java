package rikka.akashitoolkit.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * bad way to fix width = 0
 */
public class MatchParentFrameLayout extends FrameLayout {
    public MatchParentFrameLayout(Context context) {
        super(context);
    }

    public MatchParentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchParentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatchParentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getLayoutParams().width = ((ViewGroup) getParent()).getWidth();
    }
}
