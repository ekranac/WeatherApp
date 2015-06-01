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

    public static void addWoeidToSharedPreferences(String woeid, Integer position, Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

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

    public static int getCityCount(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        List<String> woeidsList = Arrays.asList(prefs.getString("Woeids", null).split(","));

        return woeidsList.size();

    }
}
