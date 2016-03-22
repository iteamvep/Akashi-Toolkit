package rikka.akashitoolkit.widget;

import android.content.Context;
import android.util.AttributeSet;
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

    public void addDivider() {
        View view = new View(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
        view.setBackgroundResource(R.drawable.line_divider);
        view.setPadding(0, 0, 0, Utils.dpToPx(4));

        addView(view);
    }

    public View addTitle(String title) {
        View view = inflate(getContext(), R.layout.drawer_title, this);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(title);

        return textView;
    }
}
