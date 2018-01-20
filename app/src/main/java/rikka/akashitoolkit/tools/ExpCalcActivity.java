package rikka.akashitoolkit.tools;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import rikka.akashitoolkit.R;
import rikka.akashitoolkit.model.Ship;
import rikka.akashitoolkit.ui.BaseActivity;

public class ExpCalcActivity extends BaseActivity {

    private static final String[] MAP_NAME = {
            "1-1 鎮守府正面海域", "1-2 南西諸島沖", "1-3 製油所地帯沿岸", "1-4 南西諸島防衛線", "1-5 [Extra] 鎮守府近海", "1-6 [Extra Operation] 鎮守府近海航路",
            "2-1 カムラン半島", "2-2 バシー島沖", "2-3 東部オリョール海", "2-4 沖ノ島海域", "2-5 [Extra] 沖ノ島沖",
            "3-1 モーレイ海", "3-2 キス島沖", "3-3 アルフォンシーノ方面", "3-4 北方海域全域", "3-5 [Extra] 北方AL海域",
            "4-1 ジャム島攻略作戦", "4-2 カレー洋制圧戦", "4-3 リランカ島空襲", "4-4 カスガダマ沖海戦", "4-5 [Extra] カレー洋リランカ島沖",
            "5-1 南方海域前面", "5-2 珊瑚諸島沖", "5-3 サブ島沖海域", "5-4 サーモン海域", "5-5 [Extra] サーモン海域北方",
            "6-1 中部海域哨戒線", "6-2 MS諸島沖", "6-3 グアノ環礁沖海域"
    };

    private static final String[] EXP_LEVEL = {
            "S", "A", "B", "C", "D"
    };

    private static final int[] MAP_EXP = {
            30, 50, 80, 100, 150, 50,
            120, 150, 200, 300, 250,
            310, 320, 330, 350, 400,
            310, 320, 330, 340, 200,
            360, 380, 400, 420, 450,
            380, 420, 100
    };

    private static final double[] EXP_PERCENT = {
            1.2, 1.0, 1.0, 0.8, 0.7
    };

    private static final double[] EXP_PERCENT_CONDITION = {
            1.0, 1.5, 2.0, 3.0
    };

    private static final int[] EXP = {
            0,       100,     300,     600,     1000,    1500,    2100,    2800,    3600,    4500,    // Lv.10
            5500,    6600,    7800,    9100,    10500,   12000,   13600,   15300,   17100,   19000,
            21000,   23100,   25300,   27600,   30000,   32500,   35100,   37800,   40600,   43500,
            46500,   49600,   52800,   56100,   59500,   63000,   66600,   70300,   74100,   78000,
            82000,   86100,   90300,   94600,   99000,   103500,  108100,  112800,  117600,  122500,
            127500,  132700,  138100,  143700,  149500,  155500,  161700,  168100,  174700,  181500,
            188500,  195800,  203400,  211300,  219500,  228000,  236800,  245900,  255300,  265000,
            275000,  285400,  296200,  307400,  319000,  331000,  343400,  356200,  369400,  383000,
            397000,  411500,  426500,  442000,  458000,  474500,  491500,  509000,  527000,  545500,
            564500,  584500,  606500,  631500,  661500,  701500,  761500,  851500,  1000000, 1000000, // Lv.100
            1010000, 1011000, 1013000, 1016000, 1020000, 1025000, 1031000, 1038000, 1046000, 1055000,
            1065000, 1077000, 1091000, 1107000, 1125000, 1145000, 1168000, 1194000, 1223000, 1255000,
            1290000, 1329000, 1372000, 1419000, 1470000, 1525000, 1584000, 1647000, 1714000, 1785000,
            1860000, 1940000, 2025000, 2115000, 2210000, 2310000, 2415000, 2525000, 2640000, 2760000,
            2887000, 3021000, 3162000, 3310000, 3465000, 3628000, 3799000, 3978000, 4165000, 4360000, // Lv.150
            4564000, 4777000, 4999000, 5230000, 5470000, 5720000, 5780000, 5860000, 5970000, 6120000,
            6320000, 6580000, 6910000, 7320000, 7820000                                               // Lv.165
    };

    private Spinner mSpinnerMap;
    private Spinner mSpinnerResult;

    private TextView mTextViewCurLv;
    private TextView mTextViewTargetLv;
    private TextView mTextViewExpToNext;
    private TextView mTextViewResult;

    private int mMap;
    private int mResult;

    private int mCurLv = 1;
    private int mTargetLv = 99;
    private int mExpToNext = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_calc);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.exp_calc);

        ArrayAdapter<String> adapter;

        mSpinnerMap = (Spinner) findViewById(R.id.spinner_map);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, MAP_NAME);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerMap.setAdapter(adapter);
        mSpinnerMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMap = position;
                calc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerResult = (Spinner) findViewById(R.id.spinner_result);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, EXP_LEVEL);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerResult.setAdapter(adapter);
        mSpinnerResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mResult = position;
                calc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTextViewCurLv = (TextView) findViewById(R.id.textView_curLv);
        mTextViewTargetLv = (TextView) findViewById(R.id.textView_targetLv);
        mTextViewExpToNext = (TextView) findViewById(R.id.textView_expToNext);
        mTextViewResult = (TextView) findViewById(R.id.textView_calcResult);

        mTextViewCurLv.setText(Integer.toString(mCurLv));
        mTextViewTargetLv.setText(Integer.toString(mTargetLv));
        mTextViewExpToNext.setText(Integer.toString(mExpToNext));

        mTextViewCurLv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mCurLv = Integer.parseInt(s.toString());
                    calc();
                } catch (Exception ignored) {

                }
            }
        });

        mTextViewTargetLv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mTargetLv = Integer.parseInt(s.toString());
                    calc();
                } catch (Exception ignored) {

                }
            }
        });

        mTextViewExpToNext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mExpToNext = Integer.parseInt(s.toString());
                    calc();
                } catch (Exception ignored) {

                }
            }
        });

        calc();
    }

    private void calc() {
        if (mCurLv < 1 || mCurLv > Ship.MAX_LEVEL || mTargetLv > Ship.MAX_LEVEL || mTargetLv < 1 || mCurLv > mTargetLv) {
            mTextViewResult.setText(R.string.exp_calc_error);
            return;
        }

        int exp_map = (int) (MAP_EXP[mMap] * EXP_PERCENT[mResult]);

        int cur_exp = EXP[mCurLv - 1] + (EXP[mCurLv] - EXP[mCurLv - 1] - mExpToNext);
        int target_exp = EXP[mTargetLv - 1];
        int exp = (target_exp - cur_exp);
        int num = exp / exp_map;

        mTextViewResult.setText(
                String.format(getString(R.string.exp_calc_result_format),
                        exp,
                        (int) (exp_map * EXP_PERCENT_CONDITION[0]), (int) (num / EXP_PERCENT_CONDITION[0]),
                        (int) (exp_map * EXP_PERCENT_CONDITION[1]), (int) (num / EXP_PERCENT_CONDITION[1]),
                        (int) (exp_map * EXP_PERCENT_CONDITION[2]), (int) (num / EXP_PERCENT_CONDITION[2]),
                        (int) (exp_map * EXP_PERCENT_CONDITION[3]), (int) (num / EXP_PERCENT_CONDITION[3])
                        ));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
