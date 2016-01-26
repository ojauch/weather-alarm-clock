package de.ojauch.weatheralarmclock.weather;

/**
 * Represents the precipitation in a city
 * @author Oskar Jauch
 */
public class Precipitation {
    private int value;
    private PrecipitationMode mode;

    public Precipitation(int value, PrecipitationMode mode) {
        this.value = value;
        this.mode = mode;
    }

    public Precipitation() {

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public PrecipitationMode getMode() {
        return mode;
    }

    public void setMode(PrecipitationMode mode) {
        this.mode = mode;
    }

    public enum PrecipitationMode {
        SNOW, RAIN, NO
    }
}
