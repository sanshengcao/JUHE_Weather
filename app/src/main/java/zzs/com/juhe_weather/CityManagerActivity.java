package zzs.com.juhe_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


import zzs.com.juhe_weather.dataClass.City;
import zzs.com.juhe_weather.dataClass.Weather;
import zzs.com.juhe_weather.tools.Consts;
import zzs.com.juhe_weather.tools.WeatherSQLUtils;

public class CityManagerActivity extends AppCompatActivity {

    private List<City> mCities;
    private List<Weather> mWeatherList;
    private CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);

        initData();
        //设置toolBar
        Toolbar toolbar = findViewById(R.id.city_manager_toolbar);
        ImageView editor_city = findViewById(R.id.editor_city);
        FloatingActionButton fab =  findViewById(R.id.fab);
        toolbar.setTitle(getString(R.string.city_manager));
        toolbar.setNavigationIcon(R.drawable.cancel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //点击监听
        CityManagerOnClickListener onClickListener = new CityManagerOnClickListener();
        fab.setOnClickListener(onClickListener);
        editor_city.setOnClickListener(onClickListener);


        ListView cityListView = findViewById(R.id.citylist);

        cityAdapter = new CityAdapter();
        cityListView.setAdapter(cityAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                isCityChanged();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                isCityChanged();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        initData();
        MLog.e("CityManagerActivity onRestart mWeatherList="+mWeatherList.size());
        cityAdapter.notifyDataSetChanged();
        super.onRestart();
    }

    public void initData(){
        mCities = WeatherApplication.cityList;
        WeatherSQLUtils weatherSQLUtils = new WeatherSQLUtils();
        mWeatherList = weatherSQLUtils.queryWeathers(getApplicationContext(),mCities);
    }

    public void isCityChanged(){
        if(false){
            WeatherApplication.isCityListChanged=false;
            Intent intent = new Intent("com.MainActivity");
            intent.putExtra("FLAG", Consts.FLAG_CHANGED_CITY);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CityManagerActivity.this.finish();
        }else {
            CityManagerActivity.this.finish();
        }
    }

    private class CityManagerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fab:
                    startActivity(new Intent("zzs.com.juhe_weather.ActivityCityAdd"));
                    break;
                case R.id.editor_city:
                    startActivity(new Intent("zzs.com.juhe_weather.ManageCity"));
                    break;
            }
        }
    }

    class CityAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mCities.size();
        }

        @Override
        public Object getItem(int position) {
            return mCities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder ;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(CityManagerActivity.this,R.layout.city_item,null);
                viewHolder.weather = convertView.findViewById(R.id.item_city_manager_weather);
                viewHolder.cityName = convertView.findViewById(R.id.item_city_manager_city);
                viewHolder.district = convertView.findViewById(R.id.item_city_manager_district);
                viewHolder.temp = convertView.findViewById(R.id.item_city_manager_temp);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            City city = mCities.get(position);
            Weather weather = null;
            if(position<mWeatherList.size()) {
                 weather = mWeatherList.get(position);
            }
            String cityName = city.getCity();
            String province = city.getProvince();
            String district = city.getDistrict();

            if(district == null){
                viewHolder.district.setText(cityName);
            } else if(district.equals(cityName)){
                viewHolder.district.setText(district);
                viewHolder.cityName.setText(province);
            }else {
                viewHolder.district.setText(district);
                if(cityName.equals(province)){
                    viewHolder.cityName.setText(province);
                }else{
                    viewHolder.cityName.setText(String
                            .format(getString(R.string.city_province),city.getCity(),city.getProvince()));
                }
            }
            if(weather!=null){
                viewHolder.weather.setText(weather.getTodayWeather().getWeather());
                viewHolder.temp.setText(weather.getTodayWeather().getTemperature());
            }else{
                viewHolder.weather.setText("");
                viewHolder.temp.setText(getString(R.string.no_weather));
            }
            return convertView;
        }
    }

    private class ViewHolder {
         TextView temp;
        TextView weather;
         TextView cityName;
         TextView district;
    }


}
