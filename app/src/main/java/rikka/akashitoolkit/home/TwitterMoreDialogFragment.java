package rikka.akashitoolkit.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Twitter;
import rikka.akashitoolkit.utils.ClipBoardUtils;
import rikka.akashitoolkit.utils.HtmlUtils;

/**
 * Created by Rikka on 2016/6/1.
 */
public class TwitterMoreDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private Twitter data;
    private String url;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        data = bundle.getParcelable("DATA");
        url = bundle.getString("URL");

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
                .setItems(new CharSequence[]{
                        "View original Tweet",
                        getString(R.string.share),
                        getString(R.string.share_by_image),
                        getString(R.string.copy_original_text),
                        getString(R.string.copy_translated_text)
                }, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://twitter.com/KanColle_STAFF/status/%s", data.getId()))));
                break;
            case 1:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, HtmlUtils.fromHtml(data.getJp()).toString());
                sendIntent.setType("text/plain");
                sendIntent = Intent.createChooser(sendIntent, "Share via");
                startActivity(sendIntent);
                break;
            case 2:
                TwitterShareDialogFragment f = new TwitterShareDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("DATA", data);
                bundle.putString("URL", url);
                f.setArguments(bundle);

                f.show(getFragmentManager(), "TwitterShareDialogFragment");
                break;
            case 3:
                ClipBoardUtils.putTextIntoClipboard(getActivity(), HtmlUtils.fromHtml(data.getJp()).toString());
                Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
                break;
            case 4:
                ClipBoardUtils.putTextIntoClipboard(getActivity(), HtmlUtils.fromHtml(data.getZh()).toString());
                Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
