package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by Rikka on 2016/8/9.
 */
public class EditTextAlertDialog extends AlertDialog {

    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClickListener(DialogInterface dialog, String text);
    }

    protected EditTextAlertDialog(@NonNull Context context) {
        super(context);
    }

    protected EditTextAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected EditTextAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return false;
            }
        });
    }

    /*/**
     *
     * @param builder Builder
     * @param resId 包含一个 id 为 @android:id/edit 的 EditText 的 layout
     * @param listener 确定或者输入法中确定
     */
    /*public EditTextAlertDialog(Builder builder, @LayoutRes int resId, String text, String hint, @Nullable final DialogInterface.OnClickListener listener) {
        builder
                .setView(resId)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onClick(dialogInterface, i);
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null);

        mDialog = builder.show();

        mEditText = (EditText) mDialog.getWindow().findViewById(android.R.id.edit);
        mEditText.setText(text);
        mEditText.setHint(hint);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENDCALL) {
                    mDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                    return true;
                }
                return false;
            }
        });


        mEditText.requestFocus();

        mEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mDialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }*/

    public static class Builder extends AlertDialog.Builder {

        private String mText;
        private String mHint;
        private EditText mEditText;
        private OnPositiveButtonClickListener mOnClickListener;
        private int mInputType;

        public Builder(@NonNull Context context) {
            super(context);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            super(context, themeResId);
        }

        @Override
        public Builder setPositiveButton(@StringRes int textId, OnClickListener listener) {
            return setPositiveButton(getContext().getString(textId), listener);
        }

        @Override
        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            listener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onPositiveButtonClickListener(dialog, mEditText.getText().toString());
                    }
                }
            };
            return (Builder) super.setPositiveButton(text, listener);
        }

        @Override
        public Builder setView(View view) {
            return (Builder) super.setView(view);
        }

        @Override
        public Builder setTitle(@StringRes int titleId) {
            return (Builder) super.setTitle(titleId);
        }

        @Override
        public Builder setTitle(CharSequence title) {
            return (Builder) super.setTitle(title);
        }

        @Override
        public Builder setCustomTitle(View customTitleView) {
            return (Builder) super.setCustomTitle(customTitleView);
        }

        @Override
        public Builder setMessage(@StringRes int messageId) {
            return (Builder) super.setMessage(messageId);
        }

        @Override
        public Builder setMessage(CharSequence message) {
            return (Builder) super.setMessage(message);
        }

        @Override
        public Builder setIcon(@DrawableRes int iconId) {
            return (Builder) super.setIcon(iconId);
        }

        @Override
        public Builder setIcon(Drawable icon) {
            return (Builder) super.setIcon(icon);
        }

        @Override
        public Builder setIconAttribute(@AttrRes int attrId) {
            return (Builder) super.setIconAttribute(attrId);
        }

        @Override
        public Builder setNegativeButton(@StringRes int textId, OnClickListener listener) {
            return (Builder) super.setNegativeButton(textId, listener);
        }

        @Override
        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            return (Builder) super.setNegativeButton(text, listener);
        }

        @Override
        public Builder setNeutralButton(@StringRes int textId, OnClickListener listener) {
            return (Builder) super.setNeutralButton(textId, listener);
        }

        @Override
        public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
            return (Builder) super.setNeutralButton(text, listener);
        }

        @Override
        public Builder setCancelable(boolean cancelable) {
            return (Builder) super.setCancelable(cancelable);
        }

        @Override
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            return (Builder) super.setOnCancelListener(onCancelListener);
        }

        @Override
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            return (Builder) super.setOnDismissListener(onDismissListener);
        }

        @Override
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            return (Builder) super.setOnKeyListener(onKeyListener);
        }

        @Override
        public Builder setItems(@ArrayRes int itemsId, OnClickListener listener) {
            return (Builder) super.setItems(itemsId, listener);
        }

        @Override
        public Builder setItems(CharSequence[] items, OnClickListener listener) {
            return (Builder) super.setItems(items, listener);
        }

        @Override
        public Builder setAdapter(ListAdapter adapter, OnClickListener listener) {
            return (Builder) super.setAdapter(adapter, listener);
        }

        @Override
        public Builder setCursor(Cursor cursor, OnClickListener listener, String labelColumn) {
            return (Builder) super.setCursor(cursor, listener, labelColumn);
        }

        @Override
        public Builder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            return (Builder) super.setMultiChoiceItems(itemsId, checkedItems, listener);
        }

        @Override
        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            return (Builder) super.setMultiChoiceItems(items, checkedItems, listener);
        }

        @Override
        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, OnMultiChoiceClickListener listener) {
            return (Builder) super.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener);
        }

        @Override
        public Builder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem, OnClickListener listener) {
            return (Builder) super.setSingleChoiceItems(itemsId, checkedItem, listener);
        }

        @Override
        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, OnClickListener listener) {
            return (Builder) super.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener);
        }

        @Override
        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener) {
            return (Builder) super.setSingleChoiceItems(items, checkedItem, listener);
        }

        @Override
        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, OnClickListener listener) {
            return (Builder) super.setSingleChoiceItems(adapter, checkedItem, listener);
        }

        @Override
        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            return (Builder) super.setOnItemSelectedListener(listener);
        }

        @Override
        public Builder setView(int layoutResId) {
            return (Builder) super.setView(layoutResId);
        }

        @Override
        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            return (Builder) super.setRecycleOnMeasureEnabled(enabled);
        }

        public Builder setEditText(@LayoutRes int resId, OnPositiveButtonClickListener listener) {
            return setEditText(resId, null, null, listener);
        }

        public Builder setEditText(@LayoutRes int resId, int inputType, OnPositiveButtonClickListener listener) {
            return setEditText(resId, null, null, inputType, listener);
        }

        public Builder setEditText(@LayoutRes int resId, String string, OnPositiveButtonClickListener listener) {
            return setEditText(resId, string, null, listener);
        }

        public Builder setEditText(@LayoutRes int resId, String string, int inputType, OnPositiveButtonClickListener listener) {
            return setEditText(resId, string, null, inputType, listener);
        }

        public Builder setEditText(@LayoutRes int resId, String text, String hint, OnPositiveButtonClickListener listener) {
            return setEditText(resId, text, hint, InputType.TYPE_CLASS_TEXT, listener);
        }

        public Builder setEditText(@LayoutRes int resId, String text, String hint, int inputType, OnPositiveButtonClickListener listener) {
            setView(resId);

            mText = text;
            mHint = hint;
            mOnClickListener = listener;
            mInputType = inputType;

            return this;
        }

        @Override
        public AlertDialog create() {
            return super.create();
        }

        @Override
        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();

            mEditText = (EditText) dialog.getWindow().findViewById(android.R.id.edit);
            mEditText.setText(mText);
            mEditText.setHint(mHint);
            mEditText.setInputType(mInputType);

            mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENDCALL) {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                        return true;
                    }
                    return false;
                }
            });


            mEditText.requestFocus();

            mEditText.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
                }
            });

            return dialog;
        }
    }
}
