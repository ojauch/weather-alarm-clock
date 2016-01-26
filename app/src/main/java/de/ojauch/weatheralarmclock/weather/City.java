package de.ojauch.weatheralarmclock.weather;

import java.util.Date;

/**
 * Represents a city
 * @author Oskar Jauch
 */
public class City {
    private String cityName;
    private int cityId;
    private double coord_lon;
    private double coord_lat;
    private String country;
    private Date sunrise;
    private Date sunset;

    public City() {

    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String name) {
        this.cityName = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int id) {
        this.cityId = id;
    }

    public double getCoord_lon() {
        return coord_lon;
    }

    public void setCoord_lon(double coord_lon) {
        this.coord_lon = coord_lon;
    }

    public double getCoord_lat() {
        return coord_lat;
    }

    public void setCoord_lat(double coord_lat) {
        this.coord_lat = coord_lat;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getSunrise() {
        return sunrise;
    }

    public void setSunrise(Date sunrise) {
        this.sunrise = sunrise;
    }

    public Date getSunset() {
        return sunset;
    }

    public void setSunset(Date sunset) {
        this.sunset = sunset;
    }
}
