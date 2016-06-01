package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/3/21.
 */
public class SimpleDrawerView extends LinearLayout {
    public SimpleDrawerView(Context context) {
        this(context, null);
    }

    public SimpleDrawerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addDividerHead() {
        View view = new View(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        //lp.setMargins(0, 0, 0, Utils.dpToPx(4));
        view.setLayoutParams(lp);
        view.setBackgroundResource(R.drawable.line_divider);

        addView(view);
    }

    public void addDivider() {
        View view = new View(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(0, Utils.dpToPx(4), 0, Utils.dpToPx(4));
        view.setLayoutParams(lp);
        view.setBackgroundResource(R.drawable.line_divider);

        addView(view);
    }

    public View addTitle(String title) {
        if (getChildCount() != 0) {
            View view = new View(getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            view.setBackgroundResource(R.drawable.line_divider);

            addView(view);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.drawer_title, this, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(title);
        addView(view);

        return textView;
    }
}
