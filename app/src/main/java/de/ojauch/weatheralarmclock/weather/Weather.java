package de.ojauch.weatheralarmclock.weather;

/**
 * Class to represent weather data from OpenWeatherMap
 * @author Oskar Jauch
 */
class Weather {
    private City city;
    private float temperature;
    private int humidity;
    private int pressure;
    private Wind wind;
    private int clouds;
    private int visibility;
    private Precipitation precipitation;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int value, Precipitation.PrecipitationMode mode) {
        this.precipitation = new Precipitation(value, mode);
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }
}
