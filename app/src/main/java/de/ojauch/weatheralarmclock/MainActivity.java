package de.ojauch.weatheralarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TimePicker tp;
    private EditText editCity;
    private CheckBox checkBoxRain;
    private CheckBox checkBoxFreezing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tp = (TimePicker) findViewById(R.id.timePicker);
        editCity = (EditText) findViewById(R.id.editCity);
        checkBoxRain = (CheckBox) findViewById(R.id.checkBoxRain);
        checkBoxFreezing = (CheckBox) findViewById(R.id.checkBoxFreezing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAlarm(View v) {
        int hour = tp.getCurrentHour();
        int minute = tp.getCurrentMinute();
        boolean rain = checkBoxRain.isChecked();
        boolean freezing = checkBoxFreezing.isChecked();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute - 1);

        String city = editCity.getText().toString();

        Intent i = new Intent(MainActivity.this, AlarmActivity.class);
        i.putExtra(AlarmActivity.EXTRA_CITY, city);
        i.putExtra(AlarmActivity.EXTRA_RAIN, rain);
        i.putExtra(AlarmActivity.EXTRA_FREEZING, freezing);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        String notification = "alarm set: " + hour + ":" + minute;

        Toast toast = Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_LONG);
        toast.show();
    }
}
