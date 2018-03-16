package com.m2dl.nojoke.oneday.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.m2dl.nojoke.oneday.R;

public class EarthQuake {

    private final Bitmap earthQuakeEffect;
    private float timeEarthQuake = 3.0f;
    private boolean finish;
    private long tStart;

    public EarthQuake(Context context) {
        earthQuakeEffect = BitmapFactory.decodeResource(context.getResources(), R.drawable.earthquake_effect);

        finish = false;
        tStart = System.currentTimeMillis();
    }

    public void update() {
        if (!finish) {
            long currentTime = System.currentTimeMillis();
            long tDelta = currentTime - tStart;
            double elapsedSeconds = tDelta / 1000.0;

            if (elapsedSeconds >= timeEarthQuake) {
                finish = true;
            }
        }
    }
    
    public boolean isFinish() {
        return finish;
    }

    public void setTimeEarthQuake(float timeEarthQuake) {
        this.timeEarthQuake = timeEarthQuake;
    }

    public Bitmap getBitmap() {
        return earthQuakeEffect;
    }

}
