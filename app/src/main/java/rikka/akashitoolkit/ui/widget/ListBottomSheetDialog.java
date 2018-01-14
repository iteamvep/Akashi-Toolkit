package rikka.akashitoolkit.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.SimpleAdapter;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/8/6.
 */
public class ListBottomSheetDialog extends BottomSheetDialog {

    public ListBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    protected ListBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ListBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    public void setItems(@StringRes int[] resId, @Nullable final OnClickListener listener) {
        CharSequence[] items = new CharSequence[resId.length];
        for (int i = 0; i < resId.length; i++) {
            items[i] = getContext().getString(resId[i]);
        }
        setItems(items, listener);
    }

    public void setItems(CharSequence[] items, @Nullable final OnClickListener listener) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setPadding(0, Utils.dpToPx(8), 0, Utils.dpToPx(8));
        SimpleAdapter<String> adapter = new SimpleAdapter<>(R.layout.bottom_sheet_list_ltem);
        List<String> list = new ArrayList<>();
        for (CharSequence item : items) {
            list.add(item.toString());
        }
        adapter.setItemList(list);
        adapter.setOnItemClickListener(new SimpleAdapter.Listener() {
            @Override
            public void OnClick(int position) {
                if (listener != null) {
                    listener.onClick(ListBottomSheetDialog.this, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        setContentView(recyclerView);
    }
}
