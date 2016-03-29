package rikka.akashitoolkit.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import rikka.akashitoolkit.R;

/**
 * Created by etby on 16-3-29.
 */
public class ImageDialogFragment extends DialogFragment {

    public static final String DIALOG_TAG = "DIALOG_TAG";

    private static final String IMAGE_URL = "IMAGE_URL";

    private ImageView mImageView;
    private String mImageUrl;

    public static void showDialog(FragmentManager manager, String imageUrl) {
        getInstance(imageUrl).show(manager, DIALOG_TAG);
    }

    private static ImageDialogFragment getInstance(String imageUrl) {
        ImageDialogFragment dialogFragment = new ImageDialogFragment();

        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imageUrl);

        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        try {
            Bundle arguments = getArguments();
            mImageUrl = arguments.getString(IMAGE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_image, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(mImageUrl)) {
            Toast.makeText(getContext(), R.string.image_load_error, Toast.LENGTH_LONG).show();
            return;
        }

        Glide.with(getContext())
                .load(mImageUrl)
                .crossFade()
                .into(mImageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.clear(mImageView);
    }
}
