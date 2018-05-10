package zzs.com.juhe_weather.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import zzs.com.juhe_weather.dataClass.FutureWeather;
import zzs.com.juhe_weather.dataClass.Real_time_weather;
import zzs.com.juhe_weather.dataClass.TodayWeather;
import zzs.com.juhe_weather.dataClass.Weather;

/**
 * Created by ENIAC on 2018/4/2.
 */

public class WeatherParser {

    private Weather mWeather;

    public Weather parserWeather(String response){
        mWeather = new Weather();
        JSONObject object = JSON.parseObject(response);
        if(object.getInteger("resultcode")==Consts.RETURN_SUCCESSE){
            String data = object.getString("result");
            parserData(data);
        }
        return mWeather;
    }

    private void parserData(String data) {
        JSONObject dataObject = JSON.parseObject(data);

        //obtain real_time_weather
        String real_time = dataObject.getString("sk");
        Real_time_weather real_time_weather = JSON.parseObject(real_time,Real_time_weather.class);
        mWeather.setReal_time_weather(real_time_weather);

        //obtain todaWeather
        String mTodayWeather = dataObject.getString("today");
        TodayWeather todayWeather = JSON.parseObject(mTodayWeather,TodayWeather.class);
        mWeather.setTodayWeather(todayWeather);

        //obtain futureWeather
        String futureArray = dataObject.getString("future");
        ArrayList<FutureWeather> futureList=(ArrayList<FutureWeather>) JSON.parseArray(futureArray, FutureWeather.class);
        mWeather.setFutureList(futureList);
    }
}
