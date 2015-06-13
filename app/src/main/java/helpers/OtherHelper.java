package helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ziga on 28.5.2015.
 */
public class OtherHelper {

    Context context;

    public OtherHelper(Context context)
    {
        this.context = context;
    }


    public void addWoeidToSharedPreferences(String woeid, Integer position)
    {
        SharedPreferences prefs = this.getMyPreferences();
        ArrayList<String> woeidList = new ArrayList<String>();
        if(prefs.getString("Woeids", null)!=null)
        {
            String woeidString = prefs.getString("Woeids", null);
            List<String> list = Arrays.asList(woeidString.split(","));
            for(String id : list)
            {
                woeidList.add(id);
            }
        }

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

        prefs.edit().putString("Woeids", TextUtils.join(",", woeidList)).apply();
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

}
