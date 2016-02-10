package de.ojauch.weatheralarmclock;

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
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

import de.ojauch.weatheralarmclock.sql.AlarmDbHelper;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static final String LOG_TAG = "weather-alarm-clock";

    private SharedPreferences sharedPref;
    private AlarmListAdapter mAdapter;
    private ListView mListView;
    private int timeShift;
    private AlarmDbHelper mDbHelper;
    private List<Alarm> mAlarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbHelper = new AlarmDbHelper(getApplicationContext());
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        update();

        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add_alarm);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment tp = new TimePickerFragment();
                tp.show(getFragmentManager(), "tp_alarm_fragment");
            }
        });

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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d(LOG_TAG, "time set to: " + hourOfDay + ":" + minute);

        // TODO: add user control to control conditions
        int hourShift = timeShift / 60;
        int minuteShift = (timeShift % 60) + 1;

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay - hourShift);
        calSet.set(Calendar.MINUTE, minute - minuteShift);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        addAlarm(calSet);
    }

    private void addAlarm(Calendar calendar) {
        Alarm alarm = new Alarm(calendar);
        alarm.setFrost(true);
        alarm.setRain(true);

        mDbHelper.writeToDb(alarm);
        update();
    }

    private void update() {
        mAlarmList = mDbHelper.getAlarmsFromDb();
        mAdapter = new AlarmListAdapter(getApplicationContext(), R.layout.alarm_row, mAlarmList);
        mListView = (ListView) findViewById(R.id.alarmListView);
        mListView.setAdapter(mAdapter);
    }
}
