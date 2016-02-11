package de.ojauch.weatheralarmclock.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.ojauch.weatheralarmclock.Alarm;

/**
 * Helper class to save alarms to an SQLite database
 *
 * @author Oskar Jauch
 */

public class AlarmDbHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String TABLE_NAME = "alarms";
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "WeatherAlarm.db";

    public static final String COLUMN_NAME_TIME = "time";
    public static final String COLUMN_NAME_ENABLED = "enabled";
    public static final String COLUMN_NAME_RAIN = "rain";
    public static final String COLUMN_NAME_FROST = "frost";
    public static final String[] COLUMNS = {
            _ID, COLUMN_NAME_TIME, COLUMN_NAME_ENABLED, COLUMN_NAME_RAIN, COLUMN_NAME_FROST
    };
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_ENABLED + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_RAIN + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_FROST + INTEGER_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    private Context mContext;

    public AlarmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private ContentValues getValues(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TIME, alarm.getTime());
        if (alarm.isEnabled())
            values.put(COLUMN_NAME_ENABLED, 1);
        else
            values.put(COLUMN_NAME_ENABLED, 0);
        if (alarm.getRain())
            values.put(COLUMN_NAME_RAIN, 1);
        else
            values.put(COLUMN_NAME_RAIN, 0);
        if (alarm.getFrost())
            values.put(COLUMN_NAME_FROST, 1);
        else
            values.put(COLUMN_NAME_FROST, 0);
        return values;
    }

    public void writeToDb(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        alarm.setId(db.insert(TABLE_NAME, "null", getValues(alarm)));
    }

    public void updateAlarmEntry(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, getValues(alarm), "_id=" + alarm.getId(), null);
    }

    public List<Alarm> getAlarmsFromDb() {
        List<Alarm> alarmList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getInt(0);
            String strTime = cursor.getString(1);
            int intEnabled = cursor.getInt(2);
            int intRain = cursor.getInt(3);
            int intFrost = cursor.getInt(4);

            int hours = Integer.parseInt(strTime.split(":")[0]);
            int minutes = Integer.parseInt(strTime.split(":")[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Alarm alarm = new Alarm(calendar);
            if (intEnabled == 1)
                alarm.enable();
            else
                alarm.disable();
            if (intRain == 1)
                alarm.setRain(true);
            else
                alarm.setRain(false);
            if (intFrost == 1)
                alarm.setFrost(true);
            else
                alarm.setFrost(false);

            alarm.setId(id);

            alarmList.add(alarm);
            cursor.moveToNext();
        }
        cursor.close();

        return alarmList;
    }

    public void remove(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + _ID + "=" + alarm.getId());
    }
}
