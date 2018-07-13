package com.jordan.androidpooltablegame;

import android.graphics.Bitmap;

public class PlayerAnimation
{
    private Bitmap[] _frames;
    private int _currentFrame;
    private long _startTime;
    private long _delay;
    private boolean _isPlayedOnce;

    public void setFrames(Bitmap[] frames)
    {
        this._frames = frames;

        this._currentFrame = 0;

        this._startTime = System.nanoTime();
    }

    public void setDelay(long delay)
    {
        this._delay = delay;
    }

    public void setFrame(int currentFrame)
    {
        this._currentFrame = currentFrame;
    }

    public void update()
    {
        long elapsed = (System.nanoTime() - this._startTime) / 1000000;

        if (elapsed > this._delay)
        {
            this._currentFrame++;

            this._startTime = System.nanoTime();
        }

        if (this._currentFrame == this._frames.length)
        {
            this._currentFrame = 0;

            this._isPlayedOnce = true;
        }
    }

    public Bitmap getImage()
    {
        return this._frames[this._currentFrame];
    }

    public int getFrame()
    {
        return this._currentFrame;
    }

    public boolean isPlayedOnce()
    {
        return this._isPlayedOnce;
    }
}
