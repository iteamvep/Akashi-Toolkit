package rikka.akashitoolkit.ship_detail;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.detail.IllustrationViewHolder;
import rikka.akashitoolkit.model.ExtraIllustration;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.staticdata.ExtraIllustrationList;
import rikka.akashitoolkit.utils.Utils;
import rikka.akashitoolkit.viewholder.IBindViewHolder;

/**
 * Created by Rikka on 2016/9/18.
 */
public class ShipDetailIllustrationViewHolder extends IllustrationViewHolder implements IBindViewHolder<Ship> {

    public ShipDetailIllustrationViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Ship data, int position) {
        if (data.isEnemy()) {
            mAdapter.setImageWidth(Utils.dpToPx(150));
            mAdapter.setImageHeight(Utils.dpToPx(150));
            mAdapter.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            mAdapter.setImageWidth(Utils.dpToPx(150));
            mAdapter.setImageHeight(Utils.dpToPx(300));
            mAdapter.setImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mAdapter.setImagePadding(Utils.dpToPx(8));
        }
        mAdapter.setUrls(getIllustrationUrls(data));
    }

    private List<String> getIllustrationUrls(Ship data) {
        List<String> list = new ArrayList<>();

        if (data.getWikiId().equals("030a")
                || data.getWikiId().equals("026a")
                || data.getWikiId().equals("027a")
                || data.getWikiId().equals("042a")
                || data.getWikiId().equals("065a")
                || data.getWikiId().equals("094a")
                || data.getWikiId().equals("183a")) {
            list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sIllust.png", data.getWikiId())));
            list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sDmgIllust.png", data.getWikiId())));

        } else {
            if (!data.isEnemy()) {
                list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sIllust.png", data.getWikiId().replace("a", ""))));
                list.add(Utils.getKCWikiFileUrl(String.format("KanMusu%sDmgIllust.png", data.getWikiId().replace("a", ""))));
            } else {
                //敌舰立绘
                list.add(Utils.getKCWikiFileUrl(String.format("ShinkaiSeikan%s.png", data.getWikiId().substring(1,data.getWikiId().length()))));
            }
        }

        ExtraIllustration extraIllustration = ExtraIllustrationList.findItemById(itemView.getContext(), data.getWikiId());
        if (extraIllustration != null) {
            for (String name :
                    extraIllustration.getImage()) {
                list.add(Utils.getKCWikiFileUrl(name));
            }
        }

        return list;
    }
}
