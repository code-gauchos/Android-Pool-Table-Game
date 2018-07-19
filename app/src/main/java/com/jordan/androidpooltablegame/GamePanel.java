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
    public static final int BACKGROUND_IMAGE_WIDTH = 500;
    // this is the height of the background image
    public static final int BACKGROUND_IMAGE_HEIGHT = 320;
    private long _startGameResetTime;
    private boolean _isGameReset;
    private boolean _isPlayerHidden;
    private boolean _isGameStarted;
    private boolean _isNewGameCreated;
    private int _bestDistance;

    private MainThread _mainThread;
    private Background _background;
    private Player _player;
    private PoolBall _oneBall;
    private PoolBall _twoBall;
    private PoolBall _threeBall;
    private PoolBall _fourBall;
    private PoolBall _fiveBall;
    private PoolBall _sixBall;
    private PoolBall _sevenBall;
    private PoolBall _eightBall;
    private PoolBall _nineBall;
    private PoolBall _tenBall;
    private PoolBall _elevenBall;
    private PoolBall _twelveBall;
    private PoolBall _thirteenBall;
    private PoolBall _fourteenBall;
    private PoolBall _fifteenBall;
    private PoolBall _cueBall;

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
                R.drawable.pool_table));

        //instantiate the pool balls
        instantiatePoolsBalls();

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

        while (retry && counter < 1000)
        {
            counter++;

            try
            {
                this._mainThread.setRunning(false);
                this._mainThread.join();

                // allow garbage collector to pick up object
                this._mainThread = null;

                retry = false;

            }
            catch (InterruptedException interEx)
            {
                interEx.printStackTrace();
            }
        }
    }

    // reference: https://developer.android.com/reference/android/view/MotionEvent
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent)
    {
        // putting your finger down on the phone
        if (touchEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (this._player.getIsPlaying() == false &&
                    this._isNewGameCreated == true &&
                    this._isGameReset == true)
            {
                System.out.println("In GamePanel - onTouchEvent(), app determined: ACTION_DOWN. " +
                        "we're starting a new game.  ");

                this._player.setIsPlaying(true);

                this._player.setIsUp(true);

                this._cueBall.setIsCueStruck(true);
            }
            else
            {
                System.out.println("In GamePanel - onTouchEvent(), app determined we're in a" +
                        " running game. player is playing is " + this._player.getIsPlaying() +
                        ".  A new game is created is " + this._isNewGameCreated +
                        ".  This game is reset is " + this._isGameReset + ".");
            }

            if (this._player.getIsPlaying() == true)
            {
                if (this._isGameStarted == false)
                {
                    this._isGameStarted = true;
                }

                this._isGameReset = false;

                this._player.setIsUp(true);

                this._cueBall.setIsCueStruck(true);
            }

            return true;
        }

        // releasing your finger from the phone
        if (touchEvent.getAction() == MotionEvent.ACTION_UP)
        {
            System.out.println("In GamePanel - onTouchEvent(), app determined: ACTION_UP. " +
                    "player has lifted finger off screen.  ");

            this._player.setIsUp(false);

            this._cueBall.setIsCueStruck(false);

            return true;
        }

        return super.onTouchEvent(touchEvent);
    }

    /**
     * This gets called from the Main Loop
     */
    public void update()
    {
        // update background if player is playing
        if (this._player.getIsPlaying() == true)
        {
            this._background.update();
            this._player.update();
            this.updatePoolBalls();
        }
        else
        {
            System.out.println("In GamePanel - update(), player is not playing the game");

            // the player is not playing; therefore, reset game values
            this.resetPoolBalls();

            if (this._isGameReset == false)
            {
                this._isNewGameCreated = false;
                this._startGameResetTime = System.nanoTime();
                this._isGameReset = true;
                this._isPlayerHidden = true;
            }

            long resetElapsed = (System.nanoTime() - this._startGameResetTime) / 1000000;


            System.out.println("In GamePanel - update(), is game reset is " + this._isGameReset +
                    ". Is new game created is " + this._isNewGameCreated);

            if (resetElapsed > 2500 && this._isNewGameCreated == false)
            {
                this.newGame();
            }
        }
    }

    /**
     * This gets called from the Main Loop
     * <p>
     * the background image might be too small for phone screen. according to the
     * https://www.youtube.com/watch?v=GPzTSpZwFoU we will need to scale up the image, as seen
     * below
     */
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

//getWidth: gives entire width of phone screen
        final float scaleFactorX = getWidth() / (BACKGROUND_IMAGE_WIDTH * 1.f);
//getHeight: gives entire height of phone screen
        final float scaleFactorY = getHeight() / (BACKGROUND_IMAGE_HEIGHT * 1.f);

        if (canvas != null)
        {
//            final int savedState = canvas.save();
//
//            canvas.scale(scaleFactorX, scaleFactorY);

            this._background.draw(canvas);

            if (this._isPlayerHidden == false)
            {
                this._player.draw(canvas);
                this._cueBall.draw(canvas);
                this._oneBall.draw(canvas);
                this._twoBall.draw(canvas);
                this._threeBall.draw(canvas);
                this._fourBall.draw(canvas);
                this._fiveBall.draw(canvas);
                this._sixBall.draw(canvas);
                this._sevenBall.draw(canvas);
                this._eightBall.draw(canvas);
                this._nineBall.draw(canvas);
                this._tenBall.draw(canvas);
                this._elevenBall.draw(canvas);
                this._twelveBall.draw(canvas);
                this._thirteenBall.draw(canvas);
                this._fourteenBall.draw(canvas);
                this._fifteenBall.draw(canvas);
            }

            drawText(canvas);

//            canvas.restoreToCount(savedState);
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
                this._isGameReset == true)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", this.BACKGROUND_IMAGE_WIDTH / 2 - 50, this.BACKGROUND_IMAGE_HEIGHT / 2, paint1);

//            paint1.setTextSize(20);
//            canvas.drawText("PRESS AND HOLD TO GO UP", this.BACKGROUND_IMAGE_WIDTH / 2 - 50, this.BACKGROUND_IMAGE_HEIGHT / 2 + 20, paint1);
//            canvas.drawText("RELEASE TO GO DOWN", this.BACKGROUND_IMAGE_WIDTH / 2 - 50, this.BACKGROUND_IMAGE_HEIGHT / 2 + 40, paint1);
        }
    }

    private void resetPoolBalls()
    {
        System.out.println("In GamePanel - resetPoolBalls(), app will reset horizontal and vertical " +
                "velocities for all pool balls. ");

        this._cueBall.resetVelocities();
        this._oneBall.resetVelocities();
        this._twoBall.resetVelocities();
        this._threeBall.resetVelocities();
        this._fourBall.resetVelocities();
        this._fiveBall.resetVelocities();
        this._sixBall.resetVelocities();
        this._sevenBall.resetVelocities();
        this._eightBall.resetVelocities();
        this._nineBall.resetVelocities();

        //todo: restore these objects.  must be null reference here
//        this._tenBall.resetVelocities();
//        this._elevenBall.resetVelocities();
//        this._twelveBall.resetVelocities();
//        this._thirteenBall.resetVelocities();
//        this._fourteenBall.resetVelocities();
//        this._fifteenBall.resetVelocities();
    }


    private void updatePoolBalls()
    {
        // check whether ball is in a pocket
        if (this._oneBall.getIsInPocket() == false)
        {
            this._oneBall.update();
        }

        if (this._twoBall.getIsInPocket() == false)
        {
            this._twoBall.update();
        }

        if (this._threeBall.getIsInPocket() == false)
        {
            this._threeBall.update();
        }

        if (this._fourBall.getIsInPocket() == false)
        {
            this._fourBall.update();
        }

        if (this._fiveBall.getIsInPocket() == false)
        {
            this._fiveBall.update();
        }

        if (this._sixBall.getIsInPocket() == false)
        {
            this._sevenBall.update();
        }

        if (this._eightBall.getIsInPocket() == false)
        {
            this._eightBall.update();
        }

        if (this._nineBall.getIsInPocket() == false)
        {
            this._nineBall.update();
        }

        //todo: fix null reference here
//        if (this._tenBall.getIsInPocket() == false)
//        {
//            this._tenBall.update();
//        }
//        if (this._elevenBall.getIsInPocket() == false)
//        {
//            this._elevenBall.update();
//        }
//        if (this._twelveBall.getIsInPocket() == false)
//        {
//            this._twelveBall.update();
//        }
//        if (this._thirteenBall.getIsInPocket() == false)
//        {
//            this._thirteenBall.update();
//        }
//        if (this._fourteenBall.getIsInPocket() == false)
//        {
//            this._fourteenBall.update();
//        }
//        if (this._fifteenBall.getIsInPocket() == false)
//        {
//            this._fifteenBall.update();
//        }
        if (this._cueBall.getIsInPocket() == false)
        {
            this._cueBall.update();
        }
    }


    private void newGame()
    {
        System.out.println("In GamePanel - newGame(), app is resetting to start a new game.  ");

        this._isPlayerHidden = false;

        this.resetPoolBalls();

        this._player.resetScore();

        if (this._player.getScore() > this._bestDistance)
        {
            this._bestDistance = this._player.getScore();
        }

        this._isNewGameCreated = true;
    }

    public boolean didObjectsCollide(GameObject a, GameObject b)
    {
        if (Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }

//        System.out.println("In didObjectsCollide(), objects did not collide. ");

        return false;
    }


    private void instantiatePoolsBalls()
    {
        this._cueBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.cue_ball), super.getContext().getResources(), R.string.cue_ball,
                32, 32, 1);

        this._oneBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.one_ball), super.getContext().getResources(), R.string.one_ball,
                32, 32, 1);

        this._twoBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.two_ball), super.getContext().getResources(), R.string.two_ball,
                32, 32, 1);
        this._threeBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.three_ball), super.getContext().getResources(), R.string.three_ball,
                32, 31, 1);

        this._fourBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.four_ball), super.getContext().getResources(), R.string.four_ball,
                32, 31, 1);
        this._fiveBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.five_ball), super.getContext().getResources(), R.string.five_ball,
                32, 32, 1);

        this._sixBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.six_ball), super.getContext().getResources(), R.string.six_ball,
                104, 103, 1);
        this._sevenBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.seven_ball), super.getContext().getResources(), R.string.seven_ball,
                102, 100, 1);

        this._eightBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.eight_ball), super.getContext().getResources(), R.string.eight_ball,
                102, 75, 1);
        this._nineBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.nine_ball), super.getContext().getResources(), R.string.nine_ball,
                110, 114, 1);

//        this._tenBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
//                R.drawable.ten_ball), 101, 101, 1);

        this._elevenBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.eleven_ball), super.getContext().getResources(), R.string.eleven_ball,
                99, 99, 1);

        this._twelveBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.twelve_ball), super.getContext().getResources(), R.string.twelve_ball,
                122, 126, 1);
        this._thirteenBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.thirteen_ball), super.getContext().getResources(), R.string.thirteen_ball,
                102, 102, 1);

        this._fourteenBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.fourteen_ball), super.getContext().getResources(), R.string.fourteen_ball,
                102, 102, 1);

        this._fifteenBall = new PoolBall(BitmapFactory.decodeResource(getResources(),
                R.drawable.fifteen_ball), super.getContext().getResources(), R.string.fifteen_ball,
                101, 105, 1);

        // todo: need to figure out how to start this game
        this._player = new Player(BitmapFactory.decodeResource(getResources(),
                R.drawable.one_ball), 71, 129, 1);
    }

}
