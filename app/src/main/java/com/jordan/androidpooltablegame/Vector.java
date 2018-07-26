package com.jordan.androidpooltablegame;

public class Vector extends GameObject
{
    public Vector()
    {
    }

    public Vector(double startingX, double startingY, double endingX, double endingY)
    {
        this.setStartingX(startingX);
        this.setStartingY(startingY);
        this.setEndingX(endingX);
        this.setEndingY(endingY);
    }

    /**
     * Normalize a vector: Make it's length equal to one
     */
    public void normalize()
    {
        this.setStartingX((this.getEndingX() - this.getStartingX()) / this.getMagnitude());
        this.setStartingY((this.getEndingY() - this.getStartingY()) / this.getMagnitude());

        this.setMagnitude(1);
    }

    public void times(double distance)
    {
        this.setMagnitude(this.getMagnitude() * distance);
    }
}
