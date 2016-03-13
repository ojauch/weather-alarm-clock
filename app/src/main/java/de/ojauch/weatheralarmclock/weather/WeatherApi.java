package de.ojauch.weatheralarmclock.weather;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Gets the current weather from Open Weather Map
 *
 * @author Oskar Jauch
 */
public class WeatherApi {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "24d7fb2485706e8b6fd1a7d53cdb6589";
    private static final String FORMAT = "&mode=xml";
    private static final String UNITS = "&units=metric";
    private static final String ns = null;
    private static final String LOG_TAG = "weather_api";

    private Weather current;

    public WeatherApi() {
    }

    private void loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;

        Weather weather = null;

        try {
            stream = downloadUrl(urlString);
            weather = parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        current = weather;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    private Weather parse(InputStream in) throws XmlPullParserException, IOException {
        Weather weather = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            weather = readCurrent(parser);
        } finally {
            in.close();
        }
        return weather;
    }

    private Weather readCurrent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "current");
        Weather current = new Weather();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            switch (name) {
                case "temperature":
                    current.setTemperature(readTemperature(parser));
                    break;
                case "precipitation":
                    current.setPrecipitation(readPrecipitation(parser));
                    break;
            }
        }

        return current;
    }

    private City readCity(XmlPullParser parser) throws IOException, XmlPullParserException, NumberFormatException {
        City city = new City();
        parser.require(XmlPullParser.START_TAG, ns, "city");

        if (parser.getName().equals("city")) {
            // get id
            String strId = parser.getAttributeValue(ns, "id");
            Log.d("weather_api", strId);
            int id = Integer.parseInt(strId.trim());
            city.setCityId(id);

            // get name
            String name = parser.getAttributeValue(ns, "name");
            city.setCityName(name);
        }

        while (!parser.getName().equals("city")) {
            String name = parser.getName();

            switch (name) {
                case "coord":
                    parser.require(XmlPullParser.START_TAG, ns, "coord");
                    String strLon = parser.getAttributeValue(ns, "lon");
                    String strLat = parser.getAttributeValue(ns, "lat");
                    double lon = Double.valueOf(strLon);
                    double lat = Double.valueOf(strLat);
                    city.setCoord_lon(lon);
                    city.setCoord_lat(lat);
                    break;
                case "country":
                    parser.require(XmlPullParser.START_TAG, ns, "country");
                    String country = parser.getAttributeValue(ns, "country");
                    city.setCountry(country);
                    break;
                case "sun":
                    parser.require(XmlPullParser.START_TAG, ns, "sun");
                    String strRise = parser.getAttributeValue(ns, "rise");
                    Date rise = new Date(strRise);
                    city.setSunrise(rise);
                    String strSet = parser.getAttributeValue(ns, "set");
                    Date set = new Date(strSet);
                    city.setSunset(set);
                    break;
            }
            parser.nextTag();
        }

        return city;
    }

    private Precipitation readPrecipitation(XmlPullParser parser) throws IOException, XmlPullParserException {
        Precipitation precipitation = new Precipitation();

        parser.require(XmlPullParser.START_TAG, ns, "precipitation");

        // get mode
        String mode = parser.getAttributeValue(ns, "mode");
        parser.nextTag();
        switch (mode) {
            case "no":
                precipitation.setMode(Precipitation.PrecipitationMode.NO);
                break;
            case "rain":
                precipitation.setMode(Precipitation.PrecipitationMode.RAIN);
                break;
            default:
                precipitation.setMode(Precipitation.PrecipitationMode.SNOW);
        }

        // get value if there is precipitation
        if (precipitation.getMode() != Precipitation.PrecipitationMode.NO) {
            String strValue = parser.getAttributeValue(ns, "value");
            int value = Integer.getInteger(strValue);
            precipitation.setValue(value);
        }

        return precipitation;
    }

    private float readTemperature(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "temperature");
        String strTemp = parser.getAttributeValue(ns, "value");
        float temp = Float.valueOf(strTemp);
        parser.nextTag();
        return temp;
    }

    private int readHumidity(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "humidity");
        String strHumidity = parser.getAttributeValue(ns, "value");
        int humidity = Integer.valueOf(strHumidity);
        parser.nextTag();
        return humidity;
    }

    private float readPressure(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pressure");
        String strPressure = parser.getAttributeValue(ns, "value");
        float pressure = Float.valueOf(strPressure);
        parser.nextTag();
        return pressure;
    }

    private Wind readWind(XmlPullParser parser) throws IOException, XmlPullParserException {
        Wind wind = new Wind();
        parser.require(XmlPullParser.START_TAG, ns, "wind");
        parser.nextTag();

        while (!parser.getName().equals("wind")) {
            String name = parser.getName();

            switch (name) {
                case "speed":
                    parser.require(XmlPullParser.START_TAG, ns, "speed");
                    String strSpeed = parser.getAttributeValue(ns, "value");
                    float speed = Float.valueOf(strSpeed);
                    wind.setSpeed(speed);
                    break;
                case "direction":
                    String code = parser.getAttributeValue(ns, "code");
                    Wind.Direction direction = null;
                    Log.d(LOG_TAG, code);
                    switch (code) {
                        case "N":
                            direction = Wind.Direction.NORTH;
                            break;
                        case "E":
                            direction = Wind.Direction.EAST;
                            break;
                        case "S":
                            direction = Wind.Direction.SOUTH;
                            break;
                        case "W":
                            direction = Wind.Direction.WEST;
                            break;
                        // TODO: add other cases
                    }
                    wind.setDirection(direction);
                    break;
            }
            parser.nextTag();
        }
        return wind;
    }

    private int readClouds(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "clouds");
        String strValue = parser.getAttributeValue(ns, "value");
        int value = Integer.getInteger(strValue);
        parser.nextTag();
        return value;
    }

    private void refreshData(String city) throws IOException, XmlPullParserException {
        String url = BASE_URL + city + "&APPID=" + API_KEY + FORMAT + UNITS;
        loadXmlFromNetwork(url);
    }

    /**
     * Check if it's raining in a specific city
     *
     * @param city that should be checked
     * @return true if it's raining
     * @throws IOException
     * @throws XmlPullParserException
     */
    public boolean isRaining(String city) throws IOException, XmlPullParserException {
        refreshData(city);
        return !current.getPrecipitation().getMode().equals(Precipitation.PrecipitationMode.NO);
    }

    /**
     * Check if it's very cold in a specific city
     *
     * @param city that should be checked
     * @return true if the temperature is lower than 2 degrees celsius
     * @throws IOException
     * @throws XmlPullParserException
     */
    public boolean isFrost(String city) throws IOException, XmlPullParserException {
        refreshData(city);
        return (current.getTemperature() < 2);
    }
}
