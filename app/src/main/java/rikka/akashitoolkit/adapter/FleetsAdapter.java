package rikka.akashitoolkit.adapter;

import android.animation.StateListAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.animation.AnimatorInflater;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.viewholder.Fleets;

/**
 * Created by Rikka on 2016/7/20.
 */
public class FleetsAdapter extends RecyclerView.Adapter<Fleets> {
    @Override
    public Fleets onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fleet, parent, false);
        return new Fleets(itemView);
    }

    @Override
    public void onBindViewHolder(Fleets holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 20;
    }
}
