package com.m2dl.nojoke.oneday.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.m2dl.nojoke.oneday.R;

public class Player {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 0;
    private int widthScreen;

    private Rect detectCollision;

    public Player(Context context, int screenX, int screenY) {
        widthScreen = screenX;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        x = (screenX / 2) - (bitmap.getWidth()/2);
        y = screenY / 4;
        speed = 1;

        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(float force) {
        x += (force * -1);

        checkOutOfScreen();

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    /**
     * Avoid to go out of the screen
     */
    public void checkOutOfScreen() {
        if (x < 0) {
            x = 0;
        }
        else if (x + bitmap.getWidth() > widthScreen) {
            x -= (x + bitmap.getWidth() - widthScreen);
        }
    }

    //one more getter for getting the rect object
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

    public int getSpeed() {
        return speed;
    }
}
