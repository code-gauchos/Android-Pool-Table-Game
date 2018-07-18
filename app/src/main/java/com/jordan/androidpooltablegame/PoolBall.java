package com.jordan.androidpooltablegame;

import android.content.res.Resources;
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
    public PoolBall(Bitmap bitmap, Resources projectResources, int bitmapNameIndex, int frameWidth,
                    int frameHeight,
                    int numFrames)
    {
        this.initialize(bitmap, projectResources, bitmapNameIndex, frameWidth, frameHeight, numFrames);
    }

    private void initialize(Bitmap spriteSheet, Resources projectResources, int bitmapNameIndex,
                            int frameWidth,
                            int frameHeight,
                            int numFrames)
    {
        System.out.println("In PoolBall - initialize(), pool ball name: " +
                projectResources.getString(bitmapNameIndex));

        this.name = projectResources.getString(bitmapNameIndex);

        setPoolBallLocation(bitmapNameIndex);

        dx = 0;
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

    private void setPoolBallLocation(int bitmapNameIndex)
    {
        switch (bitmapNameIndex)
        {
            case R.string.cue_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), cue ball.  ");

                this.x = 460;
                this.y = 100;
                break;
            }
            case R.string.one_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), one ball.  ");

                this.x = 230;
                this.y = 100;
            }
            case R.string.two_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), two ball.  ");

                this.x = 95;
                this.y = 85;
            }
            case R.string.three_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), three ball.  ");

                this.x = 95;
                this.y = 95;
            }
            case R.string.four_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), four ball.  ");

                this.x = 85;
                this.y = 100;
            }
            case R.string.five_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), five ball.  ");

                this.x = 85;
                this.y = 100;
            }
            case R.string.six_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), six ball.  ");

                this.x = 85;
                this.y = 100;
            }
            case R.string.seven_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), seven ball.  ");

                this.x = 100;
                this.y = 100;
            }
            case R.string.eight_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), eight ball.  ");

                this.x = 100;
                this.y = 100;
            }
            case R.string.nine_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), nine ball.  ");

                this.x = 100;
                this.y = 100;
            }
            case R.string.ten_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), ten ball.  ");

                this.x = 100;
                this.y = 100;
            }
            case R.string.eleven_ball:
            {
                System.out.println("In PoolBall - setPoolBallLocation(), eleven ball.  ");

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
//            score++;

            startTime = System.nanoTime();
        }

        animation.update();

        setVerticalVelocity();

        setHorizontalVelocity();
    }

    private void setHorizontalVelocity()
    {
        if (_isUp)
        {
            dx -= 3.1;

        }
        else
        {
            dx += 1.1;
        }

        // caps the speed? height?
        if (dx > 8)
        {
            dx = 8;
        }
        if (dx < -15)
        {
            dx = -15;
        }

        this.x += dx * 2;

        //set floor
        if (this.x > 350)
        {
            this.x = 350;
        }

        // set ceiling
        if (this.x < -120)
        {
            this.x = 0;
        }
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
