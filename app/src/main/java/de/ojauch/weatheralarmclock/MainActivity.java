package de.ojauch.weatheralarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static final String LOG_TAG = "weather-alarm-clock";
    private TimePicker tp;
    private CheckBox checkBoxRain;
    private CheckBox checkBoxFreezing;

    private SharedPreferences sharedPref;

    private int timeShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add_alarm);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment tp = new TimePickerFragment();
                tp.show(getFragmentManager(), "tp_alarm_fragment");
            }
        });

        //checkBoxRain = (CheckBox) findViewById(R.id.checkBoxRain);
        //checkBoxFreezing = (CheckBox) findViewById(R.id.checkBoxFrost);

        // get time shift value
        try {
            timeShift = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_TIME_SHIFT, "15"));
        } catch (NumberFormatException e) {
            Log.d(LOG_TAG, "Error parsing time shift preference value");
            timeShift = 15;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will        //noinspection SimplifiableIfStatement

        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void setAlarm(View v) {
        int hour = tp.getCurrentHour();
        int minute = tp.getCurrentMinute();
        boolean rain = checkBoxRain.isChecked();
        boolean freezing = checkBoxFreezing.isChecked();

        Calendar calendar = Calendar.getInstance();

        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        if (calendar.get(Calendar.HOUR_OF_DAY) >= hour - (timeShift / 60) &&
                calendar.get(Calendar.MINUTE) > minute) {
            Log.d(LOG_TAG, "happens on the next day");
            if (currentDay < 365) {
                calendar.set(Calendar.DAY_OF_YEAR, currentDay + 1);
            } else {
                calendar.set(Calendar.DAY_OF_YEAR, 1);
            }
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour - (timeShift / 60));
        calendar.set(Calendar.MINUTE, minute - (timeShift % 60) - 1);

        String city = sharedPref.getString(SettingsActivity.KEY_PREF_CITY, "");
        Log.d(LOG_TAG, city);

        Intent i = new Intent(MainActivity.this, AlarmActivity.class);
        i.putExtra(AlarmActivity.EXTRA_CITY, city);
        i.putExtra(AlarmActivity.EXTRA_RAIN, rain);
        i.putExtra(AlarmActivity.EXTRA_FREEZING, freezing);
        i.putExtra(AlarmActivity.EXTRA_TIME_SHIFT, timeShift);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        String notification = "alarm set: " + hour + ":" + minute;

        Toast toast = Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG);
        toast.show();
    }*/

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d(LOG_TAG, "time set to: " + hourOfDay + ":" + minute);
        //boolean rain = checkBoxRain.isChecked();
        //boolean freezing = checkBoxFreezing.isChecked();

        // TODO: add user control to control conditions
        boolean rain = true;
        boolean frost = true;

        Calendar calendar = Calendar.getInstance();

        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        if (calendar.get(Calendar.HOUR_OF_DAY) >= hourOfDay - (timeShift / 60) &&
                calendar.get(Calendar.MINUTE) > minute) {
            Log.d(LOG_TAG, "happens on the next day");
            if (currentDay < 365) {
                calendar.set(Calendar.DAY_OF_YEAR, currentDay + 1);
            } else {
                calendar.set(Calendar.DAY_OF_YEAR, 1);
            }
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay - (timeShift / 60));
        calendar.set(Calendar.MINUTE, minute - (timeShift % 60) - 1);

        String city = sharedPref.getString(SettingsActivity.KEY_PREF_CITY, "");
        Log.d(LOG_TAG, city);

        Intent i = new Intent(MainActivity.this, AlarmActivity.class);
        i.putExtra(AlarmActivity.EXTRA_CITY, city);
        i.putExtra(AlarmActivity.EXTRA_RAIN, rain);
        i.putExtra(AlarmActivity.EXTRA_FREEZING, frost);
        i.putExtra(AlarmActivity.EXTRA_TIME_SHIFT, timeShift);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        String notification = "alarm set: " + hourOfDay + ":" + minute;

        Toast toast = Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG);
        toast.show();
    }
}
