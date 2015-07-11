package helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
    private String WOEIDS = "Woeids";
    private String CITIES = "Cities";


    public OtherHelper(Context context)
    {
        this.context = context;
    }


    public void addWoeidToSharedPreferences(String woeid, Integer position)
    {
        SharedPreferences prefs = this.getMyPreferences();
        String woeidString = "";

        if(prefs.getString(WOEIDS, null)==null)
        {
            prefs.edit().putString(WOEIDS, woeid + "  ").apply();
        }
        else
        {

            woeidString = prefs.getString(WOEIDS, null);

            if(!woeidString.contains(woeid))
            {

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

            prefs.edit().putString(WOEIDS, woeidString).apply();
        }

    }

    public void addCityToSharedPreferences(String city, Integer position)
    {
        SharedPreferences prefs = this.getMyPreferences();
        String cityString = "";

        if(prefs.getString(CITIES, null)==null)
        {
            prefs.edit().putString(CITIES, city + "  ").apply();
        }
        else
        {
            cityString = prefs.getString(CITIES, null);

            if(!cityString.contains(city))
            {

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
            else
            {
                Toast.makeText(context, "Already added " + city, Toast.LENGTH_SHORT).show();
            }

            prefs.edit().putString(CITIES, cityString).apply();
        }
    }

    public int getCityCount()
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> woeidsList = null;
        try {
            woeidsList = Arrays.asList(prefs.getString(WOEIDS, null).split("  "));
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

    public String getCurrentCity()
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> cityList = Arrays.asList(prefs.getString("Cities", null).split("  "));


        return cityList.get(0);
    }

    public void removeCity(Integer position, ListView listView,  Activity activity)
    {
        SharedPreferences prefs = this.getMyPreferences();

        String cityString = prefs.getString(CITIES, null);
        String woeidString = prefs.getString(WOEIDS, null);
        List<String> cities = Arrays.asList(cityString.split("  "));
        List<String> woeids = Arrays.asList(woeidString.split("  "));

        String city = cities.get(position) + "  ";
        String woeid = woeids.get(position) + "  ";

        cityString = cityString.replace(city, "");
        woeidString = woeidString.replace(woeid, "");

        prefs.edit().putString(CITIES, cityString).apply();
        prefs.edit().putString(WOEIDS, woeidString).apply();



        ViewPager vp = (ViewPager) activity.findViewById(R.id.pager);
        vp.getAdapter().notifyDataSetChanged();
        new setListContent(listView, new OtherHelper(context), context).execute(); // Refresh list


        Toast.makeText(context, "Removed :)", Toast.LENGTH_SHORT).show();
    }

    public boolean getUnits()
    {
        SharedPreferences prefs = this.getMyPreferences();
        Boolean isFahrenheit = prefs.getBoolean("isFahrenheit", false);

        return isFahrenheit;
    }

}
