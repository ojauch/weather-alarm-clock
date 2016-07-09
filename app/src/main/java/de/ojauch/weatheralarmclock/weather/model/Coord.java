package de.ojauch.weatheralarmclock.weather.model;

/**
 * Represents the 'coord' object of the open weather map API document
 * @author Oskar Jauch
 */
public class Coord
{
    private double lon;

    private double lat;

    public double getLon()
    {
        return lon;
    }

    public void setLon( double lon )
    {
        this.lon = lon;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat( double lat )
    {
        this.lat = lat;
    }
}
