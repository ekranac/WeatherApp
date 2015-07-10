package helpers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.CityResult;
import models.Weather;

/**
 * Created by ziga on 15.5.2015.
 */
public class YahooClient {

    public static String YAHOO_GEO_URL = "http://where.yahooapis.com/v1";
    public static String YAHOO_WEATHER_URL = "http://weather.yahooapis.com/forecastrss";
    public static String YAHOO_CURRENT_LOCATION_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22";

    private static String APPID = "7655292f7edf8406f7c080d64baa1c43910976c0--";

    public static List<CityResult> getCityList(String cityName) {
        List<CityResult> result = new ArrayList<CityResult>();
        HttpURLConnection yahooHttpConn = null;
        try {
            String query = makeQueryCityURL(cityName);
            yahooHttpConn= (HttpURLConnection) (new URL(query)).openConnection();
            yahooHttpConn.connect();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));
            int event = parser.getEventType();

            CityResult cty = null;
            String tagName = null;
            String currentTag = null;

            // We start parsing the XML
            while (event != XmlPullParser.END_DOCUMENT) {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    if (tagName.equals("place")) {
                        // place Tag Found so we create a new CityResult
                        cty = new CityResult();

                    }
                    currentTag = tagName;
                }
                else if (event == XmlPullParser.TEXT) {
                    // We found some text. let's see the tagName to know the tag related to the text
                    if ("woeid".equals(currentTag))
                        cty.setWoeid(parser.getText());
                    else if ("name".equals(currentTag))
                        cty.setCityName(parser.getText());
                    else if ("country".equals(currentTag))
                        cty.setCountry(parser.getText());
                    else if ("admin1".equals(currentTag))
                        cty.setRegion(parser.getText());

                    // We don't want to analyze other tag at the moment
                }
                else if (event == XmlPullParser.END_TAG) {
                    if ("place".equals(tagName))
                        result.add(cty);
                }

                event = parser.next();
            }
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try {
                yahooHttpConn.disconnect();
            }
            catch(Throwable ignore) {}

        }
        return result;
    }

    public static Weather getWeatherData(String url, HttpURLConnection yahooHttpConn)
    {
        Weather weather = new Weather();
        try {

            yahooHttpConn = (HttpURLConnection) (new URL(url)).openConnection();
            yahooHttpConn.connect();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));

            int event = parser.getEventType();

            String tagName = null;
            while(event!=XmlPullParser.END_DOCUMENT) // Loop through XML data
            {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    if(tagName.equals("yweather:wind"))
                    {
                        weather.wind.setSpeed(parser.getAttributeValue(null, "speed"));
                    }

                    else if(tagName.equals("yweather:atmosphere"))
                    {
                        weather.atmosphere.setHumidity(parser.getAttributeValue(null, "humidity"));
                        weather.atmosphere.setPressure(parser.getAttributeValue(null, "pressure"));
                        weather.atmosphere.setVisibility(parser.getAttributeValue(null, "visibility"));
                    }

                    else if (tagName.equals("yweather:condition")) {
                        weather.condition.setTemp(parser.getAttributeValue(null, "temp"));
                        weather.condition.setText(parser.getAttributeValue(null, "text"));
                        weather.condition.setCode(parser.getAttributeValue(null, "code"));
                    }

                    else if (tagName.equals("yweather:units")) {
                        weather.units.setPressure(parser.getAttributeValue(null, "pressure"));
                        weather.units.setDistance(parser.getAttributeValue(null, "distance"));
                        weather.units.setSpeed(parser.getAttributeValue(null, "speed"));
                        weather.units.setTemperature("°" + parser.getAttributeValue(null, "temperature"));
                    }

                    else if (tagName.equals("yweather:location")) {
                        weather.location.setCity(parser.getAttributeValue(null, "city"));
                        weather.location.setCountry(parser.getAttributeValue(null, "country"));
                        weather.location.setRegion(parser.getAttributeValue(null, "region"));
                    }

                    else if (tagName.equals("yweather:forecast")) {
                        weather.forecast.addUpForecast();

                        if(weather.forecast.getForecastCount() < 5)
                        {
                            weather.forecast.setDay(weather.forecast.getDay() + parser.getAttributeValue(null, "day") + "  ");
                            weather.forecast.setDate(weather.forecast.getDate() + parser.getAttributeValue(null, "date") + "  ");
                            weather.forecast.setLow(weather.forecast.getLow() + parser.getAttributeValue(null, "low") + "  ");
                            weather.forecast.setHigh(weather.forecast.getHigh() + parser.getAttributeValue(null, "high") + "  ");
                            weather.forecast.setText(weather.forecast.getText() + parser.getAttributeValue(null, "text") + "  ");
                            if(weather.forecast.getForecastCount() < 5 && weather.forecast.getForecastCount()!=1)
                            {
                                weather.forecast.setCode(weather.forecast.getCode() + parser.getAttributeValue(null, "code") + "  ");
                            }
                        }
                        else if(weather.forecast.getForecastCount() == 5)
                        {
                            weather.forecast.setDay(weather.forecast.getDay() + parser.getAttributeValue(null, "day"));
                            weather.forecast.setDate(weather.forecast.getDate() + parser.getAttributeValue(null, "date"));
                            weather.forecast.setLow(weather.forecast.getLow() + parser.getAttributeValue(null, "low"));
                            weather.forecast.setHigh(weather.forecast.getHigh() + parser.getAttributeValue(null, "high"));
                            weather.forecast.setText(weather.forecast.getText() + parser.getAttributeValue(null, "text"));
                        }
                    }

                    else if(tagName.equals("yweather:astronomy"))
                    {
                        weather.astronomy.setSunrise(parser.getAttributeValue(null, "sunrise"));
                        weather.astronomy.setSunset(parser.getAttributeValue(null, "sunset"));
                    }

                }
                event = parser.next();
            }
        }catch(Throwable t) {
            t.printStackTrace();
        }

        return weather;
    }

    private static String makeQueryCityURL(String cityName) {
        // We remove spaces in cityName
        cityName = cityName.replaceAll(" ", "%20");
        return YAHOO_GEO_URL + "/places.q(" + cityName + "%2A);count=" + 10 + "?appid=" + APPID; // 10- number of results
    }

    public static String makeWeatherURL(String woeid, String unit) {
        return  YAHOO_WEATHER_URL + "?w=" + woeid + "&u=" + unit;
    }

    public static String makeCurrentLocationURL(String lat, String lon)
    {
        return YAHOO_CURRENT_LOCATION_URL + lat + "%2C" + lon + "%22%20and%20gflags%3D%22R%22&format=xml";
    }

    public static Location getLastKnownLocation(LocationManager mLocationManager, Context c) {
        mLocationManager = (LocationManager) c.getApplicationContext().getSystemService(c.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}


