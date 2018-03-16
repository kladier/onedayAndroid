package com.m2dl.nojoke.oneday.activities;

public class EarthQuake {

    private float timeEarthQuake = 5.0f;
    private boolean finish;
    private long tStart;

    public EarthQuake() {
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
}
