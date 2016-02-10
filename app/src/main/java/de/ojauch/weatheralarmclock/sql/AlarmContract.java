package de.ojauch.weatheralarmclock.sql;

import android.provider.BaseColumns;

/**
 * SQL contract class that is used to store alarm entries in an SQL table
 *
 * @author Oskar Jauch
 */
public final class AlarmContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlarmEntry.TABLE_NAME + " (" +
                    AlarmEntry._ID + " INTEGER PRIMARY KEY," +
                    AlarmEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    AlarmEntry.COLUMN_NAME_ACTIVATED + INTEGER_TYPE + COMMA_SEP +
                    AlarmEntry.COLUMN_NAME_RAIN + INTEGER_TYPE + COMMA_SEP +
                    AlarmEntry.COLUMN_NAME_FROST + INTEGER_TYPE + COMMA_SEP +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;

    public AlarmContract() {
    }

    public static abstract class AlarmEntry implements BaseColumns {
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_ACTIVATED = "activated";
        public static final String COLUMN_NAME_RAIN = "rain";
        public static final String COLUMN_NAME_FROST = "frost";
    }


}
