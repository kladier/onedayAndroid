package com.m2dl.nojoke.oneday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.m2dl.nojoke.oneday.activities.EarthQuake;
import com.m2dl.nojoke.oneday.activities.MainActivity;
import com.m2dl.nojoke.oneday.effects.Boom;
import com.m2dl.nojoke.oneday.entities.Rock;
import com.m2dl.nojoke.oneday.entities.Player;
import com.m2dl.nojoke.oneday.entities.Bitcoin;
import com.m2dl.nojoke.oneday.entities.State;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable, SensorEventListener {

    private final short NB_ROCKS = 2;
    
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Bitmap backgroundBitmap, earthQuakeEffect;

    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private final int FORCE_FACTOR = 3; // Increase the values read by the accelerometer
    private float forceOnPlayer;

    int screenX, screenY;
    Context context;
    int score;
    int highScore[] = new int[4];
    SharedPreferences sharedPreferences;

    boolean flag ;
    private boolean isGameOver ;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Bitcoin bitcoin;
    private Rock[] rocks;
    private EarthQuake earthQuake;
    private Boom boom;

    static MediaPlayer gameOnsound;

    public GameView(Context context, int screenX, int screenY, SensorManager sensorManager) {
        super(context);

        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        setUpSensors(sensorManager);
        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();

        this.context = context;
        bitcoin = new Bitcoin(context,screenX,screenY);
        boom = new Boom(context);
        rocks = new Rock[NB_ROCKS];

        for(int i = 0; i < NB_ROCKS ; i++) {
            rocks[i] = new Rock(context, screenX, screenY);
        }

        score = 0;
        this.screenX = screenX;
        isGameOver = false;
        this.screenY = screenY;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);

        gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        gameOnsound.start();
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    public Rock getRock(int i ) {
        return this.rocks[i];
    }

    public Bitcoin getBitcoin() {
        return this.bitcoin;
    }

    private void update() {

        if (earthQuakeHappend()) {
            earthQuake = new EarthQuake(context);
            player.setState(State.SAVE_HIM);
        }

        player.update(forceOnPlayer* FORCE_FACTOR);

        boom.setX(-250);
        boom.setY(-250);

        if(bitcoin.getX()==screenX) flag = true;

        bitcoin.update(player.getSpeed());
        //if collision occurs with player
        if (Rect.intersects(player.getDetectCollision(), bitcoin.getDetectCollision())) {

            // score is increased by 10 points whenever the player hit a bitcoin
            score += 10;
            //displaying boom at that location
            boom.setX(bitcoin.getX());
            boom.setY(bitcoin.getY());

            bitcoin.setX(-200);
        }

        for(int r = 0; r < NB_ROCKS ; r++) {
            rocks[r].update(player.getSpeed());
            if (Rect.intersects(player.getDetectCollision(), rocks[r].getDetectCollision())) {

                boom.setX(rocks[r].getX());
                boom.setY(rocks[r].getY());
                playing = false;
                isGameOver = true;

                gameOnsound.stop();

                //Assigning the scores to the highscore integer array
                for (int i = 0; i < 4; i++) {

                    if (highScore[i] < score) {

                        final int finalI = i;
                        highScore[i] = score;
                        break;
                    }
                }
                SharedPreferences.Editor e = sharedPreferences.edit();

                for (int i = 0; i < 4; i++) {
                    int j = i + 1;
                    e.putInt("score" + j, highScore[i]);
                }
                e.apply();
            }
        }

        if (earthQuake != null) earthQuake.update();

        if (earthQuake != null && earthQuake.isFinish()) {
            if (player.getState() != State.SAFE) {
                playing = false;
                isGameOver = true;
                gameOnsound.stop();

                for (int i = 0; i < 4; i++) {
                    if (highScore[i] < score) {
                        final int finalI = i;
                        highScore[i] = score;
                        break;
                    }
                }
                SharedPreferences.Editor e = sharedPreferences.edit();

                for (int i = 0; i < 4; i++) {
                    e.putInt("score" + i + 1, highScore[i]);
                }
                e.apply();
            }
            earthQuake = null;
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

            if (earthQuake != null && (!earthQuake.isFinish() && player.getState() == State.SAVE_HIM)) {
                canvas.drawBitmap(earthQuake.getBitmap(), screenX/4, screenY/2, paint);
            }

            // draw digger
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            // draw bitcoin
            canvas.drawBitmap(
                bitcoin.getBitmap(),
                bitcoin.getX(),
                bitcoin.getY(),
                paint);

            //drawing score
            paint.setTextSize(35);
            canvas.drawText("Score:"+score,100,50,paint);

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //drawing friends image
            for( int i = 0; i <NB_ROCKS ; i++) {
                Rock rock = rocks[i];
                canvas.drawBitmap(
                        rock.getBitmap(),
                        rock.getX(),
                        rock.getY(),
                        paint
                );
            }

            //draw game Over when the game is over
            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public static void stopMusic(){
        gameOnsound.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if(isGameOver){
            if(motionEvent.getAction()== MotionEvent.ACTION_DOWN) context.startActivity(new Intent(context,MainActivity.class));
        }
        if (earthQuake != null && !earthQuake.isFinish()) {
            if (player.collide(motionEvent.getX(), motionEvent.getY())) player.setState(State.SAFE);
        }
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        forceOnPlayer = event.values[0]; // get X acceleration
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Don't care
    }

    private void setUpSensors(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    private boolean earthQuakeHappend() {
        Random rn = new Random();
        int range = 100 - 0 + 1;
        int randomNum = rn.nextInt(range) + 0;

        if (earthQuake == null && randomNum <= 10) return true;
        else return false;
    }

    public short getNbRocks() {
        return this.NB_ROCKS;
    }
}

