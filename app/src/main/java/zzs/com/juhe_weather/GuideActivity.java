package zzs.com.juhe_weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import zzs.com.juhe_weather.serviceAndBroadCast.LocationCity;
import zzs.com.juhe_weather.tools.CityUtils;

public class GuideActivity extends AppCompatActivity {

    private TextView textView;
    private Button start;
    private LocationCity locationCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        textView = findViewById(R.id.guide);
        start = findViewById(R.id.guidebt);

        start();
        initData();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.MainActivity"));
                start.setVisibility(View.GONE);
                GuideActivity.this.finish();
            }
        });
    }

    public void initData(){
        locationCity = new LocationCity(getApplicationContext(),null);
        locationCity.setLocationCity();
        SharedPreferences preferences=getSharedPreferences("Citys", Context.MODE_PRIVATE);
        String citysString=preferences.getString("citys",null);
        if(citysString!=null){
            CityUtils utils = new CityUtils();
            WeatherApplication.cityList = utils.getCitys(citysString);
            MLog.e("GuideActivity initData WeatherApplication.cityList size="
                    +WeatherApplication.cityList.size());
        }
    }

    @Override
    protected void onDestroy() {
        locationCity.stopLocation();
        super.onDestroy();
    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }

                int i=3;
                while (i>=0){

                    MLog.e("i="+i);
                    Message msg = new Message();
                    msg.what = i;
                    handler.sendMessage(msg);
                    i--;
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3:
                    textView.setText("3");
                    break;
                case 2:
                    textView.setText("2");
                    break;
                case 1:
                    textView.setText("1");
                    break;
                case 0:
                    textView.setText("");
                    start.setVisibility(View.VISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
