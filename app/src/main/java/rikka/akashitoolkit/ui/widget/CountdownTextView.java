package rikka.akashitoolkit.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.util.AttributeSet;

/**
 * Created by Rikka on 2016/6/17.
 */
public class CountdownTextView extends AppCompatTextView {

    private CountDownTimer mCountDownTimer;
    private boolean mIsCountdown;
    private long mEndTime;
    private String mFinishText;

    public CountdownTextView(Context context) {
        this(context, null);
    }

    public CountdownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mEndTime = -1;
    }

    public void setEndTime(long endTime) {
        mEndTime = endTime;

        start();
    }

    public boolean isCounting() {
        return mCountDownTimer != null && mEndTime > System.currentTimeMillis();
    }

    public String getFinishText() {
        return mFinishText;
    }

    public void setFinishText(@StringRes int resId) {
        setFinishText(getContext().getString(resId));
    }

    public void setFinishText(String finishText) {
        mFinishText = finishText;
    }

    private void start() {
        cancel();

        if (mEndTime != -1) {
            mCountDownTimer = new CountDownTimer(this, mEndTime - System.currentTimeMillis(), 1000);
            mCountDownTimer.start();
        }
    }

    private void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        cancel();

        super.onDetachedFromWindow();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        state.isCountdown = mIsCountdown;
        state.endTime = mEndTime;

        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mIsCountdown = ss.isCountdown;
    }

    public static class SavedState extends BaseSavedState {
        boolean isCountdown;
        long endTime;

        public SavedState(Parcel source) {
            super(source);
            isCountdown = source.readInt() != 0;
            endTime = source.readLong();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isCountdown ? 1 : 0);
            out.writeLong(endTime);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private static class CountDownTimer extends android.os.CountDownTimer {

        private CountdownTextView mTextView;

        public CountDownTimer(CountdownTextView textView, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

            mTextView = textView;
        }

        @Override
        public void onTick(long l) {
            mTextView.setText(formatTimeLeft(l));
        }

        @Override
        public void onFinish() {
            mTextView.setText(mTextView.getFinishText());
        }

        @SuppressLint("DefaultLocale")
        protected String formatTimeLeft(long time) {
            StringBuilder sb = new StringBuilder();

            sb.append(String.format("%02d:", time / DateUtils.HOUR_IN_MILLIS));
            time = time % DateUtils.HOUR_IN_MILLIS;

            sb.append(String.format("%02d:", time / DateUtils.MINUTE_IN_MILLIS));
            time = time % DateUtils.MINUTE_IN_MILLIS;

            sb.append(String.format("%02d", time / DateUtils.SECOND_IN_MILLIS));

            return sb.toString();
        }
    }
}
