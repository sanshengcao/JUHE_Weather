package zzs.com.juhe_weather.dataClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ENIAC on 2018/4/2.
 */

public class FutureWeather implements Parcelable{

    private String temperature;
    private String weather;
    private Weather_id weather_id;
    private String wind;
    private String week;
    private String date;

    public FutureWeather(){
        this("","",null,"","","");
    }

    public FutureWeather(String temperature,String weather,Weather_id id,String wind,
                         String week,String date){
        this.temperature= temperature;
        this.weather = weather;
        this.weather_id = id;
        this.wind = wind;
        this.week = week;
        this.date = date;
    }


    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather_id(Weather_id weather_id) {
        this.weather_id = weather_id;
    }

    public Weather_id getWeather_id() {
        return weather_id;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWind() {
        return wind;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeek() {
        return week;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        int pamre=31;
        int result=1;
        result=pamre*result+(
                temperature==null?0:temperature.hashCode());
        result=pamre*result+(
                weather==null?0:weather.hashCode());
        result=pamre*result+(
                wind==null?0:wind.hashCode());
        result=pamre*result+(
                week==null?0:week.hashCode());
        result=pamre*result+(
                date==null?0:date.hashCode());
        result=pamre*result+
                (weather_id==null?0:weather_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FutureWeather){
            FutureWeather fw = (FutureWeather) obj;
            if(fw.getWeather().equals(this.weather)&&fw.temperature.equals(this.temperature)
                    &&fw.weather_id.equals(this.weather_id)&&fw.wind.equals(this.wind)
                    &&fw.week.equals(this.week)&&fw.date.equals(this.date)){
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(temperature);
        dest.writeString(weather);
        dest.writeParcelable(weather_id,flags);
        dest.writeString(wind);
        dest.writeString(week);
        dest.writeString(date);
    }

    public static  final Parcelable.Creator<FutureWeather> CREATOR = new Creator<FutureWeather>() {
        @Override
        public FutureWeather createFromParcel(Parcel in) {

            String tp = in.readString();
            String wt = in.readString();
            Weather_id wid = in.readParcelable(Weather_id.class.getClassLoader());
            String win = in.readString();
            String wk = in.readString();
            String dt = in.readString();

            return new FutureWeather(tp,wt,wid,win,wk,dt);
        }

        @Override
        public FutureWeather[] newArray(int size) {
            return new FutureWeather[size];
        }
    };
}
