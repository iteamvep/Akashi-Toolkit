package rikka.akashitoolkit.support;

import android.media.MediaPlayer;
import android.util.Log;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.cache.DiskCache;

import java.io.File;
import java.io.IOException;

import rikka.akashitoolkit.cache.DiskCacheProvider;
import rikka.akashitoolkit.cache.SimpleKey;
import rikka.akashitoolkit.cache.StreamWriter;
import rikka.akashitoolkit.network.NetworkUtils;

/**
 * Created by Rikka on 2016/5/20.
 */
public class MusicPlayer {
    private static MediaPlayer sPlayer;

    private static String lastPlayed;

    public static void play(String path) throws IOException {
        if (path == null) {
            return;
        }

        // so bad
        /*if (path.startsWith("http://kc.6candy.com/")) {
            path = path.replace("http://kc.6candy.com/", "https://upload.kcwiki.moe/");
        }*/

        if (path.startsWith("https://kc.6candy.com/")) {
            path = path.replace("https://kc.6candy.com/", "https://upload.kcwiki.moe/");
        }

        DiskCache diskCache = DiskCacheProvider.get();

        final Key key = new SimpleKey(path);
        File file = diskCache.get(key);

        if (file != null) {
            playInternal(file.getPath());
            return;
        }

        Log.d("MusicPlayer", path + " not find, start download");

        NetworkUtils.get(path, new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d("MusicPlayer", "download finished");

                DiskCache diskCache = DiskCacheProvider.get();
                diskCache.put(key, new StreamWriter(response.body().byteStream()));

                playInternal(diskCache.get(key).getPath());
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("MusicPlayer", "download failed");
            }
        });
    }

    private static void playInternal(String path) throws IOException {
        Log.d("MusicPlayer", "play " + path);

        stop();

        if (lastPlayed != null && path.equals(lastPlayed)) {
            lastPlayed = null;
            return;
        }

        lastPlayed = path;
        sPlayer = new MediaPlayer();
        sPlayer.setDataSource(path);
        sPlayer.prepare();
        sPlayer.start();
        sPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                lastPlayed = null;
            }
        });
    }

    public static void stop() {
        if (sPlayer != null) {
            if (sPlayer.isPlaying()) {
                sPlayer.stop();
            }

            sPlayer.release();
            sPlayer = null;
        }
    }
}
