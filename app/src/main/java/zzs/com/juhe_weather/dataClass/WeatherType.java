package zzs.com.juhe_weather.dataClass;

/**
 * Created by ENIAC on 2018/4/3.
 */

public class WeatherType {
    private String wid;
    private  String weather;

    public String getWeather() {
        return weather;
    }

    public String getWid() {
        return wid;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    @Override
    public String toString() {
        return "wid = "+wid+" weather = "+weather ;
    }
}
