package de.ojauch.weatheralarmclock.weather.model;

import java.util.List;

/**
 * Represents the current conditions document of the open weather map API
 */
public class Conditions
{
    private Coord coord;

    private List<Weather> weatherList;

    private String base;

    private Main main;

    private int visibility;

    private Wind wind;

    private Clouds clouds;

    private Rain rain;

    private Snow snow;

    private long dt;

    private Sys sys;

    private long id;

    private String name;

    private int cod;

    public Coord getCoord()
    {
        return coord;
    }

    public void setCoord( Coord coord )
    {
        this.coord = coord;
    }

    public List<Weather> getWeatherList()
    {
        return weatherList;
    }

    public void setWeatherList( List<Weather> weatherList )
    {
        this.weatherList = weatherList;
    }

    public String getBase()
    {
        return base;
    }

    public void setBase( String base )
    {
        this.base = base;
    }

    public Main getMain()
    {
        return main;
    }

    public void setMain( Main main )
    {
        this.main = main;
    }

    public int getVisibility()
    {
        return visibility;
    }

    public void setVisibility( int visibility )
    {
        this.visibility = visibility;
    }

    public Wind getWind()
    {
        return wind;
    }

    public void setWind( Wind wind )
    {
        this.wind = wind;
    }

    public Clouds getClouds()
    {
        return clouds;
    }

    public void setClouds( Clouds clouds )
    {
        this.clouds = clouds;
    }

    public Rain getRain()
    {
        return rain;
    }

    public void setRain( Rain rain )
    {
        this.rain = rain;
    }

    public Snow getSnow()
    {
        return snow;
    }

    public void setSnow( Snow snow )
    {
        this.snow = snow;
    }

    public long getDt()
    {
        return dt;
    }

    public void setDt( long dt )
    {
        this.dt = dt;
    }

    public Sys getSys()
    {
        return sys;
    }

    public void setSys( Sys sys )
    {
        this.sys = sys;
    }

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getCod()
    {
        return cod;
    }

    public void setCod( int cod )
    {
        this.cod = cod;
    }
}
