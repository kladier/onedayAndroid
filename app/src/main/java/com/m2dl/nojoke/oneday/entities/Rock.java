package com.m2dl.nojoke.oneday.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.m2dl.nojoke.oneday.R;

import java.util.Random;

public class Rock {
    private Bitmap bitmap;
    private int x;
    private int y;
    public int speed = 1;

    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    private Rect detectCollision;

    public Rock(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone);
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        y = screenY;
        x = generator.nextInt(maxX) - bitmap.getWidth();
        if (x < 0 ) x = 0;

        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int playerSpeed) {
        y -= playerSpeed;
        y -= speed;
        if (y < minY - bitmap.getHeight()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            y = maxY;
            x = generator.nextInt(maxX) - bitmap.getWidth();
            if (x < 0 ) x = 0;
        }

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
