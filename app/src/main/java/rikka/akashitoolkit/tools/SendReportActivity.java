package rikka.akashitoolkit.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.ui.BaseActivity;
import rikka.akashitoolkit.utils.IntentUtils;

/**
 * Created by Rikka on 2016/3/13.
 */
public class SendReportActivity extends BaseActivity {
    public static final String EXTRA_EMAIL_SUBJECT =
            "rikka.akashitoolkit.EXTRA_EMAIL_SUBJECT";

    public static final String EXTRA_EMAIL_BODY =
            "rikka.akashitoolkit.EMAIL_BODY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_send_report);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EMAIL_BODY)) {
            handleSendEmail(intent);
        } else {
            throw new RuntimeException("Crash test!");
        }
    }

    private void handleSendEmail(final Intent intent) {
        new AlertDialog.Builder(this)
                .setTitle("崩溃了..."/*R.string.app_crash_title*/)
                .setMessage("将log以邮件形式发送给开发者以帮助解决问题"/*R.string.app_crash_message*/)
                .setPositiveButton("发送邮件"/*R.string.app_crash_send_email*/, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail(SendReportActivity.this, "Akashi Toolkit crash log", intent.getStringExtra(EXTRA_EMAIL_BODY));
                        finish();
                    }
                })
                .setNegativeButton("取消"/*R.string.app_crash_send_cancel*/, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    public static void sendEmail(Context context, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "rikkanyaaa+akashiToolkitFeedback@gmail.com", null));
        intent.putExtra(Intent.EXTRA_CC, new String[]{"1248076945@qq.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        if (IntentUtils.isValid(context, intent)) {
            intent = Intent.createChooser(intent, context.getString(R.string.send_via)/*getString(R.string.send_via)*/);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, context.getString(R.string.no_mail_app)/*R.string.app_crash_no_email_client*/, Toast.LENGTH_SHORT).show();
        }

    }
}
