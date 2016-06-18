package rikka.akashitoolkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Map;
import rikka.akashitoolkit.staticdata.MapList;
import rikka.akashitoolkit.ui.MapActivity;

/**
 * Created by Rikka on 2016/4/9.
 */
public class MapAdapter extends BaseRecyclerAdapter<ViewHolder.Map> {
    private List<Map> mData;
    private Context mContext;
    private int mType;

    public MapAdapter(final Context context, final int type) {
        mData = new ArrayList<>();
        mContext = context;
        mType = type;

        rebuildDataList();
    }

    @Override
    public ViewHolder.Map onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_map, parent, false);
        return new ViewHolder.Map(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder.Map holder, int position) {
        final Map item = mData.get(position);
        holder.mTitle.setText(item.getMap());

        Spanned htmlDescription = Html.fromHtml(format(item));
        String descriptionWithOutExtraSpace = htmlDescription.toString().trim();
        holder.mTextView.setText(htmlDescription.subSequence(0, descriptionWithOutExtraSpace.length()));

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra(MapActivity.EXTRA_ITEM_ID, item.getSea() * 10 + item.getArea());
                intent.putExtra(MapActivity.EXTRA_ITEM_NAME, item.getMap());
                mContext.startActivity(intent);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String format(Map item) {
        StringBuilder sb = new StringBuilder();

        boolean otherAtLast = item.getAirforce().size() > 1 && item.getOther().size() < 2;
        for (int id = 0; id < item.getAirforce().size(); id++) {
            boolean haveAir = item.getAirforce().get(id).get(0) != 0 &&
                    item.getAirforce().get(id).get(1) != 0 &&
                    item.getAirforce().get(id).get(2) != 0;

            if (haveAir) {
                sb.append(String.format("<b>敌制空 / 优势 / 确保</b><br>%d / %d / %d<br/>",
                        item.getAirforce().get(id).get(0),
                        item.getAirforce().get(id).get(1),
                        item.getAirforce().get(id).get(2)));
            }

            sb.append(String.format("<b>推荐配置</b><br>%s<br/><b>带路条件</b><br/>%s",
                    item.getRecommend().get(id),
                    item.getCondition().get(id)));

            if (!otherAtLast)
                sb.append(String.format("<br/><b>说明</b><br/>%s", item.getOther().get(id)));

            if (id < item.getAirforce().size() - 1)
                sb.append("<p/>");
        }

        if (otherAtLast)
            sb.append(String.format("<p/><b>说明</b><br/>%s", item.getOther().get(0)));

        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void rebuildDataList() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Map> data = MapList.get(mContext);
                mData = new ArrayList<>();

                for (Map item :
                        data) {
                    if (item.getSea() == mType) {
                        mData.add(item);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
            }
        }.execute();
    }
}
