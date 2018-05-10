package zzs.com.juhe_weather.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zzs.com.juhe_weather.MLog;
import zzs.com.juhe_weather.WeatherApplication;
import zzs.com.juhe_weather.dataClass.City;

/**
 * Created by ENIAC on 2018/4/3.
 */

public class CityUtils {

    /**
     *解析从服务器获取的城市列表
     * @param response 网络获取的json字符串
     * @return All City List
     */
    public  ArrayList<City> parserCitys(String response){
        JSONObject object = JSONObject.parseObject(response);
        ArrayList<City> cities = new ArrayList<>();
        if(object.getInteger("resultcode")==Consts.RETURN_SUCCESSE){
            String result = object.getString("result");
            cities = (ArrayList<City>)JSON.parseArray(result,City.class);
        }
        return cities;
    }

    /**
     * 解析本地存储的当前城市列表
     * @param citys 从sharedpreference中获取的json字符串
     * @return 当前选择的城市列表
     */
    public  ArrayList<City> getCitys(String citys){
        ArrayList<City> cities = new ArrayList<>();
        cities=(ArrayList<City>)JSON.parseArray(citys,City.class);
        return cities;
    }

    /**
     * 单独存储城市
     * @param context
     * @param city
     */
    public void storeCity(final Context context,final City city){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                CitySQLiteHelper helper = new CitySQLiteHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(City.ID,city.getId());
                values.put(City.Province,city.getProvince());
                values.put(City.CityName,city.getCity());
                values.put(City.District,city.getDistrict());
                db.insert(CitySQLiteHelper.CityDB,null,values);
                db.close();
            }
        };
        WeatherApplication.mThreadPool.execute(runnable);
    }

    public void storeCity(final Context context,final List<City> cityList){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                CitySQLiteHelper helper = new CitySQLiteHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();
                for(City city:cityList){
                    ContentValues values = new ContentValues();
                    values.put(City.ID,city.getId());
                    values.put(City.Province,city.getProvince());
                    values.put(City.CityName,city.getCity());
                    values.put(City.District,city.getDistrict());
                    db.insert(CitySQLiteHelper.CityDB,null,values);
                }
                db.close();
            }
        };
        WeatherApplication.mThreadPool.execute(runnable);
    }

    /**
     * query city by city id
     * @param context
     * @param id city_id
     * @return
     */
    public City queryCity(Context context,int id){
        CitySQLiteHelper helper = new CitySQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = new String[]{"id","province","city","district"};
        City city = new City();

        Cursor cursor = db.query("CityDB", columns, "id=?",
                new String[]{String.valueOf(id)}, null, null, "id");

        while (cursor.moveToNext()){
            int cityid = cursor.getInt(cursor.getColumnIndex("id"));
            String province =cursor.getString(cursor.getColumnIndex("province"));
            String cityName = cursor.getString(cursor.getColumnIndex("city"));
            String district = cursor.getString(cursor.getColumnIndex("district"));
            city.setId(cityid);
            city.setProvince(province);
            city.setCity(cityName);
            city.setDistrict(district);
        }

        cursor.close();
        db.close();

        return city;
    }

    /**
     * query city by cityName
     * @param context
     * @param cityName
     * @return one or more citys
     */
    public List<City> queryCity(Context context,String cityName){
        CitySQLiteHelper helper = new CitySQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<City> cities = new ArrayList<City>();
        //自己拼接sql语句 模糊查询

        String sql = "select "+"id,"+"province,"+"city,"+"district"
                +" from CityDB where district like '%"+cityName +"%'";
        Cursor cursor =db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            City city = new City();
            int cityid = cursor.getInt(cursor.getColumnIndex("id"));
            String province =cursor.getString(cursor.getColumnIndex("province"));
            String name = cursor.getString(cursor.getColumnIndex("city"));
            String district = cursor.getString(cursor.getColumnIndex("district"));
            city.setId(cityid);
            city.setProvince(province);
            city.setCity(name);
            city.setDistrict(district);
            cities.add(city);
        }
        MLog.e("queryCity cities size="+cities.size()+"count "+cursor.getCount());
        cursor.close();
        db.close();
        return cities;
    }

    public ArrayList<City> queryAllCity(Context context){
        CitySQLiteHelper cityHelper = new CitySQLiteHelper(context);
        SQLiteDatabase database = cityHelper.getWritableDatabase();
        ArrayList<City> cities = new ArrayList<City>();
        String sql = "select * from CityDB";
        Cursor cursor=database.rawQuery(sql,null);
        MLog.e("count = "+cursor.getCount());
        while(cursor.moveToNext()){
            City city = new City();
            int cityId = cursor.getInt(cursor.getColumnIndex("id"));
            String province =cursor.getString(cursor.getColumnIndex("province"));
            String cityName = cursor.getString(cursor.getColumnIndex("city"));
            String district = cursor.getString(cursor.getColumnIndex("district"));
            city.setId(cityId);
            city.setProvince(province);
            city.setCity(cityName);
            city.setDistrict(district);
            cities.add(city);
        }
        cursor.close();
        database.close();
        return cities;
    }

}
