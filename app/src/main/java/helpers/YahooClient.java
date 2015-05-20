package helpers;

import android.util.Log;

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

    private static String APPID = "7655292f7edf8406f7c080d64baa1c43910976c0--";

    public static List<CityResult> getCityList(String cityName) {
        List<CityResult> result = new ArrayList<CityResult>();
        HttpURLConnection yahooHttpConn = null;
        try {
            String query = makeQueryCityURL(cityName);
            //Log.d("Swa", "URL [" + query + "]");
            yahooHttpConn= (HttpURLConnection) (new URL(query)).openConnection();
            yahooHttpConn.connect();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));
            Log.d("Swa", "XML Parser ok");
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
                        //  Log.d("Swa", "New City found");
                    }
                    currentTag = tagName;
                    // Log.d("Swa", "Tag ["+tagName+"]");
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
            // Log.e("Error in getCityList", t.getMessage());
        }
        finally {
            try {
                yahooHttpConn.disconnect();
            }
            catch(Throwable ignore) {}

        }
        return result;
    }

    /*public static void getWeather(String woeid, String unit, RequestQueue rq, final WeatherClientListener listener) {
        String url2Call = makeWeatherURL(woeid, unit);
        // Log.d("SwA", "Weather URL ["+url2Call+"]");
        final Weather result = new Weather();
        StringRequest req = new StringRequest(Request.Method.GET, url2Call, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                parseResponse(s, result);
                listener.onWeatherResponse(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        rq.add(req);
    }

    private static Weather parseResponse (String resp, Weather result) {
        // Log.d("SwA", "Response ["+resp+"]");
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(resp));

            String tagName = null;
            String currentTag = null;

            int event = parser.getEventType();
            boolean isFirstDayForecast = true;
            while (event != XmlPullParser.END_DOCUMENT) {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    if (tagName.equals("yweather:wind")) {
                        // Log.d("SwA", "Tag [Wind]");
                        result.wind.chill = Integer.parseInt(parser.getAttributeValue(null, "chill"));
                        result.wind.direction = Integer.parseInt(parser.getAttributeValue(null, "direction"));
                        result.wind.speed  = (int) Float.parseFloat(parser.getAttributeValue(null, "speed"));
                    }
                    else if (tagName.equals("yweather:atmosphere")) {
                        // Log.d("SwA", "Tag [Atmos]");
                        result.atmosphere.humidity = Integer.parseInt(parser.getAttributeValue(null, "humidity"));
                        result.atmosphere.visibility = Float.parseFloat(parser.getAttributeValue(null, "visibility"));
                        result.atmosphere.pressure = Float.parseFloat(parser.getAttributeValue(null, "pressure"));
                        result.atmosphere.rising = Integer.parseInt(parser.getAttributeValue(null, "rising"));
                    }
                    else if (tagName.equals("yweather:forecast")) {
                        //  Log.d("SwA", "Tag [Fore]");
                        if (isFirstDayForecast) {
                            result.forecast.code = Integer.parseInt(parser.getAttributeValue(null, "code"));
                            result.forecast.tempMin = Integer.parseInt(parser.getAttributeValue(null, "low"));
                            result.forecast.tempMax = Integer.parseInt(parser.getAttributeValue(null, "high"));
                            isFirstDayForecast = false;
                        }
                    }
                    else if (tagName.equals("yweather:condition")) {
                        //  Log.d("SwA", "Tag [Condition]");
                        result.condition.code = Integer.parseInt(parser.getAttributeValue(null, "code"));
                        result.condition.description = parser.getAttributeValue(null, "text");
                        result.condition.temp = Integer.parseInt(parser.getAttributeValue(null, "temp"));
                        result.condition.date = parser.getAttributeValue(null, "date");
                    }
                    else if (tagName.equals("yweather:units")) {
                        //   Log.d("SwA", "Tag [units]");
                        result.units.temperature = "°" + parser.getAttributeValue(null, "temperature");
                        result.units.pressure = parser.getAttributeValue(null, "pressure");
                        result.units.distance = parser.getAttributeValue(null, "distance");
                        result.units.speed = parser.getAttributeValue(null, "speed");
                    }
                    else if (tagName.equals("yweather:location")) {
                        result.location.name = parser.getAttributeValue(null, "city");
                        result.location.region = parser.getAttributeValue(null, "region");
                        result.location.country = parser.getAttributeValue(null, "country");
                    }
                    else if (tagName.equals("image"))
                        currentTag = "image";
                    else if (tagName.equals("url")) {
                        if (currentTag == null) {
                            result.imageUrl = parser.getAttributeValue(null, "src");
                        }
                    }
                    else if (tagName.equals("lastBuildDate")) {
                        currentTag="update";
                    }
                    else if (tagName.equals("yweather:astronomy")) {
                        result.astronomy.sunRise = parser.getAttributeValue(null, "sunrise");
                        result.astronomy.sunSet = parser.getAttributeValue(null, "sunset");
                    }

                }
                else if (event == XmlPullParser.END_TAG) {
                    if ("image".equals(currentTag)) {
                        currentTag = null;
                    }
                }
                else if (event == XmlPullParser.TEXT) {
                    if ("update".equals(currentTag))
                        result.lastUpdate = parser.getText();
                }
                event = parser.next();
            }

        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return result;
    }*/

    private static String makeQueryCityURL(String cityName) {
        // We remove spaces in cityName
        cityName = cityName.replaceAll(" ", "%20");
        return YAHOO_GEO_URL + "/places.q(" + cityName + "%2A);count=" + 10 + "?appid=" + APPID; // 10- number of results
    }

    private static String makeWeatherURL(String woeid, String unit) {
        return  YAHOO_WEATHER_URL + "?w=" + woeid + "&u=" + unit;
    }


    /* Pubblic listener interface */

    public static interface WeatherClientListener {
        public void onWeatherResponse(Weather weather);
    }
}
