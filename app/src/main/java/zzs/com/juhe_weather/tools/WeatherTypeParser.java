package zzs.com.juhe_weather.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import zzs.com.juhe_weather.dataClass.WeatherType;


/**
 * Created by ENIAC on 2018/4/3.
 */

public class WeatherTypeParser {

    public  static  ArrayList<WeatherType> getWeatherTypes(){
        String weatherType ="[{\"wid\":\"00\",\"weather\":\"晴\"},{\"wid\":\"01\",\"weather\":\"多云\"},{\"wid\":\"02\",\"weather\":\"阴\"},{\"wid\":\"03\",\"weather\":\"阵雨\"},{\"wid\":\"04\",\"weather\":\"雷阵雨\"},{\"wid\":\"05\",\"weather\":\"雷阵雨伴有冰雹\"},{\"wid\":\"06\",\"weather\":\"雨夹雪\"},{\"wid\":\"07\",\"weather\":\"小雨\"}," +
                "{\"wid\":\"08\",\"weather\":\"中雨\"},{\"wid\":\"09\",\"weather\":\"大雨\"},{\"wid\":\"10\",\"weather\":\"暴雨\"},{\"wid\":\"11\",\"weather\":\"大暴雨\"},{\"wid\":\"12\",\"weather\":\"特大暴雨\"},{\"wid\":\"13\",\"weather\":\"阵雪\"},{\"wid\":\"14\",\"weather\":\"小雪\"},{\"wid\":\"15\",\"weather\":\"中雪\"}," +
                "{\"wid\":\"16\",\"weather\":\"大雪\"},{\"wid\":\"17\",\"weather\":\"暴雪\"},{\"wid\":\"18\",\"weather\":\"雾\"},{\"wid\":\"19\",\"weather\":\"冻雨\"},{\"wid\":\"20\",\"weather\":\"沙尘暴\"},{\"wid\":\"21\",\"weather\":\"小雨-中雨\"},{\"wid\":\"22\",\"weather\":\"中雨-大雨\"},{\"wid\":\"23\",\"weather\":\"大雨-暴雨\"}," +
                "{\"wid\":\"24\",\"weather\":\"暴雨-大暴雨\"},{\"wid\":\"25\",\"weather\":\"大暴雨-特大暴雨\"},{\"wid\":\"26\",\"weather\":\"小雪-中雪\"},{\"wid\":\"27\",\"weather\":\"中雪-大雪\"},{\"wid\":\"28\",\"weather\":\"大雪-暴雪\"},{\"wid\":\"29\",\"weather\":\"浮尘\"},{\"wid\":\"30\",\"weather\":\"扬沙\"}," +
                "{\"wid\":\"31\",\"weather\":\"强沙尘暴\"},{\"wid\":\"53\",\"weather\":\"霾\"}]";
        ArrayList<WeatherType> weatherTypes =new ArrayList<WeatherType>();
        weatherTypes = (ArrayList<WeatherType>) JSON.parseArray(weatherType,WeatherType.class);
        return weatherTypes;
    }
}