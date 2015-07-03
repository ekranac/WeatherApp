package models;

public class Weather {

    public Location location = new Location();
    public Units units = new Units();
    public Wind wind = new Wind();
    public Atmosphere atmosphere = new Atmosphere();
    public Astronomy astronomy = new Astronomy();
    public Condition condition = new Condition();
    public Forecast forecast = new Forecast();


    public class Location
    {
        private String city;
        private String region;
        private String country;

        public String getCity()
        {
            return city;
        }
        public void setCity(String city)
        {
            this.city = city;
        }

        public String getRegion()
        {
            return region;
        }
        public void setRegion(String region)
        {
            this.region = region;
        }

        public String getCountry()
        {
            return country;
        }
        public void setCountry(String country)
        {
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
    }

    public class Units
    {
        private String temperature;
        private String distance;
        private String pressure;
        private String speed;

        public String getTemperature()
        {
            return temperature;
        }
        public void setTemperature(String temperature)
        {
            this.temperature = temperature;
        }

        public String getDistance()
        {
            return distance;
        }
        public void setDistance(String distance)
        {
            this.distance = distance;
        }

        public String getPressure()
        {
            return pressure;
        }
        public void setPressure(String pressure)
        {
            this.pressure = pressure;
        }

        public String getSpeed()
        {
            return speed;
        }
        public void setSpeed(String speed)
        {
            this.speed = speed;
        }

    }

    public class Wind
    {
        private String speed;

        public String getSpeed()
        {
            return speed;
        }
        public void setSpeed(String speed)
        {
            this.speed = speed;
        }

    }

    public class Atmosphere
    {
        private String humidity;
        private String visibility;
        private String pressure;

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getVisibility() {
            return visibility;
        }

        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }
    }

    public class Astronomy
    {
        private String sunrise;
        private String sunset;

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }
    }

    public class Condition
    {
        private String text;
        private String temp;
        private String code;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public class Forecast
    {
        private String day = "";
        private String date = "";
        private String low = "";
        private String high = "";
        private String text = "";
        private Integer forecastCount = 0;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getForecastCount() {
            return forecastCount;
        }

        public void setForecastCount(Integer forecastCount) {
            this.forecastCount = forecastCount;
        }

        public void addUpForecast(){
            this.forecastCount+=1;
        }
    }

}
