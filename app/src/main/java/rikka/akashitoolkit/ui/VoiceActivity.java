package rikka.akashitoolkit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.VoiceAdapter;
import rikka.akashitoolkit.model.ShipVoice;
import rikka.akashitoolkit.support.MusicPlayer;
import rikka.akashitoolkit.ui.widget.BaseRecyclerViewItemDecoration;

/**
 * Created by Rikka on 2016/7/25.
 */
public class VoiceActivity extends BaseActivity {

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static void start(Context context, List<ShipVoice> data, String title) {
        List<String> list = new ArrayList<>();
        for (ShipVoice item : data) {
            list.add(String.format("%s|||%s|||%s|||%s", item.getUrl(), item.getScene(), item.getJp(), item.getZh()));
        }

        Intent intent = new Intent(context, VoiceActivity.class);
        intent.putStringArrayListExtra(EXTRA_DATA, (ArrayList<String>) list);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));

        List<Object> list = new ArrayList<>();
        if (getIntent().hasExtra(EXTRA_DATA)) {
            for (String str : getIntent().getStringArrayListExtra(EXTRA_DATA)) {
                String[] s = str.split("\\|\\|\\|");
                ShipVoice item = new ShipVoice();
                item.setUrl(s[0]);
                item.setScene(s[1]);
                item.setJp(s[2]);
                item.setZh(s[3]);
                list.add(item);
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.content_container);
        recyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.cardBackground));

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BaseRecyclerViewItemDecoration(this));
        recyclerView.setAdapter(new VoiceAdapter(list));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MusicPlayer.stop();
    }
}
