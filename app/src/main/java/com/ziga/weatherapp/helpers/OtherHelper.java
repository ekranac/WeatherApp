package com.ziga.weatherapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ziga.weatherapp.R;

import java.util.Arrays;
import java.util.List;


public class OtherHelper {

    Context context;
    private String PREF_KEY_WOEIDS = "Woeids";
    private String PREF_KEY_CITIES = "Cities";
    private String MY_PREF_KEY = "MyPreferences";


    public OtherHelper(Context context)
    {
        this.context = context;
    }

    public void addDataToSharedPreferences(String woeid, String city, Boolean callFromSearch)
    {
        SharedPreferences prefs = this.getMyPreferences();

        String woeidString = prefs.getString(PREF_KEY_WOEIDS, null);
        String cityString = prefs.getString(PREF_KEY_CITIES, null);

        if(woeidString==null && cityString==null)
        {
            prefs.edit().putString(PREF_KEY_WOEIDS, woeid + "  ").apply();
            prefs.edit().putString(PREF_KEY_CITIES, city + "  ").apply();
        }

        else
        {
            if(!cityString.toLowerCase().contains(city.toLowerCase()) || !woeidString.toLowerCase().contains(woeid.toLowerCase()))
            {

                if(callFromSearch)
                {
                    cityString = cityString + city + "  ";
                    woeidString = woeidString + woeid + "  ";

                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    /*if(woeidString==null || woeidString=="" || cityString==null || cityString=="")
                    {
                        woeidString="";
                        cityString="";
                    }

                    woeidString = woeid + "  " + woeidString;
                    cityString = city + "  " + cityString;*/
                    List<String> woeids = Arrays.asList(woeidString.split("  "));
                    List<String> cities = Arrays.asList(cityString.split("  "));
                    woeids.set(0, woeid);
                    cities.set(0, city);

                    woeidString = woeids.toString();
                    cityString  = cities.toString();
                }
            }

            else
            {
                if(callFromSearch && (cityString.contains(city) || woeidString.contains(woeid)))
                {
                    Toast.makeText(context, "Already added " + city, Toast.LENGTH_SHORT).show();
                }
            }

            prefs.edit().putString(PREF_KEY_WOEIDS, woeidString).apply();
            prefs.edit().putString(PREF_KEY_CITIES, cityString).apply();
        }


    }
    public int getCityCount()
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> woeidsList = null;
        try {
            woeidsList = Arrays.asList(prefs.getString(PREF_KEY_WOEIDS, null).split("  "));
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
        SharedPreferences prefs = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE);
        return prefs;
    }

    public String getCurrentCity()
    {
        SharedPreferences prefs = this.getMyPreferences();
        List<String> cityList = Arrays.asList(prefs.getString(PREF_KEY_CITIES, null).split("  "));

        return cityList.get(0);
    }

    public void removeCity(Integer position, View rootView, ListView listView,  Activity activity)
    {
        SharedPreferences prefs = this.getMyPreferences();

        String cityString = prefs.getString(PREF_KEY_CITIES, null);
        String woeidString = prefs.getString(PREF_KEY_WOEIDS, null);
        List<String> cities = Arrays.asList(cityString.split("  "));
        List<String> woeids = Arrays.asList(woeidString.split("  "));

        String city = cities.get(position) + "  ";
        String woeid = woeids.get(position) + "  ";

        cityString = cityString.replace(city, "");
        woeidString = woeidString.replace(woeid, "");

        prefs.edit().putString(PREF_KEY_CITIES, cityString).apply();
        prefs.edit().putString(PREF_KEY_WOEIDS, woeidString).apply();



        ViewPager vp = (ViewPager) activity.findViewById(R.id.pager);
        vp.getAdapter().notifyDataSetChanged();
        new setListContent(listView, rootView, context).execute(); // Refresh list


        Toast.makeText(context, "Removed :)", Toast.LENGTH_SHORT).show();
    }

    public boolean getUnits()
    {
        SharedPreferences prefs = this.getMyPreferences();
        Boolean isFahrenheit = prefs.getBoolean("isFahrenheit", false);

        return isFahrenheit;
    }

}
