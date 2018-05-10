package zzs.com.juhe_weather;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zzs.com.juhe_weather.dataClass.City;
import zzs.com.juhe_weather.dataClass.FutureWeather;
import zzs.com.juhe_weather.dataClass.Weather;
import zzs.com.juhe_weather.tools.Consts;
import zzs.com.juhe_weather.tools.ImageIconSet;
import zzs.com.juhe_weather.tools.URLTools;
import zzs.com.juhe_weather.tools.WeatherParser;
import zzs.com.juhe_weather.tools.WeatherSQLUtils;

/**
 * Created by ENIAC on 2018/3/31.
 */

public class CityFragment extends Fragment {

    private City mCity;
    private Weather mWeather = null;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mCity =getArguments().getParcelable("city");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.city_weather, container, false);
            MLog.e("CityFragment onCreateView mCity = "+mCity.toString());
            if(WeatherApplication.isNetworkAvailable&&mWeather == null){
                //有网络加载网络数据
                if(mCity.getId()==Consts.LOCATION_CITY_ID &&mCity.getLongitude()!=0.0){
                    String url= URLTools.getURLForLocation(mCity.getLongitude()
                            ,mCity.getLatitude(),Consts.Type_Json2);
                    try{
                        getWeatherForCityOrId(url);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                } else if(mCity.getDistrict()!=null){
                    String url= URLTools.getURL_City(mCity.getDistrict(),Consts.Type_Json2);
                    try{
                        getWeatherForCityOrId(url);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }else {
                //没有网络加载本地数据
                WeatherSQLUtils utils = new WeatherSQLUtils();
                mWeather=utils.queryWeather(getContext(),mCity);

                setViews();
            }
            View iew ;

        return view;
    }


    @Override
    public void onResume() {
        MLog.e("CityFragment onResume");
        super.onResume();
    }

    public City getCity(){
        return mCity;
    }

    private void setViews() {
        RelativeLayout title =  view.findViewById(R.id.titlerl);
        TextView title_temp= view.findViewById(R.id.title_temp_text);
        TextView title_location =  view.findViewById(R.id.title_location);
        TextView title_weather =  view.findViewById(R.id.title_weather_text);
        TextView title_wind =  view.findViewById(R.id.title_wind_text);
        TextView moreWeather = view.findViewById(R.id.moreweather);

        moreWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setAction("com.MoreWeatherActivity");
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("FutureWeather",mWeather.getFutureList());
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        if(mWeather!=null){
            ImageIconSet.setTitleBackground(getContext(),title,mWeather.getTodayWeather().getWeather_id());
            String temp = String.format(getString(R.string.show_temp), mWeather.getReal_time_weather().getTemp());
            title_temp.setText(temp);
            if(mCity.getId()==Consts.LOCATION_CITY_ID &&mCity.getAddress()!=null) {
                title_location.setText(mCity.getAddress());
            }else {
                title_location.setText(String.format(getString(R.string.show_address)
                        , mCity.getCity(),mCity.getDistrict()));
            }
            title_weather.setText(mWeather.getTodayWeather().getWeather());
            title_wind.setText(mWeather.getTodayWeather().getWind());
            setFutureWeather(mWeather.getFutureList());
        }

    }

    public void getWeatherForCityOrId(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        final Request.Builder builder = new Request.Builder();
        MLog.e("url = "+url);
        Request request = builder.get().url(url).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response res) throws IOException {

               int code = res.code();

               if(code == Consts.RETURN_SUCCESSE) {
                   String rescall = res.body().string();
                   WeatherSQLUtils utils = new WeatherSQLUtils();
                   MLog.e("Response="+rescall);
                   if ((rescall.indexOf("html")==-1)&&(rescall.indexOf("head")==-1)&&(rescall.indexOf("body")==-1)) {
                       WeatherParser wp = new WeatherParser();
                       mWeather = wp.parserWeather(rescall);

                       if (mWeather != null) {
                           //把获取到的天气存入数据库

                           utils.storeWeather(getContext(),mCity,mWeather);

                           //再初始化View
                           CityFragment.this.getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   setViews();
                               }
                           });
                           MLog.e("onResponse :" + "mWeather="
                                   + mWeather.getTodayWeather().getWeather()
                                   + " mCity=" + mWeather.getTodayWeather().getCity());
                       }
                   }else{
                       mWeather=utils.queryWeather(getContext(),mCity);
                       if(mWeather!=null){
                           CityFragment.this.getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   setViews();
                               }
                           });
                       }
                   }
               }
            }

            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }
        });
    }

    public void setFutureWeather(List<FutureWeather> futureWeathers) {
        FutureWeather today_weather = futureWeathers.get(0);
        FutureWeather second_weather = futureWeathers.get(1);
        FutureWeather third_weather = futureWeathers.get(2);

        ViewGroup vp_today =  view.findViewById(R.id.item_day_weather_0);
        TextView time_day_today = vp_today.findViewById(R.id.time_day);
        TextView weather_today = vp_today.findViewById(R.id.item_weather_text);
        ImageView weather_icon_today = vp_today.findViewById(R.id.weather_icon);
        TextView wind_today = vp_today.findViewById(R.id.item_wind_text);
        TextView temp_today = vp_today.findViewById(R.id.item_temp_text);

        //set weather
        time_day_today.setText(R.string.today);
        weather_today.setText(today_weather.getWeather());
        ImageIconSet.setWeatherIcon(today_weather.getWeather_id(),weather_icon_today);
        wind_today.setText(today_weather.getWind());
        temp_today.setText(today_weather.getTemperature());


        ViewGroup vp_second =  view.findViewById(R.id.item_day_weather_1);
        TextView time_day_second = vp_second.findViewById(R.id.time_day);
        TextView weather_second = vp_second.findViewById(R.id.item_weather_text);
        ImageView weather_icon_second = vp_second.findViewById(R.id.weather_icon);
        TextView wind_second = vp_second.findViewById(R.id.item_wind_text);
        TextView temp_second = vp_second.findViewById(R.id.item_temp_text);

        time_day_second.setText(R.string.tomorrow);
        weather_second.setText(second_weather.getWeather());
        ImageIconSet.setWeatherIcon(second_weather.getWeather_id(),weather_icon_second);
        wind_second.setText(second_weather.getWind());
        temp_second.setText(second_weather.getTemperature());

        ViewGroup vp_third = view.findViewById(R.id.item_day_weather_2);
        TextView time_day_third = vp_third.findViewById(R.id.time_day);
        TextView weather_third = vp_third.findViewById(R.id.item_weather_text);
        ImageView weather_icon_third = vp_third.findViewById(R.id.weather_icon);
        TextView wind_third = vp_third.findViewById(R.id.item_wind_text);
        TextView temp_third = vp_third.findViewById(R.id.item_temp_text);

        time_day_third.setText(third_weather.getWeek());
        weather_third.setText(third_weather.getWeather());
        ImageIconSet.setWeatherIcon(third_weather.getWeather_id(),weather_icon_third);
        wind_third.setText(third_weather.getWind());
        temp_third.setText(third_weather.getTemperature());

    }


}
