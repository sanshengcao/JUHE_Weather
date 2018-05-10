package zzs.com.juhe_weather.tools;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import zzs.com.juhe_weather.MLog;
import zzs.com.juhe_weather.R;
import zzs.com.juhe_weather.dataClass.Weather_id;

import static zzs.com.juhe_weather.R.id.title_weather_text;

/**
 * Created by ENIAC on 2018/4/9.
 */

public class ImageIconSet {

    public static void setTitleBackground(Context context, RelativeLayout title, Weather_id weatherId) {
        int fa = Integer.parseInt(weatherId.getFa());
        TextView title_temp= title.findViewById(R.id.title_temp_text);
        TextView title_location =  title.findViewById(R.id.title_location);
        TextView title_weather =  title.findViewById(title_weather_text);
        TextView title_wind =  title.findViewById(R.id.title_wind_text);
        if (fa >12&&fa<18||fa>=26&&fa<=28) {
            title.setBackgroundResource(R.drawable.bg_snow);
            title_temp.setTextColor(context.getResources().getColor(R.color.black));
            title_location.setTextColor(context.getResources().getColor(R.color.black));
            title_weather.setTextColor(context.getResources().getColor(R.color.black));
            title_wind.setTextColor(context.getResources().getColor(R.color.black));
        }else if(fa==2||fa==18||fa==19){
            title.setBackgroundResource(R.drawable.bg_cloudy);
            title_temp.setTextColor(context.getResources().getColor(R.color.white));
            title_location.setTextColor(context.getResources().getColor(R.color.white));
            title_weather.setTextColor(context.getResources().getColor(R.color.white));
            title_wind.setTextColor(context.getResources().getColor(R.color.white));
        }else if(fa>2&&fa<13||fa>20&&fa<26){
            title.setBackgroundResource(R.drawable.bg_rain);
            title_temp.setTextColor(context.getResources().getColor(R.color.white));
            title_location.setTextColor(context.getResources().getColor(R.color.white));
            title_weather.setTextColor(context.getResources().getColor(R.color.white));
            title_wind.setTextColor(context.getResources().getColor(R.color.white));
        }else if(fa>=29||fa==20){
            title.setBackgroundResource(R.drawable.bg_dust);
            title_temp.setTextColor(context.getResources().getColor(R.color.white));
            title_location.setTextColor(context.getResources().getColor(R.color.white));
            title_weather.setTextColor(context.getResources().getColor(R.color.white));
            title_wind.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            title.setBackgroundResource(R.drawable.bg_sunny);
            title_temp.setTextColor(context.getResources().getColor(R.color.black));
            title_location.setTextColor(context.getResources().getColor(R.color.black));
            title_weather.setTextColor(context.getResources().getColor(R.color.black));
            title_wind.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    public static void setWeatherIcon(Weather_id weatherId,ImageView weatherIcon) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int fa = Integer.parseInt(weatherId.getFa());
        int fb = Integer.parseInt(weatherId.getFb());
        if(hour>=6&&hour<18){
            switch (fa) {
                case 0:
                    weatherIcon.setImageResource(R.drawable.d00);
                    break;
                case 1:
                    weatherIcon.setImageResource(R.drawable.d01);
                    break;
                case 2:
                    weatherIcon.setImageResource(R.drawable.d02);
                    break;
                case 3:
                    weatherIcon.setImageResource(R.drawable.d03);
                    break;
                case 4:
                    weatherIcon.setImageResource(R.drawable.d04);
                    break;
                case 5:
                    weatherIcon.setImageResource(R.drawable.d05);
                    break;
                case 6:
                    weatherIcon.setImageResource(R.drawable.d06);
                    break;
                case 7:
                    weatherIcon.setImageResource(R.drawable.d07);
                    break;
                case 8:
                    weatherIcon.setImageResource(R.drawable.d08);
                    break;
                case 9:
                    weatherIcon.setImageResource(R.drawable.d09);
                    break;
                case 10:
                    weatherIcon.setImageResource(R.drawable.d10);
                    break;
                case 11:
                    weatherIcon.setImageResource(R.drawable.d11);
                    break;
                case 12:
                    weatherIcon.setImageResource(R.drawable.d12);
                    break;
                case 13:
                    weatherIcon.setImageResource(R.drawable.d13);
                    break;
                case 14:
                    weatherIcon.setImageResource(R.drawable.d14);
                    break;
                case 15:
                    weatherIcon.setImageResource(R.drawable.d15);
                    break;
                case 16:
                    weatherIcon.setImageResource(R.drawable.d16);
                    break;
                case 17:
                    weatherIcon.setImageResource(R.drawable.d17);
                    break;
                case 18:
                    weatherIcon.setImageResource(R.drawable.d18);
                    break;
                case 19:
                    weatherIcon.setImageResource(R.drawable.d19);
                    break;
                case 20:
                    weatherIcon.setImageResource(R.drawable.d20);
                    break;
                case 21:
                    weatherIcon.setImageResource(R.drawable.d21);
                    break;
                case 22:
                    weatherIcon.setImageResource(R.drawable.d22);
                    break;
                case 23:
                    weatherIcon.setImageResource(R.drawable.d23);
                    break;
                case 24:
                    weatherIcon.setImageResource(R.drawable.d24);
                    break;
                case 25:
                    weatherIcon.setImageResource(R.drawable.d25);
                    break;
                case 26:
                    weatherIcon.setImageResource(R.drawable.d26);
                    break;
                case 27:
                    weatherIcon.setImageResource(R.drawable.d27);
                    break;
                case 28:
                    weatherIcon.setImageResource(R.drawable.d28);
                    break;
                case 29:
                    weatherIcon.setImageResource(R.drawable.d29);
                    break;
                case 30:
                    weatherIcon.setImageResource(R.drawable.d30);
                    break;
                case 31:
                    weatherIcon.setImageResource(R.drawable.d31);
                    break;
                case 53:
                    weatherIcon.setImageResource(R.drawable.d53);
                    break;
                default: weatherIcon.setImageResource(R.drawable.d01);
                    break;
            }
        }else{
            switch (fa) {
                case 0:
                    weatherIcon.setImageResource(R.drawable.n00);
                    break;
                case 1:
                    weatherIcon.setImageResource(R.drawable.n01);
                    break;
                case 2:
                    weatherIcon.setImageResource(R.drawable.n02);
                    break;
                case 3:
                    weatherIcon.setImageResource(R.drawable.n03);
                    break;
                case 4:
                    weatherIcon.setImageResource(R.drawable.n04);
                    break;
                case 5:
                    weatherIcon.setImageResource(R.drawable.n05);
                    break;
                case 6:
                    weatherIcon.setImageResource(R.drawable.n06);
                    break;
                case 7:
                    weatherIcon.setImageResource(R.drawable.n07);
                    break;
                case 8:
                    weatherIcon.setImageResource(R.drawable.n08);
                    break;
                case 9:
                    weatherIcon.setImageResource(R.drawable.n09);
                    break;
                case 10:
                    weatherIcon.setImageResource(R.drawable.n10);
                    break;
                case 11:
                    weatherIcon.setImageResource(R.drawable.n11);
                    break;
                case 12:
                    weatherIcon.setImageResource(R.drawable.n12);
                    break;
                case 13:
                    weatherIcon.setImageResource(R.drawable.n13);
                    break;
                case 14:
                    weatherIcon.setImageResource(R.drawable.n14);
                    break;
                case 15:
                    weatherIcon.setImageResource(R.drawable.n15);
                    break;
                case 16:
                    weatherIcon.setImageResource(R.drawable.n16);
                    break;
                case 17:
                    weatherIcon.setImageResource(R.drawable.n17);
                    break;
                case 18:
                    weatherIcon.setImageResource(R.drawable.n18);
                    break;
                case 19:
                    weatherIcon.setImageResource(R.drawable.n19);
                    break;
                case 20:
                    weatherIcon.setImageResource(R.drawable.n20);
                    break;
                case 21:
                    weatherIcon.setImageResource(R.drawable.n21);
                    break;
                case 22:
                    weatherIcon.setImageResource(R.drawable.n22);
                    break;
                case 23:
                    weatherIcon.setImageResource(R.drawable.n23);
                    break;
                case 24:
                    weatherIcon.setImageResource(R.drawable.n24);
                    break;
                case 25:
                    weatherIcon.setImageResource(R.drawable.n25);
                    break;
                case 26:
                    weatherIcon.setImageResource(R.drawable.n26);
                    break;
                case 27:
                    weatherIcon.setImageResource(R.drawable.n27);
                    break;
                case 28:
                    weatherIcon.setImageResource(R.drawable.n28);
                    break;
                case 29:
                    weatherIcon.setImageResource(R.drawable.n29);
                    break;
                case 30:
                    weatherIcon.setImageResource(R.drawable.n30);
                    break;
                case 31:
                    weatherIcon.setImageResource(R.drawable.n31);
                    break;
                case 53:
                    weatherIcon.setImageResource(R.drawable.n53);
                    break;
                default: weatherIcon.setImageResource(R.drawable.n01);
                    break;
            }
        }

    }
}
