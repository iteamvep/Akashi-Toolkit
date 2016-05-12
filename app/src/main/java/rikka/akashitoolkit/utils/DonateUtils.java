package rikka.akashitoolkit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Rikka on 2016/5/11.
 */
public class DonateUtils {
    public static String PACKAGENAME_ALIPAY = "com.eg.android.AlipayGphone";

    public static void startActivity(Context context) {
        if (!Utils.isPackageInstalled(context, PACKAGENAME_ALIPAY)) {
            Toast.makeText(context, "您没有安装支付宝客户端.. QAQ", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Utils.isPackageEnabled(context, PACKAGENAME_ALIPAY)) {
            Toast.makeText(context, "您的支付宝已被禁用 _(:3｣ ∠)_", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(context, "有了提督君的支持，我会变得更好用的哟~", Toast.LENGTH_LONG).show();

        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Faex09499ycmvzsfbkbswl7e")));
    }
}
