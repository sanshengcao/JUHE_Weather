package zzs.com.juhe_weather.serviceAndBroadCast;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;

import zzs.com.juhe_weather.MLog;
import zzs.com.juhe_weather.R;
import zzs.com.juhe_weather.WeatherApplication;

/**
 * Created by ENIAC on 2018/4/25.
 */

public class NetworkService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MLog.e("NetworkService onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (WeatherApplication.isCheckNetwork){
                    Process p=null;
                    try {
                        String ip = "www.baidu.com";
                        p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping三次
                        // ping的状态
                        int status = p.waitFor();
                        MLog.e("net status = "+status);
                        if (status == 0) {
                            WeatherApplication.isNetworkAvailable = true;
                        }else{
                            final Handler handler = new Handler(getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),getString(R.string.network_error),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Thread.sleep(10000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if(p!=null){
                            p.destroy();
                        }
                    }

                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }
}
