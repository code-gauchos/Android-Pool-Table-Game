package com.jordan.androidpooltablegame;

public abstract class GameObject
{
    /**
     * starting point of vector on _startX plane
     */
    private double _startX;

    private double _endX;
    /**
     * starting point of vector on _startY plane
     */
    private double _startY;

    private double _endY;

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

    private double _magnitude;

    public double getStartingX()
    {
        return _startX;
    }

    public void setStartingX(double x)
    {
        this._startX = x;
    }

    public double getStartingY()
    {
        return _startY;
    }

    public void setStartingY(double y)
    {
        this._startY = y;
    }

    // _startY vector
    public int getDy()
    {
        return dy;
    }

    public void setDy(int dy)
    {
        this.dy = dy;
    }

    // _startX vector
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
     * Length of a vector
     *
     * @return
     */
    public double getMagnitude()
    {
        return _magnitude;
    }

    /**
     * Magnitude is defined as the length of a vector
     *
     * The magnitude of a vector can be found by taking the square root of each of the vector
     * components squared. Using Pythagoras' theorem, calculate:
     *
     * |a| = sqrt( x2 + y2 )
     *
     * magnitude = |a|
     *
     * @return
     */
    public void setMagnitude()
    {
        // calculate delta X
        double deltaXSquared = this.getEndingX() - this.getStartingX();

        // square delta X
        deltaXSquared *= deltaXSquared;

        // calculate delta X
        double deltaYSquared = this.getEndingY() - this.getStartingY();

        // square delta Y
        deltaYSquared *= deltaYSquared;

        //this is the hypotenuse
        this._magnitude = Math.sqrt(deltaXSquared + deltaYSquared);
    }

    /**
     * ending point of vector on _startX plane
     */
    public double getEndingX()
    {
        return _endX;
    }

    public void setEndingX(double _endX)
    {
        this._endX = _endX;
    }

    /**
     * ending point of vector on _startX plane
     */
    public double getEndingY()
    {
        return _endY;
    }

    public void setEndingY(double _endY)
    {
        this._endY = _endY;
    }

    public void setMagnitude(double magnitude)
    {
        this._magnitude = magnitude;
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
        double deltaXSquared = this.getStartingX() - otherBall.getStartingX();

        // square delta X
        deltaXSquared *= deltaXSquared;

        // calculate delta Y
        double deltaYSquared = this.getStartingY() - otherBall.getStartingY();

        // square delta Y
        deltaYSquared *= deltaYSquared;

        return Math.sqrt(deltaXSquared + deltaYSquared);
    }
}
