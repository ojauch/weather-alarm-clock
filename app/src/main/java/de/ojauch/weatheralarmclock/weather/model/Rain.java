package de.ojauch.weatheralarmclock.weather.model;

/**
 * Represents the 'rain' object of the open weather map API document
 *
 * @author Oskar Jauch
 */
public class Rain
{
    // TODO: fix naming convention issues
    private int _3h;

    public int get_3h()
    {
        return _3h;
    }

    public void set_3h( int _3h )
    {
        this._3h = _3h;
    }
}
