package rikka.akashitoolkit.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.support.Settings;

public class PushSendActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView[] mTextView = new TextView[3];
    private boolean mHideKey;

    private boolean mDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_send);

        findViewById(R.id.button).setOnClickListener(this);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PushSendActivity.this)
                        .setMessage(getString(R.string.push_send_help))
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });

        mTextView[0] = (EditText) findViewById(R.id.text_gcm_api_key);
        mTextView[1] = (EditText) findViewById(R.id.text_lc_app_id);
        mTextView[2] = (EditText) findViewById(R.id.text_lc_app_key);

        mTextView[0].setText(Settings.instance(this).getString("GCM_API_KEY", ""));
        mTextView[1].setText(Settings.instance(this).getString("LC_APP_ID", ""));
        mTextView[2].setText(Settings.instance(this).getString("LC_APP_KEY", ""));

        mHideKey = Settings.instance(this).getBoolean("HIDE_KEY", false);
        if (mHideKey) {
            for (TextView textView :
                    mTextView) {
                ((View) textView.getParent()).setVisibility(View.GONE);
            }
        }

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHideKey = !mHideKey;

                for (TextView textView :
                        mTextView) {
                    ((View) textView.getParent()).setVisibility(mHideKey ? View.GONE : View.VISIBLE);
                }

                Settings.instance(PushSendActivity.this).putBoolean("HIDE_KEY", mHideKey);
            }
        });

        mDestroyed = false;
    }

    @Override
    protected void onDestroy() {
        mDestroyed = true;
        super.onDestroy();
    }

    @Override
    public void onClick(final View v) {
        v.setEnabled(false);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                StringBuilder sb = new StringBuilder();

                try {
                    sb.append("GCM:\n").append(sendGCM());
                } catch (IOException e) {
                    sb.append("无法发送 GCM 消息.\n请检查网络连接和API_KEY");
                    e.printStackTrace();
                }

                try {
                    sb.append("\n\nLeanCloud:\n").append(sendLeanCloud());
                } catch (IOException e) {
                    sb.append("\n\n无法发送 LeanCloud 消息.\n请检查网络连接和API_KEY");
                    e.printStackTrace();
                }

                return sb.toString();
            }

            @Override
            protected void onPostExecute(String value) {
                if (mDestroyed) {
                    return;
                }

                v.setEnabled(true);

                new AlertDialog.Builder(PushSendActivity.this)
                        .setMessage(value)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        }.execute();
    }

    private String sendGCM() throws IOException {
        String API_KEY = ((EditText) findViewById(R.id.text_gcm_api_key)).getEditableText().toString();

        Settings.instance(this).putString("GCM_API_KEY", API_KEY);

        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jData = new JSONObject();
            // Where to send GCM message.
            jData.put("to", "/topics/global");

            // What to send in GCM message.
            jData.put("data", getJsonData(false));

            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");

            return send(conn, jData);
        } catch (JSONException e) {
            e.printStackTrace();
            return "JSONException";
        }
    }

    private String send(HttpURLConnection conn, JSONObject jData) throws IOException {
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // Send GCM message content.
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(jData.toString().getBytes());

        // Read GCM response.
        InputStream inputStream = conn.getInputStream();

        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }

        return out.toString();
    }

    private String sendLeanCloud() throws IOException {
        String APP_ID = ((EditText) findViewById(R.id.text_lc_app_id)).getEditableText().toString();
        String APP_KEY = ((EditText) findViewById(R.id.text_lc_app_key)).getEditableText().toString();

        Settings.instance(this).putString("LC_APP_ID", APP_ID);
        Settings.instance(this).putString("LC_APP_KEY", APP_KEY);

        try {
            JSONObject jData = new JSONObject();

            jData.put("data", getJsonData(true));

            // Create connection to send GCM Message request.
            URL url = new URL("https://leancloud.cn/1.1/push");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("X-LC-Id", APP_ID);
            conn.setRequestProperty("X-LC-Key", APP_KEY);
            conn.setRequestProperty("Content-Type", "application/json");

            return send(conn, jData);
        } catch (JSONException e) {
            e.printStackTrace();
            return "JSONException";
        }
    }

    private JSONObject getJsonData(boolean isLC) throws JSONException {
        JSONObject jData = new JSONObject();

        String title = ((EditText) findViewById(R.id.text_title)).getEditableText().toString();
        String content = ((EditText) findViewById(R.id.text_content)).getEditableText().toString();
        String activity = ((EditText) findViewById(R.id.text_activity)).getEditableText().toString();
        String extra = ((EditText) findViewById(R.id.text_extra)).getEditableText().toString();

        int id;
        try {
            id = Integer.parseInt(((EditText) findViewById(R.id.text_id)).getEditableText().toString());
        } catch (NumberFormatException ignored) {
            id = -1;
        }

        jData.put("title", title);
        jData.put("message", content);
        jData.put("id", id);
        jData.put("activity", activity);
        jData.put("extra", extra);

        if (isLC) {
            jData.put("action", "rikka.akashitool.PUSH_MESSAGE");
        }

        return jData;
    }
}
