package de.ojauch.weatheralarmclock;

import java.util.Calendar;


/**
 * @author Oskar Jauch
 */
public class Alarm {
    private Calendar mCalendar;
    private boolean mEnabled;
    private boolean mRain;
    private boolean mFrost;
    private long mID;

    public Alarm(Calendar calendar) {
        this.mCalendar = calendar;
        this.mEnabled = true;
    }

    public boolean getRain() {
        return mRain;
    }

    public void setRain(boolean rain) {
        this.mRain = rain;
    }

    public boolean getFrost() {
        return mFrost;
    }

    public void setFrost(boolean frost) {
        this.mFrost = frost;
    }

    public void enable() {
        this.mEnabled = true;
    }

    public void disable() {
        this.mEnabled = false;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public long getId() {
        return mID;
    }

    public void setId(long id) {
        this.mID = id;
    }

    public void changeTime(Calendar calendar) {
        this.mCalendar = calendar;
    }

    public int getHours() {
        return mCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes() {
        return mCalendar.get(Calendar.MINUTE);
    }

    public String getTime() {
        return getHours() + ":" + getMinutes();
    }
}
