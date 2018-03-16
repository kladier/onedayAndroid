package com.m2dl.nojoke.oneday.widgets;

import android.content.Context;
import android.widget.Button;

public class GameWidgets {

    Button moveLeftBtn, moveRightBtn;

    public GameWidgets(Context ctx, int screenX, int screenY) {
        moveLeftBtn = new Button(ctx);
        moveRightBtn = new Button(ctx);


        moveLeftBtn.setWidth(300);
        moveLeftBtn.setHeight(60);
        moveLeftBtn.setText("LEFT");
        moveLeftBtn.setX(500);
        moveLeftBtn.setY(400);
        //moveLeftBtn.setY(screenY - moveLeftBtn.getHeight() - 10);

        moveRightBtn.setWidth(300);
        moveLeftBtn.setHeight(60);
        moveRightBtn.setText("RIGHT");
        moveLeftBtn.setX(screenX - moveRightBtn.getWidth() - 10);
        moveLeftBtn.setY(screenY - moveLeftBtn.getHeight() - 10);
    }

    public Button getMoveLeftBtn() {
        return moveLeftBtn;
    }

    public Button getMoveRightBtn() {
        return moveRightBtn;
    }
}
