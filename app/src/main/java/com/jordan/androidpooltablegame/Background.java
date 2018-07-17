package com.jordan.androidpooltablegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background
{
    private Bitmap _image;
    private int _x, _y;

    public Background(Bitmap image)
    {
        this._image = image;
    }

    public void update()
    {

    }

    // https://www.youtube.com/watch?v=GPzTSpZwFoU
    // the screen is much bigger than 856x480 (YT example).  we will need to scale the image
    // bigger
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(this._image, this._x, this._y, null);
    }
}
