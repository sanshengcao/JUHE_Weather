package zzs.com.juhe_weather.dataClass;

/**
 * Created by ENIAC on 2018/4/2.
 */

public class Real_time_weather {

    private String temp;
    private String wind_direction;
    private String wind_strength;
    private String humidity;
    private String time;

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return temp;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public void setWind_strength(String wind_strength) {
        this.wind_strength = wind_strength;
    }

    public String getWind_strength() {
        return wind_strength;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

}
