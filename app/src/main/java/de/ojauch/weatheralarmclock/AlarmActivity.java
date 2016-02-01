package de.ojauch.weatheralarmclock;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;

import de.ojauch.weatheralarmclock.weather.WeatherApi;

public class AlarmActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String EXTRA_CITY = "de.ojauch.weatheralarmclock.city";
    public static final String EXTRA_RAIN = "de.ojauch.weatheralarmclock.rain";
    public static final String EXTRA_FREEZING = "de.ojauch.weatheralarmclock.freezing";
    public static final String EXTRA_LOCATION_ACTIVATED = "de.ojauch.weatheralarmclock.location_activated";

    private WeatherApi weatherApi;
    private String mCity;
    private boolean mRain;
    private boolean mFreezing;
    private boolean mLocationActivated;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mCity = intent.getStringExtra(EXTRA_CITY);
        mRain = intent.getBooleanExtra(EXTRA_RAIN, false);
        mFreezing = intent.getBooleanExtra(EXTRA_FREEZING, false);
        mLocationActivated = intent.getBooleanExtra(EXTRA_LOCATION_ACTIVATED, false);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        weatherApi = new WeatherApi();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mLocationActivated) {
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            } catch (SecurityException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "App needs location permission.",
                        Toast.LENGTH_LONG);
                toast.show();
                Log.e(MainActivity.LOG_TAG, "location permission not given");
            }
        }
        new CheckRainTask().execute(mCity);
    }


    private class CheckRainTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... cities) {
            if (mLastLocation != null) {
                Log.d(MainActivity.LOG_TAG, "lon: " + mLastLocation.getLongitude());
                Log.d(MainActivity.LOG_TAG, "lat: " + mLastLocation.getLatitude());
                try {
                    if (mRain && !weatherApi.isRaining(mLastLocation))
                        return false;
                    if (mFreezing && !weatherApi.isFreezing(mLastLocation))
                        return false;
                    return true;
                } catch (IOException e) {
                    Log.d("weather_alarm", e.getMessage());
                    return true;
                } catch (XmlPullParserException e) {
                    Log.d("weather_alarm", e.getMessage());
                    return true;
                }
            } else {
                try {
                    if (mRain && !weatherApi.isRaining(cities[0]))
                        return false;
                    if (mFreezing && !weatherApi.isFreezing(cities[0]))
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
