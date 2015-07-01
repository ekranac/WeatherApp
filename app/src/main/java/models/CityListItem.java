package models;

/**
 * Created by ziga on 30.6.2015.
 */
public class CityListItem {

    String city;
    String currentTemp;

    public CityListItem(String city, String currentTemp)
    {
        this.city = city;
        this.currentTemp = currentTemp;
    }

    public String getCity() {
        return city;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }
}
