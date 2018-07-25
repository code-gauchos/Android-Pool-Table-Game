package com.jordan.androidpooltablegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

// Player will only move up and down, not left/right
// background image will scroll
public class Player extends GameObject
{
    private int score;
    private boolean _isUp;
    private boolean _isPlaying;
    private PoolBallAnimation animation = new PoolBallAnimation();
    private long startTime;

    // width of individual frame in bitmap image.  so if there are 3 helos
    // in the image, you need to measure w/h of the individual frame
    public Player(Bitmap bitmap, int frameWidth, int frameHeight, int numFrames)
    {
        this.initialize(bitmap, frameWidth, frameHeight, numFrames);
    }

    private void initialize(Bitmap spriteSheet, int frameWidth, int frameHeight, int numFrames)
    {
        this.name = "Dark Voyager";
        this.setX(100);
        this.setY(GamePanel.BACKGROUND_IMAGE_HEIGHT / 2);
        dy = 0;
        score = 0;

        // array to store all the images of the pool ball
        // the image could have three frames of the same image
        Bitmap[] images = new Bitmap[numFrames];

        for (int imageCounter = 0; imageCounter < images.length; imageCounter++)
        {
            // dividing the image by frame.  will create illusion
            //of animation
            images[imageCounter] = Bitmap.createBitmap(spriteSheet, imageCounter * frameWidth, 0, frameWidth, frameHeight);
        }

        animation.setFrames(images);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void setIsUp(boolean isUp)
    {
        _isUp = isUp;
    }

    // implement scoring here
    // commented code is for helicopter flying in
    // platformer game
    public void update()
    {
        long elapsed = (System.nanoTime() - startTime) / 1000000;

        if (elapsed > 100)
        {
            score++;

            startTime = System.nanoTime();
        }

        animation.update();
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(), this.getX(), this.getY(), null);
    }

    public int getScore()
    {
        return score;
    }

    public boolean getIsPlaying()
    {
        return _isPlaying;
    }

    public void setIsPlaying(boolean isPlaying)
    {
        this._isPlaying = isPlaying;
    }

    public void resetScore()
    {
        score = 0;
    }
}
