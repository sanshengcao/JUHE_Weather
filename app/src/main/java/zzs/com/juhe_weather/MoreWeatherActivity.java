package zzs.com.juhe_weather;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import zzs.com.juhe_weather.dataClass.FutureWeather;
import zzs.com.juhe_weather.dataClass.Weather_id;
import zzs.com.juhe_weather.tools.ImageIconSet;

public class MoreWeatherActivity extends AppCompatActivity {

    private ArrayList<FutureWeather> futureWeathers;
    private ListView mListView;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_weather);
        futureWeathers= getIntent().getParcelableArrayListExtra("FutureWeather");
        mListView = findViewById(R.id.more_list);

        mToolbar = (Toolbar) findViewById(R.id.more_weather_toolbar);
        setSupportActionBar(mToolbar);
        mListView.setAdapter(new MoreWeatherAdapter());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    public class MoreWeatherAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return futureWeathers.size();
        }

        @Override
        public Object getItem(int position) {
            return futureWeathers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView==null){
                convertView = View.inflate(MoreWeatherActivity.this,R.layout.item_more,null);
                viewHolder = new ViewHolder();
                viewHolder.more_date = convertView.findViewById(R.id.item_more_date);
                viewHolder.icon = convertView.findViewById(R.id.item_more_icon);
                viewHolder.more_weather = convertView.findViewById(R.id.item_more_weather);
                viewHolder.more_wind = convertView.findViewById(R.id.item_more_wind);
                viewHolder.more_temp = convertView.findViewById(R.id.item_more_temp);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            FutureWeather futureWeather = futureWeathers.get(position);
            String week = futureWeather.getWeek();
            String date = futureWeather.getDate();
            Weather_id weather_id = futureWeather.getWeather_id();
            viewHolder.more_date.setText(FormatDate(week,date));
            ImageIconSet.setWeatherIcon(weather_id,viewHolder.icon);
            viewHolder.more_weather.setText(futureWeather.getWeather());
            viewHolder.more_wind.setText(futureWeather.getWind());
            viewHolder.more_temp.setText(futureWeather.getTemperature());

            return convertView;
        }
    }

    public String FormatDate(String week,String date){
        int year = Integer.parseInt(date.substring(0,4));
        int mon = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        String d = week+"  "+mon+getString(R.string.month)+day+getString(R.string.day);
        return d;
    }

    class ViewHolder {
        TextView more_date;
        ImageView icon;
        TextView more_weather;
        TextView more_wind;
        TextView more_temp;
    }
}
