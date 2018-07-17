package com.jordan.androidpooltablegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

// Billiard ball will in all directions
public class PoolBall extends GameObject
{
    private int score;
    private boolean _isUp;
    private boolean _isPlaying;
    private PoolBallAnimation animation = new PoolBallAnimation();
    private long startTime;

    // width of individual frame in bitmap image.  so if there are 3 helos
    // in the image, you need to measure w/h of the individual frame
    public PoolBall(Bitmap bitmap, String bitmapName, int frameWidth, int frameHeight,
                    int numFrames)
    {
        this.initialize(bitmap, bitmapName, frameWidth, frameHeight, numFrames);
    }

    private void initialize(Bitmap spriteSheet, String bitmapName, int frameWidth, int frameHeight,
                            int numFrames)
    {
        this.name = bitmapName;

        setPoolBallLocation(bitmapName);

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

    private void setPoolBallLocation(String bitmapName)
    {
        switch (bitmapName)
        {
            case "@string/cue_ball":
            {
                this.x = 350;
                this.y = 100;
                break;
            }
            case "@string/one_ball":
            {
                this.x = 100;
                this.y = 100;
            }
        }
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

        setVerticalVelocity();
    }

    private void setVerticalVelocity()
    {
        if (_isUp)
        {
            dy -= 3.1;

        }
        else
        {
            dy += 1.1;
        }

        // caps the speed? height?
        if (dy > 8)
        {
            dy = 8;
        }
        if (dy < -15)
        {
            dy = -15;
        }

        this.y += dy * 2;

        //set floor
        if (this.y > 350)
        {
            this.y = 350;
        }

        // set ceiling
        if (this.y < -120)
        {
            this.y = 0;
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(), this.x, this.y, null);
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

    public void resetDY()
    {
        dy = 0;
    }

    public void resetScore()
    {
        score = 0;
    }
}
