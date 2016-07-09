package de.ojauch.weatheralarmclock;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;

import de.ojauch.weatheralarmclock.weather.WeatherApi;

public class AlarmActivity extends AppCompatActivity {

    public static final String EXTRA_CITY = "de.ojauch.weatheralarmclock.city";
    public static final String EXTRA_RAIN = "de.ojauch.weatheralarmclock.rain";
    public static final String EXTRA_FREEZING = "de.ojauch.weatheralarmclock.freezing";
    public static final String EXTRA_TIME_SHIFT = "de.ojauch.weatheralarmclock.time_shift";

    private WeatherApi weatherApi;
    private boolean rain;
    private boolean freezing;
    private int timeShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String city = intent.getStringExtra(EXTRA_CITY);
        rain = intent.getBooleanExtra(EXTRA_RAIN, true);
        freezing = intent.getBooleanExtra(EXTRA_FREEZING, true);
        timeShift = intent.getIntExtra(EXTRA_TIME_SHIFT, 15);

        weatherApi = new WeatherApi();
        new CheckRainTask().execute(city);
    }

    private class CheckRainTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... cities) {
            try {
                if ((rain && !weatherApi.isRaining(cities[0])) && (freezing && !weatherApi.isFrost(cities[0])))
                    return false;
                return true;
            } catch (IOException e) {
                Log.d("weather_alarm", e.getMessage());
                return true;
            } catch (XmlPullParserException e) {
                Log.d("weather_alarm", e.getMessage());
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean ringEarlier) {
            if (ringEarlier) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.getTime().getHours();
                int minutes = calendar.getTime().getMinutes();

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, hours);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes + 1);

                Log.d(MainActivity.LOG_TAG, "ring earlier");
                startActivity(intent);
            } else {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.getTime().getHours();
                int minutes = calendar.getTime().getMinutes();

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, hours + (timeShift / 60));
                intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes + (timeShift % 60));

                Log.d(MainActivity.LOG_TAG, "ring at normal time");
                startActivity(intent);
            }
            System.exit(0);
        }
    }
}
