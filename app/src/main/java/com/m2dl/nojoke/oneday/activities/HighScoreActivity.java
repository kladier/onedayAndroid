package com.m2dl.nojoke.oneday.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.m2dl.nojoke.oneday.R;

public class HighScoreActivity extends AppCompatActivity {

    TextView textView,textView2,textView3,textView4;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        textView.setText("1st. "+sharedPreferences.getInt("score1",0));
        textView2.setText("2nd. "+sharedPreferences.getInt("score2",0));
        textView3.setText("3rd. "+sharedPreferences.getInt("score3",0));
        textView4.setText("4th. "+sharedPreferences.getInt("score4",0));


    }
}
