package zzs.com.juhe_weather.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import zzs.com.juhe_weather.MLog;
import zzs.com.juhe_weather.WeatherApplication;
import zzs.com.juhe_weather.dataClass.City;
import zzs.com.juhe_weather.dataClass.FutureWeather;
import zzs.com.juhe_weather.dataClass.Real_time_weather;
import zzs.com.juhe_weather.dataClass.TodayWeather;
import zzs.com.juhe_weather.dataClass.Weather;

/**
 * Created by ENIAC on 2018/4/19.
 */

public class WeatherSQLUtils {

    public void storeWeather(final Context context, final City city,final Weather weather){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                WeatherSQLiteHelper sqLiteHelper = new WeatherSQLiteHelper(context);
                SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                String real_weather = JSON.toJSONString(weather.getReal_time_weather());
                String today_weather = JSON.toJSONString(weather.getTodayWeather());
                String future_weather = JSON.toJSONString(weather.getFutureList());
                MLog.e("id = "+city.getId()+" real_weather = "+real_weather);
                MLog.e(" today_weather ="+today_weather);
                MLog.e(" future_weather ="+future_weather);

                String sql = "REPLACE INTO WeatherDB VALUES (" +city.getId()+","+"'"+city.getProvince()+"'"+","+"'"+city.getCity()+"'"+","
                        +"'"+city.getDistrict()+"'"+","+"'"+real_weather+"'"+","+"'"+today_weather+"'"
                        +","+"'"+future_weather+"'"+")";

                db.execSQL(sql);
                db.close();
            }
        };
        WeatherApplication.mThreadPool.execute(runnable);
    }


    public Weather queryWeather(Context context,City city){
        WeatherSQLiteHelper sqLiteHelper = new WeatherSQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        int id = city.getId();
        String[] columns = new String[]{"real_w","today_w","future_w"};

        Cursor cursor=db.query(WeatherSQLiteHelper.WeatherDB,columns,"id=?",
                new String[]{String.valueOf(id)},null,null,null);
        Weather weather = null;
        while (cursor.moveToNext()){
            weather=new Weather();
            String real_time_w =null,today_w =null, future_w =null;

            real_time_w=cursor.getString(cursor.getColumnIndex("real_w"));
            today_w = cursor.getString(cursor.getColumnIndex("today_w"));
            future_w = cursor.getString(cursor.getColumnIndex("future_w"));

            Real_time_weather real_time_weather= JSON.parseObject(real_time_w,Real_time_weather.class);
            TodayWeather todayWeather = JSON.parseObject(today_w,TodayWeather.class);
            ArrayList<FutureWeather> futureList=
                    (ArrayList<FutureWeather>) JSON.parseArray(future_w, FutureWeather.class);

            weather.setReal_time_weather(real_time_weather);
            weather.setTodayWeather(todayWeather);
            weather.setFutureList(futureList);
        }

        cursor.close();
        db.close();
        return  weather;
    }


    public List<Weather> queryWeathers(Context context,List<City> cities){
        WeatherSQLiteHelper sqLiteHelper = new WeatherSQLiteHelper(context);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        List<Weather> weathers= new ArrayList<>();
        for(City city:cities){
            int id = city.getId();
            Weather weather = null;
            String[] columns = new String[]{"real_w","today_w","future_w"};

            Cursor cursor=db.query(WeatherSQLiteHelper.WeatherDB,columns,"id=?",
                    new String[]{String.valueOf(id)},null,null,null);

            while (cursor.moveToNext()){
                weather=new Weather();
                String real_time_w =null,today_w =null, future_w =null;

                real_time_w=cursor.getString(cursor.getColumnIndex("real_w"));
                today_w = cursor.getString(cursor.getColumnIndex("today_w"));
                future_w = cursor.getString(cursor.getColumnIndex("future_w"));

                Real_time_weather real_time_weather= JSON.parseObject(real_time_w,Real_time_weather.class);
                TodayWeather todayWeather = JSON.parseObject(today_w,TodayWeather.class);
                ArrayList<FutureWeather> futureList=
                        (ArrayList<FutureWeather>) JSON.parseArray(future_w, FutureWeather.class);

                weather.setReal_time_weather(real_time_weather);
                weather.setTodayWeather(todayWeather);
                weather.setFutureList(futureList);
            }
            if(weather!=null){
                weathers.add(weather);
            }
            cursor.close();
        }
        db.close();
        return weathers;
    }

    public void deleteWeather(final Context context,final City city){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                WeatherSQLiteHelper sqLiteHelper = new WeatherSQLiteHelper(context);
                SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                int id = city.getId();
                db.delete(WeatherSQLiteHelper.WeatherDB,"id=?",new String[]{String.valueOf(id)});
                db.close();
            }
        };
        WeatherApplication.mThreadPool.execute(runnable);

    }

    public void updateWeather(final Context context,final City city,final Weather weather){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                WeatherSQLiteHelper sqLiteHelper = new WeatherSQLiteHelper(context);
                SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                int id = city.getId();
                String real_weather = JSON.toJSONString(weather.getReal_time_weather());
                String today_weather = JSON.toJSONString(weather.getTodayWeather());
                String future_weather = JSON.toJSONString(weather.getFutureList());
                ContentValues values = new ContentValues();
                values.put("id",id);
                values.put("province",city.getProvince());
                values.put("city",city.getCity());
                values.put("district",city.getDistrict());
                values.put("real_w",real_weather);
                values.put("today_w",today_weather);
                values.put("future_w",future_weather);

                db.update(WeatherSQLiteHelper.WeatherDB,values,"id=?",new String[]{String.valueOf(id)});

                db.close();
            }
        };
        WeatherApplication.mThreadPool.execute(runnable);
    }
}
