package com.m2dl.nojoke.oneday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.m2dl.nojoke.oneday.activities.MainActivity;
import com.m2dl.nojoke.oneday.effects.Boom;
import com.m2dl.nojoke.oneday.entities.Rock;
import com.m2dl.nojoke.oneday.entities.Player;
import com.m2dl.nojoke.oneday.entities.Bitcoin;

public class GameView extends SurfaceView implements Runnable, SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private final int FORCE_FACTOR = 3; // Increase the values read by the accelerometer
    private float forceOnPlayer;

    //a screenX holder
    int screenX;


    //context to be used in onTouchEvent to cause the activity transition from GameAvtivity to MainActivity.
    Context context;

    //the score holder
    int score;

    //the high Scores Holder
    int highScore[] = new int[4];

    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;


    //to count the number of Misses
    int countMisses;

    //indicator that the enemy has just entered the game screen
    boolean flag ;

    //an indicator if the game is Over
    private boolean isGameOver ;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Bitcoin enemies;

    //created a reference of the class Rock
    private Rock Rock;

    //defining a boom object to display blast
    private Boom boom;

    //the mediaplayer objects to configure the background music
    static MediaPlayer gameOnsound;

    final MediaPlayer killedEnemysound;

    final MediaPlayer gameOversound;
    

    public GameView(Context context, int screenX, int screenY, SensorManager sensorManager) {
        super(context);

        setUpSensors(sensorManager);
        player = new Player(context, screenX, screenY);

        surfaceHolder = getHolder();
        paint = new Paint();

        //initializing context
        this.context = context;

        enemies = new Bitcoin(context,screenX,screenY);

        //initializing boom object
        boom = new Boom(context);

        //initializing the Rock class object
        Rock = new Rock(context, screenX, screenY);

        //setting the score to 0 initially
        score = 0;

        //setting the countMisses to 0 initially
        countMisses = 0;

        this.screenX = screenX;

        isGameOver = false;


        //initializing shared Preferences
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);


        //initializing the array high scores with the previous values
       highScore[0] = sharedPreferences.getInt("score1",0);
       highScore[1] = sharedPreferences.getInt("score2",0);
       highScore[2] = sharedPreferences.getInt("score3",0);
       highScore[3] = sharedPreferences.getInt("score4",0);


        //initializing the media players for the game sounds
       gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);

        //starting the music to be played across the game
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


    private void update() {

        //incrementing score as time passes
        score++;

        player.update(forceOnPlayer* FORCE_FACTOR);

        //setting boom outside the screen
        boom.setX(-250);
        boom.setY(-250);

        //setting the flag true when the enemy just enters the screen
        if(enemies.getX()==screenX){

            flag = true;
        }


        enemies.update(player.getSpeed());
                //if collision occurs with player
                if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())) {

                    //displaying boom at that location
                    boom.setX(enemies.getX());
                    boom.setY(enemies.getY());


                    //playing a sound at the collision between player and the enemy
                    killedEnemysound.start();

                    enemies.setX(-200);
                }

                else{// the condition where player misses the enemy

                    //if the enemy has just entered
                    if(flag){

                        //if player's x coordinate is equal to enemies's y coordinate
                        if(player.getDetectCollision().exactCenterX()>=enemies.getDetectCollision().exactCenterX()){

                            //increment countMisses
                            countMisses++;

                            //setting the flag false so that the else part is executed only when new enemy enters the screen
                            flag = false;

                            //if no of Misses is equal to 3, then game is over.
                            if(countMisses==3){

                                //setting playing false to stop the game.
                                playing = false;
                                isGameOver = true;


                                //stopping the gameon music
                                gameOnsound.stop();
                                //play the game over sound
                                gameOversound.start();

                                //Assigning the scores to the highscore integer array
                                for(int i=0;i<4;i++){
                                    if(highScore[i]<score){

                                        final int finalI = i;
                                        highScore[i] = score;
                                        break;
                                    }
                                }

                                //storing the scores through shared Preferences
                                SharedPreferences.Editor e = sharedPreferences.edit();

                                for(int i=0;i<4;i++){

                                    int j = i+1;
                                    e.putInt("score"+j,highScore[i]);
                                }
                                e.apply();

                            }

                        }
                        }

                }



        //updating the Rock ships coordinates
        Rock.update(player.getSpeed());
                //checking for a collision between player and a Rock
                if(Rect.intersects(player.getDetectCollision(), Rock.getDetectCollision())){

                    //displaying the boom at the collision
                    boom.setX(Rock.getX());
                    boom.setY(Rock.getY());
                    //setting playing false to stop the game
                    playing = false;
                    //setting the isGameOver true as the game is over
                    isGameOver = true;



                    //stopping the gameon music
                    gameOnsound.stop();
                    //play the game over sound
                    gameOversound.start();

                //Assigning the scores to the highscore integer array
                    for(int i=0;i<4;i++){

                        if(highScore[i]<score){

                            final int finalI = i;
                            highScore[i] = score;
                            break;
                        }


                    }
                    //storing the scores through shared Preferences
                    SharedPreferences.Editor e = sharedPreferences.edit();

                    for(int i=0;i<4;i++){

                        int j = i+1;
                        e.putInt("score"+j,highScore[i]);
                    }
                    e.apply();

                }

    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);



            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);


                canvas.drawBitmap(
                        enemies.getBitmap(),
                        enemies.getX(),
                        enemies.getY(),
                        paint

                );

            //drawing the score on the game screen
            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);


            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );






            //drawing friends image
            canvas.drawBitmap(

                    Rock.getBitmap(),
                    Rock.getX(),
                    Rock.getY(),
                    paint
            );


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

    //stop the music on exit
    public static void stopMusic(){

        gameOnsound.stop();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        //Tappin on game Over screen to return to the MainActivity
        if(isGameOver){

            if(motionEvent.getAction()== MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
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
}

