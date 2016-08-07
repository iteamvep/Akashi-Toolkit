package rikka.akashitoolkit.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.Toast;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.utils.ClipBoardUtils;
import rikka.akashitoolkit.utils.IntentUtils;

/**
 * Created by Rikka on 2016/6/1.
 */
public class TwitterMoreDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private String text;
    private String translate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        text = bundle.getString("TEXT");
        translate = bundle.getString("TRANSLATE");

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
                .setItems(new CharSequence[]{
                        getString(R.string.share),
                        getString(R.string.copy_original_text),
                        getString(R.string.copy_translated_text)
                }, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                sendIntent = Intent.createChooser(sendIntent, "Share via");
                startActivity(sendIntent);
                break;
            case 1:
                ClipBoardUtils.putTextIntoClipboard(getActivity(), text);
                Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                ClipBoardUtils.putTextIntoClipboard(getActivity(), translate);
                Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
