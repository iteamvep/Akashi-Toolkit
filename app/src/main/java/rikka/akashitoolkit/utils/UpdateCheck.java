package rikka.akashitoolkit.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.CheckUpdate;
import rikka.akashitoolkit.network.RetrofitAPI;
import rikka.akashitoolkit.support.Settings;

/**
 * Created by Rikka on 2016/3/15.
 */
public class UpdateCheck {
    private static final String URL = "http://www.minamion.com/";

    private static UpdateCheck sInstance;

    private Call<CheckUpdate> mCall;
    private Context mContext;

    public static synchronized UpdateCheck instance() {
        if (sInstance == null) {
            sInstance = new UpdateCheck();
        }
        return sInstance;
    }

    public void recycle() {
        if (mCall != null && mCall.isExecuted()) {
            mCall.cancel();
        }

        mCall = null;
        mContext = null;
    }

    public void check(final Context context, Callback<CheckUpdate> callback) {
        if (mCall != null && mCall.isExecuted()) {
            return;
        }

        mContext = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        int channel = Settings
                .instance(context)
                .getIntFromString(Settings.UPDATE_CHECK_CHANNEL, 0);

        RetrofitAPI.CheckUpdateService service = retrofit.create(RetrofitAPI.CheckUpdateService.class);
        mCall = service.get(4, channel);
        mCall.enqueue(callback);
    }

    // so bad..
    public void check(Context context, final boolean callByUser) {
        if (mCall != null && mCall.isExecuted()) {
            return;
        }

        mContext = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        int channel = Settings
                .instance(context)
                .getIntFromString(Settings.UPDATE_CHECK_CHANNEL, 0);

        RetrofitAPI.CheckUpdateService service = retrofit.create(RetrofitAPI.CheckUpdateService.class);
        mCall = service.get(3, channel);
        mCall.enqueue(new Callback<CheckUpdate>() {
            @Override
            public void onResponse(Call<CheckUpdate> call, final Response<CheckUpdate> response) {
                int versionCode;
                try {
                    versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                final CheckUpdate.UpdateEntity entity = response.body().getUpdate();

                if (entity.getVersionCode() > versionCode) {
                    new AlertDialog.Builder(mContext, R.style.AppTheme_Dialog_Alert)
                            .setTitle(String.format("有新版本啦 (%s - %d)", entity.getVersionName(), entity.getVersionCode()))
                            .setMessage(String.format("更新内容:\n%s", entity.getChange()))
                            .setPositiveButton("去下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entity.getUrl()));
                                    mContext.startActivity(intent);
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    mContext = null;
                                }
                            })
                            .setNegativeButton("才不要", null)
                            .show();
                } else {
                    if (callByUser) {
                        Toast.makeText(mContext, "已经是最新版本啦", Toast.LENGTH_SHORT).show();
                    }
                }

                mCall = null;
            }

            @Override
            public void onFailure(Call<CheckUpdate> call, Throwable t) {
                if (callByUser) {
                    Toast.makeText(mContext, "失败了..", Toast.LENGTH_SHORT).show();
                }
                t.printStackTrace();

                mCall = null;
            }
        });
    }
}
