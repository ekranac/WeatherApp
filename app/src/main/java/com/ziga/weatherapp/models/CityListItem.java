package com.ziga.weatherapp.models;

/**
 * Created by ziga on 30.6.2015.
 */
public class CityListItem {

    String city;
    String currentTemp;
    String code;

    public CityListItem(String city, String currentTemp, String code)
    {
        this.city = city;
        this.currentTemp = currentTemp;
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public String getCurrentTemp() {
        return currentTemp;
    }

    public String getCode() {
        return code;
    }

}
