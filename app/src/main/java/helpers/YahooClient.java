package helpers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.io.StringReader;
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
            // Log.d("Swa", "URL [" + query + "]");
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

    public static void getCurrentLocationWoeid(final String lat, final String lon, final Context c)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = YahooClient.makeCurrentLocationURL(lat, lon);
                Log.i("THE URL", url);
                HttpURLConnection yahooHttpConn = null;

                try {
                    yahooHttpConn = (HttpURLConnection) (new URL(url)).openConnection();
                    yahooHttpConn.connect();
                    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));

                    int event = parser.getEventType();

                    String tagName = null;
                    String currentTag = null;
                    while(event!=XmlPullParser.END_DOCUMENT)
                    {
                        tagName = parser.getName();

                        if (event == XmlPullParser.START_TAG) {
                            currentTag = tagName;
                        }
                        else if(event == XmlPullParser.TEXT)
                        {
                            if("woeid".equals(currentTag))
                            {
                                OtherHelper helper = new OtherHelper(c);
                                helper.addWoeidToSharedPreferences(parser.getText(), 0);
                            }
                        }
                        event = parser.next();
                    }
                }catch(Throwable t) {
                    t.printStackTrace();
                    // Log.e("Error in getCityList", t.getMessage());
                }

            }
        }).start();

    }

    public void getWeather()
    {

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
