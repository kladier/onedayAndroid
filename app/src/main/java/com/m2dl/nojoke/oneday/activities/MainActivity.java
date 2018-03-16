package com.m2dl.nojoke.oneday.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.m2dl.nojoke.oneday.GameView;
import com.m2dl.nojoke.oneday.R;


public class MainActivity extends Activity implements View.OnClickListener {

    private ImageButton buttonPlay;
    private ImageButton buttonScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonScore = findViewById(R.id.buttonScore);

        buttonScore.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==buttonPlay){
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        }
        if(view==buttonScore){
            startActivity(new Intent(MainActivity.this,HighScoreActivity.class));
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You got enough bitcoins for now ?")
                .setCancelable(false)
                .setPositiveButton("Yes, let me go ..", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        GameView.stopMusic();
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                })
                .setNegativeButton("I want to dig more !", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}