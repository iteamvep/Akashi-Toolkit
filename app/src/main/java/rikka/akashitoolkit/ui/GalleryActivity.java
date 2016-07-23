package rikka.akashitoolkit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.GalleryAdapter;
import rikka.akashitoolkit.support.StaticData;

public class GalleryActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static void start(Context context, List<String> url, String title) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putStringArrayListExtra(EXTRA_URL, (ArrayList<String>) url);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    private int mItemSize;
    private int mSpanCount;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));

        mItemSize = -1;

        boolean horizontal = getResources().getBoolean(R.bool.is_horizontal);
        boolean tablet = StaticData.instance(this).isTablet;

        mSpanCount = horizontal ? (tablet ? 5 : 4) : 3;

        mData = getIntent().getStringArrayListExtra(EXTRA_URL);

        final GalleryAdapter adapter = new GalleryAdapter(R.layout.item_image) {
            @Override
            public void onItemClicked(View v, List<String> data, int position) {
                ImagesActivity.start(v.getContext(), data, position, null);
            }

            @Override
            public void onCreateImageView(ImageView imageView) {
                imageView.setLayoutParams(new FrameLayout.LayoutParams(mItemSize, mItemSize));
            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(this, mSpanCount);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.content_container);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mItemSize = v.getWidth() / mSpanCount;
                adapter.setData(mData);

                v.removeOnLayoutChangeListener(this);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
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
}
