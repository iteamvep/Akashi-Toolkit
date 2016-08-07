package rikka.akashitoolkit.expedition;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.widget.ExpandableLayout;

/**
 * Created by Rikka on 2016/8/7.
 */
public class ExpeditionViewHolder extends RecyclerView.ViewHolder {
    public ExpandableLayout mExpandableLayout;
    public TextView[] mRewardNumber;
    public TextView[] mRequireNumber;
    public TextView mTitle;
    public TextView mTime;
    public TextView mReward;
    public TextView mFleetRequire;
    public TextView mShipRequire;

    public ExpeditionViewHolder(View itemView) {
        super(itemView);

        mExpandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandableLinearLayout);

        mTitle = (TextView) itemView.findViewById(android.R.id.title);
        mTime = (TextView) itemView.findViewById(R.id.text_time);

        mRewardNumber = new TextView[4];
        mRewardNumber[0] = (TextView) itemView.findViewById(R.id.text_number_0);
        mRewardNumber[1] = (TextView) itemView.findViewById(R.id.text_number_1);
        mRewardNumber[2] = (TextView) itemView.findViewById(R.id.text_number_2);
        mRewardNumber[3] = (TextView) itemView.findViewById(R.id.text_number_3);
        mReward = (TextView) itemView.findViewById(R.id.text_reward);
        mRequireNumber = new TextView[3];
        mRequireNumber[0] = (TextView) itemView.findViewById(R.id.text_number_4);
        mRequireNumber[1] = (TextView) itemView.findViewById(R.id.text_number_5);
        mRequireNumber[2] = (TextView) itemView.findViewById(R.id.text_number_6);

        mFleetRequire = (TextView) itemView.findViewById(R.id.text_fleet_require);
        mShipRequire = (TextView) itemView.findViewById(R.id.text_ship_require);
    }

    public void setRewardResource(String str, int i) {
        setRewardResource(Html.fromHtml(str), i);
    }

    public void setRewardResource(Spanned str, int i) {
        if (TextUtils.isEmpty(str)) {
            mRewardNumber[i].setText("0");
        } else {
            mRewardNumber[i].setText(str);
        }
    }

    public void setRequireResource(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            mRequireNumber[i].setText("0");
        } else {
            mRequireNumber[i].setText(str);
        }
    }

    public void setRewardText(@Nullable String str1, @Nullable String str2) {
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(str1)) {
            sb.append(str1);
        }

        if (!TextUtils.isEmpty(str2)) {
            if (sb.length() > 0) {
                sb.append("<br>");
            }

            sb.append(str2);
        }

        setRewardNumber(Html.fromHtml(sb.toString()));
    }

    public void setRewardNumber(@Nullable Spanned str) {
        if (TextUtils.isEmpty(str)) {
            mReward.setVisibility(View.GONE);
        } else {
            mReward.setVisibility(View.VISIBLE);
            mReward.setText(str);
        }
    }

    @SuppressLint("DefaultLocale")
    public void setFleetRequire(int totalLevel, int flagshipLevel, int minShips) {
        StringBuilder sb = new StringBuilder();
        if (totalLevel != 0) {
            sb.append(String.format("舰队总等级 %d", totalLevel));
        }

        if (flagshipLevel != 0) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(String.format("旗舰等级 %d", flagshipLevel));
        }

        if (minShips != 0) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(String.format("最低舰娘数 %d", minShips));
        }

        mFleetRequire.setText(sb.toString());
    }

    public void setShipRequire(@Nullable String ship, @Nullable String bucket) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(ship)) {
            sb.append(ship.replace(" ", "<br>"));
        }

        if (!TextUtils.isEmpty(bucket)) {
            if (sb.length() > 0) {
                sb.append("<br>");
            }
            sb.append(bucket);
        }

        mShipRequire.setText(Html.fromHtml(sb.toString()));
    }
}
