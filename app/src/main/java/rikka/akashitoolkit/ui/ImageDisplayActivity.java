package rikka.akashitoolkit.ui;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerAdapter;
import rikka.akashitoolkit.utils.Utils;

public class ImageDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    private List<String> mList;
    private int mPosition;
    private AsyncTask mDownloadTask;
    private boolean[] mIsDownloaded;

    private FloatingActionButton mFAB;
    private TextView mTextView;
    private TextView mTextView2;
    private CoordinatorLayout mCoordinatorLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(this);
        mFAB.setVisibility(View.GONE);

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView2 = (TextView) findViewById(R.id.textView2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorImageDisplayBackground));
        }

        mList = getIntent().getStringArrayListExtra(EXTRA_URL);
        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
        mIsDownloaded = new boolean[mList.size()];

        mTextView.setText(Integer.toString(mPosition + 1));
        mTextView2.setText(Integer.toString(mList.size()));

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Bundle getArgs(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("POSITION", position);
                bundle.putString("URL", mList.get(position));
                return bundle;
            }
        };

        for (String ignored :
                mList) {
            adapter.addFragment(ImageFragment.class, "Image");
        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(99);
        viewPager.setCurrentItem(mPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != mPosition) {
                    mTextView.setText(Integer.toString(position + 1));
                    setFAB();
                }
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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

    private FutureTarget<File> mFileFutureTarget;

    @Override
    public void onClick(View v) {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
        }

        mDownloadTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                mFileFutureTarget = Glide.with(getApplicationContext())
                        .load(Utils.getGlideUrl(mList.get(mPosition)))
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                try {
                    File file = mFileFutureTarget.get();
                    File dst = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    String fileName = Uri.parse(mList.get(mPosition)).getLastPathSegment();
                    Utils.copyFile(file,
                            new File(dst.getAbsolutePath() + "/" + fileName));

                    return fileName;
                } catch (InterruptedException | ExecutionException | IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String filename) {
                if (filename != null) {
                    Snackbar.make(mCoordinatorLayout, "Saved" + " " + filename, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(mCoordinatorLayout, "Failed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }.execute();
    }

    public static class ImageFragment extends Fragment {
        private ImageView mImageView;
        private int mPosition;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.content_photo_view, container, false);
            mImageView = (ImageView) view.findViewById(R.id.imageView);

            mPosition = getArguments().getInt("POSITION");
            Log.d("ImageFragment", getArguments().getString("URL"));

            Glide.with(this)
                    .load(Utils.getGlideUrl(getArguments().getString("URL")))
                    .crossFade()
                    .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            ((ImageDisplayActivity) getActivity()).setIsDownloaded(mPosition, true);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            ((ImageDisplayActivity) getActivity()).setIsDownloaded(mPosition, true);
                            return false;
                        }
                    })
                    .into(mImageView);

            return view;
        }

        @Override
        public void onStop() {
            Glide.clear(mImageView);
            super.onStop();
        }
    }

    private void setFAB() {
        if (mIsDownloaded[mPosition]) {
            showFAB();
        } else {
            hideFAB();
        }
    }

    public void setIsDownloaded(int position, boolean isDownloaded) {
        mIsDownloaded[position] = isDownloaded;

        setFAB();
    }

    private void showFAB() {
        if (mFAB.getVisibility() == View.VISIBLE)
            return;

        mFAB.setVisibility(View.VISIBLE);
        ScaleAnimation anim = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        mFAB.startAnimation(anim);

        Log.d(getClass().getSimpleName(), "show FAB " + Integer.toString(mPosition));
    }

    private void hideFAB() {
        if (mFAB.getVisibility() == View.GONE)
            return;

        mFAB.setVisibility(View.GONE);

        Log.d(getClass().getSimpleName(), "hide FAB " + Integer.toString(mPosition));
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

    /*Glide.with(this)
                .load(getIntent().getStringExtra(EXTRA_URL))
                .crossFade()
                .into((ImageView) findViewById(R.id.imageView));*/

        /*mTask = new AsyncTask<Void, Void, File>() {
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
        }.execute();*/
}
