package com.m2dl.nojoke.oneday.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m2dl.nojoke.oneday.GameView;
import com.m2dl.nojoke.oneday.R;
import com.m2dl.nojoke.oneday.widgets.GameWidgets;

public class GameActivity extends AppCompatActivity implements SensorEventListener{

    private GameView gameView;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private FrameLayout game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_GAME);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        GameWidgets gameWidgets = new GameWidgets(this, size.x, size.y);

        LinearLayout gameWidgetsLayout = new LinearLayout(this);
        gameWidgetsLayout.setGravity(Gravity.TOP|Gravity.CENTER);
        gameWidgetsLayout.setMinimumHeight(size.y);

        //Initializing game view object
        //this time we are also passing the screen size to the GameView constructor

        SensorManager senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gameView = new GameView(this, size.x, size.y, senSensorManager);

        game = new FrameLayout(this);

        game.addView(gameView);
        game.addView(gameWidgetsLayout);
        setContentView(game);
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (gameView != null) {
            int maxLuminosity = 2000;
            float illuminance = event.values[0];

            int percentageIlluminance = (int)illuminance*100/maxLuminosity;

            int opacity = 128;

            if (percentageIlluminance >= 0 && percentageIlluminance < 25) {
                opacity = 255;
            }
            else if (percentageIlluminance >= 25 && percentageIlluminance < 50) {
                opacity = 200;
            }
            else if (percentageIlluminance >= 50 && percentageIlluminance < 75) {
                opacity = 100;
            }
            else {
                opacity = 50;
            }
            gameView.setOpacity(opacity);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // we dont care
    }
}
