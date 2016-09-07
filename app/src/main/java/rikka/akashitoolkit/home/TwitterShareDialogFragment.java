package rikka.akashitoolkit.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Twitter;
import rikka.akashitoolkit.support.Settings;
import rikka.akashitoolkit.ui.widget.MySpannableFactory;
import rikka.akashitoolkit.utils.HtmlUtils;
import rikka.akashitoolkit.utils.Utils;

/**
 * Created by Rikka on 2016/9/7.
 */
public class TwitterShareDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private Twitter data;
    private String url;

    private ImageView mImageView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        data = bundle.getParcelable("DATA");
        url = bundle.getString("URL");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert)
                .setPositiveButton(R.string.share, this)
                .setTitle(R.string.preview);

        FrameLayout frameLayout = new FrameLayout(getContext());
        mImageView = new ImageView(getContext());
        initView();
        frameLayout.setPadding(Utils.dpToPx(16), 0, Utils.dpToPx(16), 0);
        frameLayout.addView(mImageView);
        builder.setView(frameLayout);

        return builder.create();
    }

    private void initView() {
        Context context = new ContextThemeWrapper(getContext(), R.style.AppTheme_Light);

        final View view = LayoutInflater.from(context).inflate(R.layout.card_twitter_inside_image, null);
        view.setDrawingCacheEnabled(true);

        ViewHolder holder = new ViewHolder(view);
        holder.name.setText("「艦これ」開発/運営");

        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .into(holder.avatar);
        }

        holder.content.setText(HtmlUtils.fromHtml(data.getJp()));

        String translated = data.getZh();
        if (TextUtils.isEmpty(translated)) {
            holder.translated.setVisibility(View.GONE);
        } else {
            holder.translated.setText(HtmlUtils.fromHtml(translated));
            holder.translated.setEnabled(true);
        }

        holder.time.setText(DateUtils.formatDateTime(context, data.getTimestamp(), DateUtils.FORMAT_SHOW_YEAR));

        if (!TextUtils.isEmpty(data.getImg())) {
            Glide.with(context)
                    .load(data.getImg())
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        if (Settings.instance(getContext()).getBoolean(Settings.SHARE_NO_FOOTER, false)) {
            holder.footer.setVisibility(View.GONE);
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        mImageView.setImageBitmap(view.getDrawingCache());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mImageView.getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            File file = new File(getContext().getExternalCacheDir(), "image.png");
            if (!file.getParentFile().exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
            }

            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setType("image/*");
                startActivity(Intent.createChooser(intent, "send to"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class ViewHolder {

        public final ImageView avatar;
        public final TextView name;
        public final TextView content;
        public final TextView translated;
        public final TextView time;
        public final ImageView image;
        public final View footer;

        public ViewHolder(View itemView) {
            avatar = (ImageView) itemView.findViewById(R.id.image_twitter_avatar);
            name = (TextView) itemView.findViewById(R.id.text_twitter_name);
            content = (TextView) itemView.findViewById(R.id.text_twitter_content);
            translated = (TextView) itemView.findViewById(R.id.text_twitter_content_translated);
            time = (TextView) itemView.findViewById(R.id.text_twitter_time);
            image = (ImageView) itemView.findViewById(R.id.image_twitter_content);
            footer = itemView.findViewById(android.R.id.text2);

            content.setSpannableFactory(MySpannableFactory.getInstance());
            translated.setSpannableFactory(MySpannableFactory.getInstance());
            content.setMovementMethod(LinkMovementMethod.getInstance());
            translated.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
