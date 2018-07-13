package com.jordan.androidpooltablegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background
{
    private Bitmap _image;
    private int _x, _y, _vectorX;

    public Background(Bitmap image)
    {
        this._image = image;

        this._vectorX = GamePanel.MOVE_SPEED;
    }

    public void update()
    {
        this._x += this._vectorX;

        if (this._x < -GamePanel.BACKGROUND_IMAGE_WIDTH)
        {
            this._x = 0;
        }
    }

    // https://www.youtube.com/watch?v=GPzTSpZwFoU
    // the screen is much bigger than 856x480 (YT example).  we will need to scale the image
    // bigger
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(this._image, this._x, this._y, null);

        // background will scroll.  to eliminate blank background, need to show a
        // image to cover up blank part
        if (this._x < 0)
        {
            canvas.drawBitmap(this._image, this._x + GamePanel.BACKGROUND_IMAGE_WIDTH, this._y, null);
        }
    }
}
