package com.jordan.androidpooltablegame;

import android.graphics.Rect;

public abstract class GameObject
{
    /**
     * point on _x plane
     */
    private int _x;

    /**
     * point on _y plane
     */
    private int _y;

    protected int dy;
    protected int dx;

    /**
     * image width
     */
    protected int width;

    /**
     * image height
     */
    protected int height;

    protected String name;

    private double _radius;

    public int getX()
    {
        return _x;
    }

    public void setX(int x)
    {
        this._x = x;
    }

    public int getY()
    {
        return _y;
    }

    public void setY(int y)
    {
        this._y = y;
    }

    // _y vector
    public int getDy()
    {
        return dy;
    }

    public void setDy(int dy)
    {
        this.dy = dy;
    }

    // _x vector
    public int getDx()
    {
        return dx;
    }

    public void setDx(int dx)
    {
        this.dx = dx;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public double getRadius()
    {
        return _radius;
    }

    public void setRadius(double radius)
    {
        this._radius = radius;
    }

    /**
     * The distance is the hypotenuse of a triangle
     *
     * @param otherBall
     * @return
     */
    public double distanceBetweenCentersOfBalls(GameObject otherBall)
    {
        // calculate delta X
        double deltaXSquared = this.getX() - otherBall.getX();

        // square delta X
        deltaXSquared *= deltaXSquared;

        // calculate delta Y
        double deltaYSquared = this.getY() - otherBall.getY();

        // square delta Y
        deltaYSquared *= deltaYSquared;

        return Math.sqrt(deltaXSquared + deltaYSquared);
    }
}
