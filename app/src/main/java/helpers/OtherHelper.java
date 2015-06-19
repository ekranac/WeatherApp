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
                woeidList.add(id);
            }
        }


        if(alreadyExists == false) // Add the woeid only if it doesn't exist
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

    public void setCurrentLocationWoeid()
    {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = YahooClient.getLastKnownLocation(lm, context);
        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());

        String url = YahooClient.makeCurrentLocationURL(latitude, longitude); // Creates URL based on current location
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
                }
                event = parser.next();
            }
        }catch(Throwable t) {
            t.printStackTrace();
        }
    }

}
