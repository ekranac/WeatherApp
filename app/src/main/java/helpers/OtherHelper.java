package helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ziga.weatherapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        String woeidString = "";

        if(prefs.getString("Woeids", null)!=null)
        {
            woeidString = prefs.getString("Woeids", null);
            List<String> list = Arrays.asList(woeidString.split("  "));
            for(String id : list) // Go throught every woeid, check if it's equal to the one you want to add, reposition to ArrayList
            {
                if(id.equals(woeid))
                {
                    alreadyExists = true;
                }
            }
        }


        if(alreadyExists == false) // Add the NEW woeid only if it doesn't exist
        {
            woeidString = prefs.getString("Woeids", null);

            if(position==null)
            {
                woeidString = woeidString + woeid + "  ";
            }

            else
            {
                if(woeidString==null || woeidString=="")
                {
                    woeidString="";
                }
                woeidString = woeid + "  " + woeidString;
            }
        }

        prefs.edit().putString("Woeids", woeidString).apply();
    }

    public void addCityToSharedPreferences(String city, Integer position)
    {
        Boolean alreadyExists = false;
        SharedPreferences prefs = this.getMyPreferences();
        String cityString = "";

        if(prefs.getString("Cities", null)!=null)
        {
            cityString = prefs.getString("Cities", null);
            List<String> list = Arrays.asList(cityString.split("  "));
            for(String cityName : list)
            {
                if(cityName.equals(city))
                {
                    alreadyExists = true;
                }
            }
        }


        if(alreadyExists == false)
        {
            cityString = prefs.getString("Woeids", null);

            if(position==null)
            {
                cityString = cityString + city + "  ";
            }

            else
            {
                if(cityString==null || cityString=="")
                {
                    cityString="";
                }
                cityString = city + "  " + cityString;
            }
        }


        prefs.edit().putString("Cities", cityString).apply();
    }

    public int getCityCount()
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> woeidsList = null;
        try {
            woeidsList = Arrays.asList(prefs.getString("Woeids", null).split("  "));
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

        String cityString = prefs.getString("Cities", null);
        cityString = cityString + name + "  ";


        prefs.edit().putString("Cities", cityString).apply();

    }

    public String getCurrentCity()
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> cityList = Arrays.asList(prefs.getString("Cities", null).split("  "));

        return cityList.get(0);
    }

    public void removeCity(Integer position, ListView listView,  Activity activity)
    {
        SharedPreferences prefs = this.getMyPreferences();

        String cityString = prefs.getString("Cities", null);
        String woeidString = prefs.getString("Woeids", null);
        List<String> cities = Arrays.asList(cityString.split("  "));
        List<String> woeids = Arrays.asList(woeidString.split("  "));

        String city = cities.get(position) + "  ";
        String woeid = woeids.get(position) + "  ";

        cityString = cityString.replace(city, "");
        woeidString = woeidString.replace(woeid, "");

        prefs.edit().putString("Cities", cityString).apply();
        prefs.edit().putString("Woeids", woeidString).apply();



        ViewPager vp = (ViewPager) activity.findViewById(R.id.pager);
        vp.getAdapter().notifyDataSetChanged();
        this.refreshListViewAdapter(activity);


        Toast.makeText(context, "Removed :)", Toast.LENGTH_SHORT).show();
    }

    public void refreshListViewAdapter(Activity activity)
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> cities = Arrays.asList(prefs.getString("Cities", null).split("  "));
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                context,
                R.layout.list_city,
                R.id.list_city_textview,
                cities
        );

        ListView listView = (ListView) activity.findViewById(R.id.city_listview);
        listView.setAdapter(mAdapter);
    }
}
