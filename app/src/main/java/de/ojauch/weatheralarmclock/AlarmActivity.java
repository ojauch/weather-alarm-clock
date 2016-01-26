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

    private WeatherApi weatherApi;
    private boolean rain;
    private boolean freezing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String city = intent.getStringExtra(EXTRA_CITY);
        rain = intent.getBooleanExtra(EXTRA_RAIN, true);
        freezing = intent.getBooleanExtra(EXTRA_FREEZING, true);

        weatherApi = new WeatherApi();
        new CheckRainTask().execute(city);
    }

    private class CheckRainTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... cities) {
            try {
                if (rain && !weatherApi.isRaining(cities[0]))
                    return false;
                if (freezing && !weatherApi.isFreezing(cities[0]))
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
        protected void onPostExecute(Boolean isRaining) {
            if (isRaining) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.getTime().getHours();
                int minutes = calendar.getTime().getMinutes();

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, hours);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes + 1);

                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), "set alarm", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "don't set alarm", Toast.LENGTH_LONG);
                toast.show();
            }
            System.exit(0);
        }
    }
}
