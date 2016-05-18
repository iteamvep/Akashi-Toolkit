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
    }

    public void check(final Context context, Callback<CheckUpdate> callback) {
        if (mCall != null && mCall.isExecuted()) {
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        int channel = Settings
                .instance(context)
                .getIntFromString(Settings.UPDATE_CHECK_CHANNEL, 0);

        RetrofitAPI.CheckUpdateService service = retrofit.create(RetrofitAPI.CheckUpdateService.class);
        mCall = service.get(5, channel);
        mCall.enqueue(callback);
    }
}
