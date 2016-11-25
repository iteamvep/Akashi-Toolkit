package rikka.akashitoolkit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import rikka.akashitoolkit.R;

/**
 * Created by Rikka on 2016/5/11.
 */
public class AlipayDonateUtils {

    public static String PACKAGENAME_ALIPAY = "com.eg.android.AlipayGphone";

    public static void startActivity(Context context) {
        if (!PackageUtils.isInstalled(context, PACKAGENAME_ALIPAY)) {
            String text = String.format("%s\n%s",
                    context.getString(R.string.donate_alipay_not_installed),
                    String.format(context.getString(R.string.copied_to_clipboard_format), "rikka@xing.moe"));

            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            ClipBoardUtils.putTextIntoClipboard(context, "rikka@xing.moe");
            return;
        }

        if (!PackageUtils.isEnabled(context, PACKAGENAME_ALIPAY)) {
            String text = String.format("%s\n%s",
                    context.getString(R.string.donate_alipay_disabled),
                    String.format(context.getString(R.string.copied_to_clipboard_format), "rikka@xing.moe"));

            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            ClipBoardUtils.putTextIntoClipboard(context, "rikka@xing.moe");
            return;
        }

        Toast.makeText(context, R.string.donate_alipay_thanks, Toast.LENGTH_LONG).show();

        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Faex09499ycmvzsfbkbswl7e")));
    }
}
