package rikka.akashitoolkit.utils;

import android.text.Spannable;
import android.text.style.URLSpan;

/**
 * Created by Rikka on 2016/4/6.
 */
public class MySpannableFactory extends Spannable.Factory {
    private final static Spannable.Factory sInstance = new MySpannableFactory();

    public static Spannable.Factory getInstance() {
        return sInstance;
    }

    @Override
    public Spannable newSpannable(CharSequence source) {
        return new SpannableString(source);
    }

    private static class SpannableString extends android.text.SpannableString {
        public SpannableString(CharSequence source) {
            super(source);
        }

        @Override
        public void setSpan(Object what, int start, int end, int flags) {
            if (what instanceof URLSpan) {
                what = new MyUrlSpan((URLSpan) what);
            }
            super.setSpan(what, start, end, flags);
        }
    }
}
