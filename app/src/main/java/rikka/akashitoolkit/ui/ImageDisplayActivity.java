package rikka.akashitoolkit.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.Utils;

public class ImageDisplayActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "EXTRA_URL";

    private boolean mIsDestroyed;
    private AsyncTask mTask;
    private File mFile;
    private String mFileName;

    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorImageDisplayBackground));
        }

        Glide.with(this)
                .load(getIntent().getStringExtra(EXTRA_URL))
                .crossFade()
                .into((ImageView) findViewById(R.id.imageView));

        mTask = new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... params) {
                String url = getIntent().getStringExtra(EXTRA_URL);

                File dst = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                mFileName = dst.getAbsolutePath() + "/" + Uri.parse(url).getLastPathSegment();

                FutureTarget<File> future = Glide.with(getApplicationContext())
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                try {
                    mFile = future.get();

                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final File file) {
                //mFAB.show();

                mFAB.setVisibility(View.VISIBLE);
                ScaleAnimation anim = new ScaleAnimation(0f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(300);
                mFAB.startAnimation(anim);

                if (!mIsDestroyed && file != null) {
                    Glide.with(ImageDisplayActivity.this)
                            .load(file)
                            .crossFade()
                            .into((ImageView) findViewById(R.id.imageView));
                }

                mFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... params) {
                                try {
                                    Utils.copyFile(mFile, new File(mFileName));
                                    return true;

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                            }

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                if (aBoolean) {
                                    Snackbar.make(view, "Saved" + " " + mFileName, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(view, "Failed", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        }.execute();
                    }
                });
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        mIsDestroyed = true;
        mTask.cancel(true);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    for (final String url :
                urlList) {
            Log.d(MapActivity.class.getSimpleName(), url);

            if (url != null) {
                final ImageView imageView = (ImageView) LayoutInflater.from(this)
                        .inflate(R.layout.item_illustrations, container, false)
                        .findViewById(R.id.imageView);

                container.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShipDisplayActivity.this, ImageDisplayActivity.class);
                        intent.putExtra(ImageDisplayActivity.EXTRA_URL, url);
                        startActivity(intent);
                    }
                });

                mDownloadTask = new AsyncTask<Void, Void, File>() {
                    @Override
                    protected File doInBackground(Void... params) {
                        FutureTarget<File> future = Glide.with(getApplicationContext())
                                .load(url)
                                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        try {
                            File file = future.get();
                            File dst = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            if (dst != null) {
                                Utils.copyFile(file,
                                        new File(dst.getAbsolutePath() + "/" + Uri.parse(url).getLastPathSegment()));
                            }

                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(File file) {
                        if (!mIsDestroyed && file != null) {
                            Glide.with(ShipDisplayActivity.this)
                                    .load(file)
                                    .crossFade()
                                    .into(imageView);
                        }
                    }
                }.execute();
            }
     */
}
