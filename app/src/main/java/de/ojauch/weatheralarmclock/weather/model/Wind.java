package de.ojauch.weatheralarmclock.weather.model;

/**
 * Represents the 'wind' object of the open weather map API document
 *
 * @author Oskar Jauch
 */
public class Wind
{
    private float speed;

    private int deg;

    public float getSpeed()
    {
        return speed;
    }

    public void setSpeed( float speed )
    {
        this.speed = speed;
    }

    public int getDeg()
    {
        return deg;
    }

    public void setDeg( int deg )
    {
        this.deg = deg;
    }
}
