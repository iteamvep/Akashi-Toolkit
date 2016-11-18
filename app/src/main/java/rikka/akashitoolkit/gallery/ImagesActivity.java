package rikka.akashitoolkit.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.adapter.ViewPagerStateAdapter;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.utils.FileUtils;
import rikka.akashitoolkit.utils.Utils;

public class ImagesActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ImagesActivity";

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DOWNLOADABLE = "EXTRA_DOWNLOADABLE";

    private List<String> mList;
    private int mPosition;
    private AsyncTask mDownloadTask;
    private boolean[] mIsDownloaded;
    private boolean mDownloadable;

    private FloatingActionButton mFAB;
    private TextView mTextView;
    private TextView mTextView2;
    private CoordinatorLayout mCoordinatorLayout;

    public static void start(Context context, String url) {
        start(context, new String[]{url}, 0);
    }

    public static void start(Context context, String[] url, int position) {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(url));
        start(context, list, position, null);
    }

    public static void start(Context context, List<String> url, int position, String title) {
        start(context, url, position, title, true);
    }

    public static void start(Context context, List<String> url, int position, String title, boolean downloadable) {
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putStringArrayListExtra(ImagesActivity.EXTRA_URL, (ArrayList<String>) url);
        intent.putExtra(ImagesActivity.EXTRA_POSITION, position);
        intent.putExtra(ImagesActivity.EXTRA_TITLE, title);
        intent.putExtra(ImagesActivity.EXTRA_DOWNLOADABLE, downloadable);
        context.startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(getIntent().getStringExtra(EXTRA_TITLE), null, ContextCompat.getColor(this, R.color.background)));
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(this);
        mFAB.setVisibility(View.GONE);

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView2 = (TextView) findViewById(R.id.textView2);

        mList = getIntent().getStringArrayListExtra(EXTRA_URL);
        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 0);
        if (mPosition == -1) {
            Log.e(TAG, "position is -1, set to 0");
            mPosition = 0;
        }
        mIsDownloaded = new boolean[mList.size()];

        mDownloadable = getIntent().getBooleanExtra(EXTRA_DOWNLOADABLE, true);

        for (String s : mList) {
            Log.d(getClass().getSimpleName(), s);
        }

        if (mList.size() == 1) {
            findViewById(R.id.content_container).setVisibility(View.GONE);
        }

        mTextView.setText(Integer.toString(mPosition + 1));
        mTextView2.setText(Integer.toString(mList.size()));

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerStateAdapter adapter = new ViewPagerStateAdapter(getSupportFragmentManager()) {
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 0);
            } else {
                startDownload(null);
            }
        } else {
            StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
            StorageVolume volume = sm.getPrimaryStorageVolume();
            Intent intent = volume.createAccessIntent(Environment.DIRECTORY_PICTURES);
            startActivityForResult(intent, 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            getContentResolver().takePersistableUriPermission(data.getData(),
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            startDownload(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload(null);
                } else {
                    Snackbar.make(mCoordinatorLayout, getString(R.string.require_write_external_storage_permission), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    class TaskReturn {
        String filename;
        Uri uri;

        public TaskReturn(String filename, Uri uri) {
            this.filename = filename;
            this.uri = uri;
        }
    }

    private void startDownload(final Uri data) {
        mDownloadTask = new AsyncTask<Void, Void, TaskReturn>() {
            @Override
            protected void onPreExecute() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 0);
                    }
                }
            }

            @Override
            protected TaskReturn doInBackground(Void... params) {
                mFileFutureTarget = Glide.with(getApplicationContext())
                        .load(Utils.getGlideUrl(mList.get(mPosition)))
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                try {
                    String fileName = "/AkashiToolkit/" + Uri.parse(mList.get(mPosition)).getLastPathSegment();

                    File src = mFileFutureTarget.get();
                    File dst = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    dst = new File(dst.getAbsolutePath() + fileName);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        FileUtils.copyFile(src, dst);

                        return new TaskReturn(Environment.DIRECTORY_PICTURES + fileName, null);
                    } else {
                        DocumentFile pickedDir = DocumentFile.fromTreeUri(ImagesActivity.this, data);
                        DocumentFile dir = pickedDir.findFile("AkashiToolkit");

                        if (dir == null)
                            pickedDir = pickedDir.createDirectory("AkashiToolkit");
                        else
                            pickedDir = dir;

                        Uri file = Uri.parse(mList.get(mPosition));
                        DocumentFile newFile = pickedDir.findFile(file.getLastPathSegment());
                        if (newFile != null) {
                            newFile.delete();
                        }
                        newFile = pickedDir.createFile(Utils.getMimeType(fileName), file.getLastPathSegment());

                        try {
                            OutputStream out = getContentResolver().openOutputStream(newFile.getUri());
                            if (out == null) {
                                return null;
                            }

                            InputStream in = new FileInputStream(src);

                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            in.close();
                            out.close();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }

                        return new TaskReturn(Uri.parse(mList.get(mPosition)).getLastPathSegment(), newFile.getUri());
                    }
                } catch (InterruptedException | ExecutionException | IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(final TaskReturn data) {
                if (data != null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        Snackbar.make(mCoordinatorLayout, String.format(getString(R.string.saved), data.filename), Snackbar.LENGTH_LONG)
                                .setAction(R.string.open, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + data.filename)), "image/*");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else {
                        Context context = ImagesActivity.this;

                        File picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        File imagePath = new File(picturePath, "AkashiToolkit");
                        //File image = new File(imagePath, data.filename);

                        final Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setType("image/*");
                        intent.setData(data.uri);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            context.grantUriPermission(packageName, data.uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }

                        Snackbar.make(mCoordinatorLayout, String.format(getString(R.string.saved), Uri.fromFile(picturePath).getLastPathSegment() + "/AkashiToolkit/" + data.filename), Snackbar.LENGTH_LONG)
                                .setAction(R.string.open, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                } else {
                    Snackbar.make(mCoordinatorLayout, getString(R.string.save_failed), Snackbar.LENGTH_LONG)
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
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (e != null) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            ((ImagesActivity) getActivity()).setIsDownloaded(mPosition, false);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            ((ImagesActivity) getActivity()).setIsDownloaded(mPosition, true);
                            return false;
                        }
                    })
                    .into(mImageView);

            return view;
        }

        @Override
        public void onDestroyView() {
            Glide.clear(mImageView);
            super.onDestroyView();
        }
    }

    private void setFAB() {
        if (!mDownloadable) {
            return;
        }

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

        mFAB.clearAnimation();

        mFAB.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new FastOutSlowInInterpolator());
        scaleAnimation.setDuration(300);

        // avoid missing frame?
        scaleAnimation.setStartOffset(500);
        mFAB.startAnimation(scaleAnimation);

        //Log.d(getClass().getSimpleName(), "show FAB " + Integer.toString(mPosition));
    }

    private void hideFAB() {
        if (mFAB.getVisibility() == View.GONE)
            return;

        if (mFAB.getVisibility() == View.VISIBLE) {
            mFAB.clearAnimation();

            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(300);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mFAB.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFAB.startAnimation(scaleAnimation);
        } else {
            mFAB.setVisibility(View.GONE);
        }

        Log.d(getClass().getSimpleName(), "hide FAB " + Integer.toString(mPosition));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void setNavigationBarColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);

        getWindow().setNavigationBarColor(typedValue.data);
    }
}
