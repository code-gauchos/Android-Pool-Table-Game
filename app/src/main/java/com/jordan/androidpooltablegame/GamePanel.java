package com.jordan.androidpooltablegame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

// top left corner of landscape/profile screen is origin (0,0)
// (0, max y) is bottom left
// (max X, 0) is top right
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    // this is the width of the background image
    public static final int BACKGROUND_IMAGE_WIDTH = 831;
    // this is the height of the background image
    public static final int BACKGROUND_IMAGE_HEIGHT = 495;
    public static final int MOVE_SPEED = -5;
    private long smokeStartTimer;
    private long _missileStartTime;
    private long _startGameResetTime;
    private boolean _isGameReset;
    private boolean _isPlayerHidden;
    private boolean _isGameStarted;
    private boolean _isNewGameCreated;
    private int _bestDistance;

    private MainThread _mainThread;
    private Background _background;
    private Player _player;
    private Random random = new Random();

    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);

        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {
        // pass in the background image of the game
        this._background = new Background(BitmapFactory.decodeResource(getResources(),
                R.drawable.gas_stop));

        //instantiate the platFort Player
        this._player = new Player(BitmapFactory.decodeResource(getResources(),
                R.drawable.dark_voyager), 71, 129, 1);

        // instantiate a thread object
        _mainThread = new MainThread(getHolder(), this);

        // we can safely start game loop
        this._mainThread.setRunning(true);
        this._mainThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {
        boolean retry = true;
        int counter = 0;

        while (retry && counter < 1000) {
            counter++;

            try {
                this._mainThread.setRunning(false);
                this._mainThread.join();

                // allow garbage collector to pick up object
                this._mainThread = null;

                retry = false;

            }
            catch (InterruptedException interEx) {
                interEx.printStackTrace();
            }
        }
    }

    // reference: https://developer.android.com/reference/android/view/MotionEvent
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent)
    {
        // putting your finger down on the phone
        if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (this._player.getIsPlaying() == false &&
                    this._isNewGameCreated == true &&
                    this._isGameReset == true) {
                this._player.setIsPlaying(true);

                this._player.setIsUp(true);
            }

            if (this._player.getIsPlaying() == true) {
                if (this._isGameStarted == false) {
                    this._isGameStarted = true;
                }

                this._isGameReset = false;

                this._player.setIsUp(true);
            }

            return true;
        }

        // releasing your finger from the phone
        if (touchEvent.getAction() == MotionEvent.ACTION_UP) {

            this._player.setIsUp(false);

            return true;
        }

        return super.onTouchEvent(touchEvent);
    }

    public void update()
    {
        // update background if player is playing
        if (this._player.getIsPlaying() == true) {
            this._background.update();
            this._player.update();
        }
        else {
            // the player is not playing; therefore, a missile struck
            // the player.  show an explosion.
            this._player.resetDY();

            if (this._isGameReset == false) {
                this._isNewGameCreated = false;
                this._startGameResetTime = System.nanoTime();
                this._isGameReset = true;
                this._isPlayerHidden = true;
            }

            long resetElapsed = (System.nanoTime() - this._startGameResetTime) / 1000000;

            if (resetElapsed > 2500 && this._isNewGameCreated == false) {
                newGame();
            }
        }
    }


    // the background image might be too small for phone screen
// according to the https://www.youtube.com/watch?v=GPzTSpZwFoU we will need
// to scale up the image, as seen below
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

//getWidth: gives entire width of phone screen
        final float scaleFactorX = getWidth() / (BACKGROUND_IMAGE_WIDTH * 1.f);
//getHeight: gives entire height of phone screen
        final float scaleFactorY = getHeight() / (BACKGROUND_IMAGE_HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);

            this._background.draw(canvas);

            if (this._isPlayerHidden == false) {
                this._player.draw(canvas);
            }

            drawText(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    private void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("DISTANCE: " + (this._player.getScore() * 3), 10, this.BACKGROUND_IMAGE_HEIGHT - 10, paint);
        canvas.drawText("BEST: " + this._bestDistance, this.BACKGROUND_IMAGE_WIDTH - 215, this.BACKGROUND_IMAGE_HEIGHT - 10, paint);

        if (this._player.getIsPlaying() == false && this._isNewGameCreated == true &&
                this._isGameReset == true) {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", this.BACKGROUND_IMAGE_WIDTH / 2 - 50, this.BACKGROUND_IMAGE_HEIGHT / 2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", this.BACKGROUND_IMAGE_WIDTH / 2 - 50, this.BACKGROUND_IMAGE_HEIGHT / 2 + 20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", this.BACKGROUND_IMAGE_WIDTH / 2 - 50, this.BACKGROUND_IMAGE_HEIGHT / 2 + 40, paint1);
        }
    }

    private void newGame()
    {
        this._isPlayerHidden = false;

        this._player.resetDY();
        this._player.resetScore();
        this._player.setY(this.BACKGROUND_IMAGE_HEIGHT / 2);

        if (this._player.getScore() > this._bestDistance) {
            this._bestDistance = this._player.getScore();

        }

        this._isNewGameCreated = true;
    }

    public boolean didObjectsCollide(GameObject a, GameObject b)
    {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }

//        System.out.println("In didObjectsCollide(), objects did not collide. ");

        return false;
    }
}
