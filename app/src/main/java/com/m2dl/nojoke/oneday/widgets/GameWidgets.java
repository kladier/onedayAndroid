package com.m2dl.nojoke.oneday.widgets;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;

public class GameWidgets {

    Button moveLeftBtn, moveRightBtn;

    public GameWidgets(Context ctx, int screenX, int screenY) {
        moveLeftBtn = new Button(ctx);
        moveRightBtn = new Button(ctx);


        moveLeftBtn.setWidth(300);
        moveLeftBtn.setHeight(60);
        moveLeftBtn.setText("LEFT");
        moveLeftBtn.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        //moveLeftBtn.setY(screenY - moveLeftBtn.getHeight() - 10);

        moveRightBtn.setWidth(300);
        moveRightBtn.setHeight(60);
        moveRightBtn.setText("RIGHT");
        moveRightBtn.setX(screenX - moveRightBtn.getWidth() - 10);
        moveRightBtn.setY(screenY - moveRightBtn.getHeight() - 10);
    }

    public Button getMoveLeftBtn() {
        return moveLeftBtn;
    }

    public Button getMoveRightBtn() {
        return moveRightBtn;
    }
}
