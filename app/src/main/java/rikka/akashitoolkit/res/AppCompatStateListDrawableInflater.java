package rikka.akashitoolkit.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/10/8.
 * 只是用来为了自己用的东西少些一些代码而已.. 并没有什么意义的东西..
 */

public class AppCompatStateListDrawableInflater {

    private AppCompatStateListDrawableInflater() {
    }

    @NonNull
    public static Drawable createFromXml(@NonNull Resources r, @NonNull XmlPullParser parser,
                                         @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        final AttributeSet attrs = Xml.asAttributeSet(parser);

        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG
                && type != XmlPullParser.END_DOCUMENT) {
            // Seek parser to start tag.
        }

        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }

        return createFromXmlInner(r, parser, attrs, theme);
    }

    @NonNull
    private static Drawable createFromXmlInner(@NonNull Resources r,
                                               @NonNull XmlPullParser parser, @NonNull AttributeSet attrs,
                                               @Nullable Resources.Theme theme)
            throws XmlPullParserException, IOException {
        final String name = parser.getName();
        if (!name.equals("animated-selector") && !name.equals("selector")) {
            throw new XmlPullParserException(
                    parser.getPositionDescription() + ": invalid animated state list drawable tag " + name);
        }

        return inflate(r, parser, attrs, theme);
    }

    private static Drawable inflate(@NonNull Resources r, @NonNull XmlPullParser parser,
                                    @NonNull AttributeSet attrs, @Nullable Resources.Theme theme)
            throws XmlPullParserException, IOException {
        final int innerDepth = parser.getDepth() + 1;
        int depth;
        int type;

        Drawable drawable;
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = new AnimatedStateListDrawable();
        } else {
            drawable = new StateListDrawable();
        }

        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && ((depth = parser.getDepth()) >= innerDepth || type != XmlPullParser.END_TAG)) {
            if (type != XmlPullParser.START_TAG || depth > innerDepth) {
                continue;
            }

            Drawable innerDrawable;
            TypedArray a;

            switch (parser.getName()) {
                case "item":
                    a = obtainAttributes(r, theme, attrs, R.styleable.AnimatedStateListDrawableItem);
                    innerDrawable = null;
                    int id = 0;
                    if (a.hasValue(R.styleable.AnimatedStateListDrawableItem_android_drawable)) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            innerDrawable = a.getDrawable(R.styleable.AnimatedStateListDrawableItem_android_drawable);
                        } else {
                            int resId = a.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_drawable, 0);
                            innerDrawable = VectorDrawableCompat.create(r, resId, theme);
                        }
                    }
                    if (a.hasValue(R.styleable.AnimatedStateListDrawableItem_android_id)) {
                        id = a.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_id, 0);
                    }

                    a.recycle();

                    // Parse all unrecognized attributes as state specifiers.
                    int j = 0;
                    final int numAttrs = attrs.getAttributeCount();
                    int[] stateSpec = new int[numAttrs];
                    for (int i = 0; i < numAttrs; i++) {
                        final int stateResId = attrs.getAttributeNameResource(i);
                        if (stateResId != android.R.attr.drawable && stateResId != android.R.attr.id) {
                            // Unrecognized attribute, add to state set
                            stateSpec[j++] = attrs.getAttributeBooleanValue(i, false)
                                    ? stateResId : -stateResId;
                        }
                    }
                    stateSpec = StateSet.trimStateSet(stateSpec, j);

                    if (Build.VERSION.SDK_INT >= 21) {
                        if (innerDrawable == null) {
                            throw new XmlPullParserException(
                                    parser.getPositionDescription() + ": drawable is null");
                        }
                        ((AnimatedStateListDrawable) drawable).addState(stateSpec, innerDrawable, id);
                    } else {
                        ((StateListDrawable) drawable).addState(stateSpec, innerDrawable);
                    }
                    break;
                case "transition":
                    if (Build.VERSION.SDK_INT >= 21) {
                        a = obtainAttributes(r, theme, attrs, R.styleable.AnimatedStateListDrawableTransition);

                        innerDrawable = null;
                        int fromId = 0, toId = 0;
                        boolean reversible = false;
                        if (a.hasValue(R.styleable.AnimatedStateListDrawableTransition_android_drawable)) {
                            innerDrawable = a.getDrawable(R.styleable.AnimatedStateListDrawableTransition_android_drawable);
                        }
                        if (a.hasValue(R.styleable.AnimatedStateListDrawableTransition_android_fromId)) {
                            fromId = a.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_fromId, 0);
                        }
                        if (a.hasValue(R.styleable.AnimatedStateListDrawableTransition_android_toId)) {
                            toId = a.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_toId, 0);
                        }
                        if (a.hasValue(R.styleable.AnimatedStateListDrawableTransition_android_reversible)) {
                            reversible = a.getBoolean(R.styleable.AnimatedStateListDrawableTransition_android_reversible, false);
                        }

                        a.recycle();

                        if (innerDrawable == null || !(innerDrawable instanceof AnimatedVectorDrawable)) {
                            throw new XmlPullParserException(
                                    parser.getPositionDescription() + ": drawable is null or not AnimatedVectorDrawable");
                        }
                        ((AnimatedStateListDrawable) drawable).addTransition(fromId, toId, (AnimatedVectorDrawable) innerDrawable, reversible);
                    }
                    break;
            }
        }

        return drawable;
    }

    private static TypedArray obtainAttributes(Resources res, Resources.Theme theme,
                                               AttributeSet set, int[] attrs) {
        return theme == null ? res.obtainAttributes(set, attrs)
                : theme.obtainStyledAttributes(set, attrs, 0, 0);
    }
}
