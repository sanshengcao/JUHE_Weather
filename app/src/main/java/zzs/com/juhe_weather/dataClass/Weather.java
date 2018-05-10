package zzs.com.juhe_weather.dataClass;

import java.util.ArrayList;

/**
 * Created by ENIAC on 2018/4/2.
 */

public class Weather {

    private Real_time_weather real_time_weather;
    private TodayWeather todayWeather;
    private ArrayList<FutureWeather> futureList;

    public Real_time_weather getReal_time_weather() {
        return real_time_weather;
    }

    public TodayWeather getTodayWeather() {
        return todayWeather;
    }

    public ArrayList<FutureWeather> getFutureList() {
        return futureList;
    }

    public void setReal_time_weather(Real_time_weather real_time_weather) {
        this.real_time_weather = real_time_weather;
    }

    public void setTodayWeather(TodayWeather todayWeather) {
        this.todayWeather = todayWeather;
    }

    public void setFutureList(ArrayList<FutureWeather> futureList) {
        this.futureList = futureList;
    }
}
