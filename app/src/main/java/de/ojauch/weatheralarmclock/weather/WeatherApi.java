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

import de.ojauch.weatheralarmclock.weather.Weather;

/**
 * Gets the current weather from Open Weather Map
 * @author Oskar Jauch
 */
public class WeatherApi {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "24d7fb2485706e8b6fd1a7d53cdb6589";
    private static final String FORMAT = "&mode=xml";
    private static final String UNITS = "&units=metric";
    private static final String ns = null;

    private Weather current;

    private boolean rain = false;

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
                case "city":
                    current.setCity(readCity(parser));
                    break;
                case "temperature":
                    current.setTemperature(readTemperature(parser));
                    break;
                case "humidity":
                    current.setHumidity(readHumidity(parser));
                    break;
                case "pressure":
                    current.setPressure(readPressure(parser));
                    break;
                case "wind":
                    current.setWind(readWind(parser));
                    break;
                case "clouds":
                    current.setClouds(readClouds(parser));
                    break;
                case "precipitation":
                    current.setPrecipitation(readPrecipitation(parser));
                    break;
            }
        }

        return current;
    }

    private City readCity(XmlPullParser parser) throws IOException, XmlPullParserException {
        City city = new City();
        parser.require(XmlPullParser.START_TAG, ns, "city");

        // get id
        String strId = parser.getAttributeValue(ns, "id");
        int id = Integer.getInteger(strId);
        city.setCityId(id);

        // get name
        String name = parser.getAttributeValue(ns, "name");
        city.setCityName(name);

        // get coordinates
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, ns, "coord");
        String strLon = parser.getAttributeValue(ns, "lon");
        String strLat = parser.getAttributeValue(ns, "lat");
        double lon = Double.valueOf(strLon);
        double lat = Double.valueOf(strLat);
        city.setCoord_lon(lon);
        city.setCoord_lat(lat);

        // get country
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, ns, "country");
        String country = parser.getAttributeValue(ns, "country");
        city.setCountry(country);

        // get sunrise/sunset
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, ns, "sun");
        String strRise = parser.getAttributeValue(ns, "rise");
        Date rise = new Date(strRise);
        city.setSunrise(rise);
        String strSet = parser.getAttributeValue(ns, "set");
        Date set = new Date(strSet);
        city.setSunset(set);

        return city;
    }

    private Precipitation readPrecipitation(XmlPullParser parser) throws IOException, XmlPullParserException {
        Precipitation precipitation = new Precipitation();

        parser.require(XmlPullParser.START_TAG, ns, "precipitation");

        // get mode
        String mode = parser.getAttributeValue(ns, "mode");
        parser.nextTag();
        if (mode.equals("no")) {
            Log.d("weather_api", "not raining " + mode);
            precipitation.setMode(Precipitation.PrecipitationMode.NO);
        } else if (mode.equals("rain")) {
            Log.d("weather_api", "raining " + mode);
            precipitation.setMode(Precipitation.PrecipitationMode.RAIN);
        } else {
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

    private int readPressure(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pressure");
        String strPressure = parser.getAttributeValue(ns, "value");
        int pressure = Integer.valueOf(strPressure);
        parser.nextTag();
        return pressure;
    }

    private Wind readWind(XmlPullParser parser) throws IOException, XmlPullParserException {
        Wind wind = new Wind();
        parser.require(XmlPullParser.START_TAG, ns, "wind");
        parser.nextTag();

        // get wind speed
        parser.require(XmlPullParser.START_TAG, ns, "speed");
        String strSpeed = parser.getAttributeValue(ns, "value");
        float speed = Float.valueOf(strSpeed);
        wind.setSpeed(speed);

        // get wind direction
        parser.nextTag();
        String code = parser.getAttributeValue(ns, "code");
        Wind.Direction direction = null;
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
        }
        wind.setDirection(direction);
        parser.nextTag();
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

    public boolean isRaining(String city) throws IOException, XmlPullParserException {
        refreshData(city);
        return !current.getPrecipitation().getMode().equals(Precipitation.PrecipitationMode.NO);
    }

    public boolean isFreezing(String city) throws IOException, XmlPullParserException {
        refreshData(city);
        return (current.getTemperature() < 2);
    }
}
