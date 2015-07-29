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

        switch(country)
        {
            case "United States":
                country = "US";
                break;
            case "United Kingdom":
                country = "UK";
                break;
            case "United Arab Emirates":
                country = "UAE";
                break;
            case "Democratic Republic of Congo":
                country = "DOC";
                break;
        }

        return country;
    }

    public void setCountry(String country) {

        switch(country)
        {
            case "United States":
                country = "US";
                break;
            case "United Kingdom":
                country = "UK";
                break;
            case "United Arab Emirates":
                country = "UAE";
                break;
            case "Democratic Republic of Congo":
                country = "DOC";
                break;
        }
        this.country = country;
    }

    @Override
    public String toString() {
        String result = "";

        if(region!=null || region!="" || region!="null")
        {
            result = cityName + ", " + region + ", " + country;
        }
        else
        {
            result = cityName + ", " + country;
        }

        return result;
    }
}