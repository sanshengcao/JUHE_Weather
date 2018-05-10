package zzs.com.juhe_weather;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import zzs.com.juhe_weather.dataClass.City;

/**
 * Created by ENIAC on 2018/4/13.
 */

public class WeatherApplication extends Application {
    public static ArrayList<City> cityList;
    public static boolean isCheckNetwork;
    public static boolean isNetworkAvailable;
    public static City mLocationCity = null;
    public static ThreadPoolExecutor mThreadPool;
    public static Boolean isCityListChanged =false;

    @Override
    public void onCreate() {
        MLog.e("WeatherApplication onCreate");
        cityList = new ArrayList<City>();
        isCheckNetwork = true;
        isNetworkAvailable = false;
        mThreadPool = new ThreadPoolExecutor(1,5,10, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(8));
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        isCheckNetwork = false;
        super.onLowMemory();
    }
}
