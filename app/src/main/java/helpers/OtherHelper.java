package helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OtherHelper {

    Context context;

    public OtherHelper(Context context)
    {
        this.context = context;
    }


    public void addWoeidToSharedPreferences(String woeid, Integer position)
    {
        Boolean alreadyExists = false; // To check whether the woeid is already saved in preferences
        SharedPreferences prefs = this.getMyPreferences();
        ArrayList<String> woeidList = new ArrayList<String>();

        if(prefs.getString("Woeids", null)!=null)
        {
            String woeidString = prefs.getString("Woeids", null);
            List<String> list = Arrays.asList(woeidString.split(","));
            for(String id : list) // Go throught every woeid, check if it's equal to the one you want to add, reposition to ArrayList
            {
                if(id.equals(woeid))
                {
                    alreadyExists = true;
                }
                woeidList.add(id); // Add the existent ones to an ArrayList
            }
        }


        if(alreadyExists == false) // Add the NEW woeid only if it doesn't exist
        {
            if(position==null)
            {
                woeidList.add(woeid);
            }
            else
            {
                if(woeidList.isEmpty())
                {
                    woeidList.add(0, "null");
                }
                woeidList.set(position, woeid);
            }
        }

        prefs.edit().putString("Woeids", TextUtils.join(",", woeidList)).apply();
    }

    public void addCityToSharedPreferences(String city, Integer position)
    {
        Boolean alreadyExists = false;
        SharedPreferences prefs = this.getMyPreferences();
        ArrayList<String> cityList = new ArrayList<String>();

        if(prefs.getString("Cities", null)!=null)
        {
            String woeidString = prefs.getString("Cities", null);
            List<String> list = Arrays.asList(woeidString.split(","));
            for(String cityName : list)
            {
                if(cityName.equals(city))
                {
                    alreadyExists = true;
                }
                cityList.add(cityName);
            }
        }


        if(alreadyExists == false)
        {
            if(position==null)
            {
                cityList.add(city);
            }
            else
            {
                if(cityList.isEmpty())
                {
                    cityList.add(0, "null");
                }
                cityList.set(position, city);
            }
        }


        prefs.edit().putString("Cities", TextUtils.join(",", cityList)).apply();
    }

    public int getCityCount()
    {
        SharedPreferences prefs = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        List<String> woeidsList = null;
        try {
            woeidsList = Arrays.asList(prefs.getString("Woeids", null).split(","));
        } catch(Throwable t) {}

        if(woeidsList==null || woeidsList.size()==1)
        {
            return 3;
        }

        else
        {
            return woeidsList.size() + 2;
        }

    }

    public SharedPreferences getMyPreferences()
    {
        SharedPreferences prefs = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        return prefs;
    }

    public void setCurrentLocationData()
    {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = YahooClient.getLastKnownLocation(lm, context);
        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());

        String url = YahooClient.makeCurrentLocationURL(latitude, longitude); // Creates URL based on current location
        Log.i("CURRENT URL", url);
        HttpURLConnection yahooHttpConn = null;

        try {
            yahooHttpConn = (HttpURLConnection) (new URL(url)).openConnection();
            yahooHttpConn.connect();
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(yahooHttpConn.getInputStream()));

            int event = parser.getEventType();

            String tagName = null;
            String currentTag = null;
            while(event!=XmlPullParser.END_DOCUMENT) // Loop through XML data
            {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    currentTag = tagName;
                }
                else if(event == XmlPullParser.TEXT)
                {
                    if("woeid".equals(currentTag)) // If tag is woeid
                    {
                        this.addWoeidToSharedPreferences(parser.getText(), 0); // Add the woeid to shared preferences
                    }
                    else if("city".equals(currentTag))
                    {
                        this.addCityToSharedPreferences(parser.getText(), 0);
                    }
                }
                event = parser.next();
            }
        }catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public void addCityName(String name)
    {
        SharedPreferences prefs = this.getMyPreferences();
        ArrayList<String> cityList = new ArrayList<String>();

        String cityString = prefs.getString("Cities", null);
        List<String> list = Arrays.asList(cityString.split(","));

        for(String city : list)
        {
            cityList.add(city);
            Log.i("LOOP CITY", city);
        }

        cityList.add(name);


        prefs.edit().putString("Cities", TextUtils.join(",", cityList)).apply();

    }

    public String getCurrentCity()
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> cityList = Arrays.asList(prefs.getString("Cities", null).split(","));

        return cityList.get(0);
    }

}
