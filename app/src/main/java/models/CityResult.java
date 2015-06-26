package models;

public class CityResult {

    private String woeid;
    private String cityName;
    private String region;
    private String country;

    public CityResult() {}

    public CityResult(String woeid, String cityName, String country) {
        this.woeid = woeid;
        this.cityName = cityName;
        this.country = country;
    }

    public CityResult(String woeid, String cityName, String region, String country) {
        this.woeid = woeid;
        this.cityName = cityName;
        this.region = region;
        this.country = country;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        String result = "";

        if(region==cityName || region==country)
        {
            result = cityName + ", " + country;
        }
        else
        {
            result = cityName + ", " + region + ", " + country;
        }

        return result;
    }
}