package com.jordan.androidpooltablegame;

public class Vector
{
    private double _magnitude;

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
    public double getMagnitude()
    {
        return this._magnitude;
    }

    public Vector copy()
    {
        return new Vector();
    }
}
